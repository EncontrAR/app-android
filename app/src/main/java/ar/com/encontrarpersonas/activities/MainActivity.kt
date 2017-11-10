package ar.com.encontrarpersonas.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.annotation.RequiresPermission
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import ar.com.encontrarpersonas.App
import ar.com.encontrarpersonas.R
import ar.com.encontrarpersonas.api.EncontrarRestApi
import ar.com.encontrarpersonas.data.models.Campaign
import ar.com.encontrarpersonas.data.models.Position
import ar.com.encontrarpersonas.extensions.userHasGrantedLocationPermission
import ar.com.encontrarpersonas.screens.detail.DetailScreen
import ar.com.encontrarpersonas.screens.home.HomeScreen
import ar.com.encontrarpersonas.screens.settings.SettingsScreen
import ar.com.encontrarpersonas.services.JobDispatcher
import com.crashlytics.android.Crashlytics
import com.google.android.gms.location.LocationServices
import com.mcxiaoke.koi.async.asyncDelay
import com.wealthfront.magellan.ActionBarConfig
import com.wealthfront.magellan.NavigationListener
import com.wealthfront.magellan.Navigator
import com.wealthfront.magellan.support.SingleActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * MIT License
 *
 * Copyright (c) 2017 Proyecto Encontrar
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 *
 */
class MainActivity : SingleActivity(), NavigationListener {

    // Public constants
    companion object {
        val EXTRA_CAMPAIGN = "extraCampaign"
    }

    // Private constants
    private val REQUEST_CODE_LOCATION = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.activity_toolbar)
        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.text_primary))
    }

    /*
        Handle new incoming intents (usually coming through a notification being opened) when
        the Activity is already started
     */
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        // If the intent received a campaign, go directly to the detail view of that campaign
        if (intent.extras != null && intent.extras.containsKey(EXTRA_CAMPAIGN)) {
            getNavigator().goTo(DetailScreen(
                    (intent.extras.getSerializable(EXTRA_CAMPAIGN) as Campaign)
            ))
        }
    }

    /*
        Set up a Magellan Navigator with the root Screen
    */
    override fun createNavigator(): Navigator {
        return Navigator.withRoot(HomeScreen()).build()
    }

    @SuppressLint("MissingPermission")
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        // If the intent received a campaign, go directly to the detail view of that campaign
        if (intent.extras != null && intent.extras.containsKey(EXTRA_CAMPAIGN)) {

            // There is no callback for being notified when Magellan is done loading the views,
            // and we MUST be sure that Google services are initialized before going to
            // the Detail View. Since this won't happen until the UI thread is done with the setup,
            // we free the UI thread for a while. Even 1 millisecond should be enough, we just need
            // to let Magellan's and Anvil's lifecycles finnish. ¯\_(ツ)_/¯
            asyncDelay(800, {
                runOnUiThread {
                    getNavigator().goTo(DetailScreen(
                            (intent.extras.getSerializable(EXTRA_CAMPAIGN) as Campaign)
                    ))
                }
            })
        }

        // If location permission has been previously granted, send the location each time
        // the app is opened
        if (userHasGrantedLocationPermission()) {
            sendCurrentLocationToApi()
        }
    }


    /*
        Hide or show the Toolbar depending on each Screen configuration
    */
    override fun onNavigate(actionBarConfig: ActionBarConfig) {
        // Do something with the toolbar if the screen changes
        if (actionBarConfig.visible()) {
            supportActionBar?.show()
        } else {
            supportActionBar?.hide()
        }
    }

    /*
        Inflate custom menu on the Toolbar
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        getNavigator().onCreateOptionsMenu(menu) // Important, let Magellan deal with the Menu
        return true
    }

    /*
        Handle Toolbar actions
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.main_menu_settings -> {
                getNavigator().goTo(SettingsScreen())
                return true
            }

            R.id.main_menu_share -> {
                val screen = getNavigator().currentScreen()
                if (screen is DetailScreen) screen.presenter.shareCampaign()
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    /*
        Handle runtime permissions
     */
    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE_LOCATION -> {
                // Check if the location permission has been granted
                if (grantResults.isNotEmpty()
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Right after the user has accepted the location permission, send the current
                    // location for the first time
                    sendCurrentLocationToApi()

                    // Start the location update background cronjob
                    JobDispatcher.startRecurrentLocationUpdateJob()

                } else {
                    // Location permission denied, do nothing...
                }
            }
        }
    }

    /**
     * Requests the location permission to the user.
     */
    fun requestLocationPermission() {
        if (!userHasGrantedLocationPermission()) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show rationale and request the permission again.
                showRationaleDialog(
                        getString(R.string.permission_location_name),
                        getString(R.string.permission_location_rationale),
                        DialogInterface.OnClickListener { _, _ ->
                            ActivityCompat.requestPermissions(this,
                                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                    REQUEST_CODE_LOCATION)
                        }
                )

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        REQUEST_CODE_LOCATION)
            }
        }
    }

    /**
     * Displays a dialog with the provided title and message.
     */
    private fun showRationaleDialog(title: String,
                                    message: String,
                                    onOkClick: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(getString(R.string.permission_message_ok), onOkClick)
                .create()
                .show()
    }

    /**
     * Request the last known position (usually the current one) of the device to the location
     * manager and sends it to the API.
     */
    @SuppressLint("MissingPermission")
    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    private fun sendCurrentLocationToApi() {
        LocationServices
                .getFusedLocationProviderClient(App.sInstance)
                .lastLocation
                .addOnSuccessListener { lastKnownLocation ->
                    if (lastKnownLocation != null) {
                        EncontrarRestApi
                                .deviceUser
                                .updateLoggedDevicePosition(
                                        Position(
                                                lastKnownLocation.latitude.toString(),
                                                lastKnownLocation.longitude.toString())
                                )
                                .enqueue(object : Callback<List<Campaign>> {
                                    override fun onResponse(call: Call<List<Campaign>>?,
                                                            response: Response<List<Campaign>>?) {
                                        if (response == null || !response.isSuccessful) {
                                            Crashlytics.log("The server rejected the latest" +
                                                    " position update: $lastKnownLocation")
                                        }
                                    }

                                    override fun onFailure(call: Call<List<Campaign>>?,
                                                           t: Throwable?) {
                                        Crashlytics.log("Couldn't send the latest position" +
                                                " to the server: $lastKnownLocation")
                                    }

                                })
                    } else {
                        Crashlytics.log("Last known location was null, most probably" +
                                " the device has the location setting disabled by the user.")
                    }
                }
    }

}