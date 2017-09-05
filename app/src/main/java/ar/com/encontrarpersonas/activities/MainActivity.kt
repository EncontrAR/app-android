package ar.com.encontrarpersonas.activities

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import ar.com.encontrarpersonas.R
import ar.com.encontrarpersonas.screens.detail.DetailScreen
import ar.com.encontrarpersonas.screens.home.HomeScreen
import ar.com.encontrarpersonas.screens.settings.SettingsScreen
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

    override fun createNavigator(): Navigator {
        return Navigator.withRoot(HomeScreen()).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.activity_toolbar)
        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.text_primary))
    }

    override fun onNavigate(actionBarConfig: ActionBarConfig) {
        // Do something with the toolbar if the screen changes
        if (actionBarConfig.visible()) {
            supportActionBar?.show()
        } else {
            supportActionBar?.hide()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        getNavigator().onCreateOptionsMenu(menu) // Important, let Magellan deal with the Menu
        return true
    }

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
}