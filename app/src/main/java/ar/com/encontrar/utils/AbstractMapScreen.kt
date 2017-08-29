package ar.com.encontrar.utils

import android.content.Context
import com.google.android.gms.maps.MapView
import com.wealthfront.magellan.BaseScreenView
import com.wealthfront.magellan.Screen

/**
 * MIT License
 *
 * Copyright (c) 2017 Wolox S.A
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
@Suppress("FINITE_BOUNDS_VIOLATION_IN_JAVA")
abstract class AbstractMapScreen<S : Screen<*>?> : Screen<BaseScreenView<S>>() {

    var mapView : MapView? = null

    override fun onShow(context: Context?) {
        super.onShow(context)
        mapView?.onCreate(null)
    }

    override fun onHide(context: Context?) {
        super.onHide(context)
        mapView?.onDestroy()
    }

    override fun onPause(context: Context?) {
        super.onPause(context)
        mapView?.onPause()
    }

    override fun onResume(context: Context?) {
        super.onResume(context)
        mapView?.onResume()
    }

}