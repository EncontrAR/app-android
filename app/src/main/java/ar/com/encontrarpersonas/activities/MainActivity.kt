package ar.com.encontrarpersonas.activities

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import ar.com.encontrarpersonas.R
import ar.com.encontrarpersonas.data.models.Campaign
import ar.com.encontrarpersonas.extensions.userHasGrantedLocationPermission
import ar.com.encontrarpersonas.screens.detail.DetailScreen
import ar.com.encontrarpersonas.screens.home.HomeScreen
import ar.com.encontrarpersonas.screens.settings.SettingsScreen
import ar.com.encontrarpersonas.services.JobDispatcher
import com.wealthfront.magellan.ActionBarConfig
import com.wealthfront.magellan.NavigationListener
import com.wealthfront.magellan.Navigator
import com.wealthfront.magellan.support.SingleActivity

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

    /*
        Set up a Magellan Navigator with the root Screen
     */
    override fun createNavigator(): Navigator {
        if (intent.extras != null && intent.extras.containsKey(EXTRA_CAMPAIGN)) {
            val campaign = (intent.extras.getSerializable(EXTRA_CAMPAIGN) as Campaign)

            val navigator = Navigator
                    .withRoot(DetailScreen(campaign))
                    .build()

            navigator.rewriteHistory(this, {
                HomeScreen()
                DetailScreen(campaign)
            })

            return navigator
        } else {
            return Navigator.withRoot(HomeScreen()).build()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.activity_toolbar)
        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.text_primary))
    }

    /*
        Handle new incoming intents (usually coming through a notification being opened)
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

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        // TODO Revisar si esto sirvió de algo
        // If the intent received a campaign, go directly to the detail view of that campaign
        if (intent.extras != null && intent.extras.containsKey(EXTRA_CAMPAIGN)) {
            getNavigator().goTo(DetailScreen(
                    (intent.extras.getSerializable(EXTRA_CAMPAIGN) as Campaign)
            ))
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
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE_LOCATION -> {
                if (grantResults.isNotEmpty()
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Location permission granted, start the location update job.
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

}