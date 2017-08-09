package ar.com.encontrar.components.share

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.widget.LinearLayout
import ar.com.encontrar.R
import com.mcxiaoke.koi.ext.isAppInstalled
import trikita.anvil.DSL.*
import trikita.anvil.RenderableView

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
class ShareMethodComponent(context: Context,
                           val socialNetwork: SocialNetwork,
                           val urlToShare: String) : RenderableView(context) {


    init {
        view()
    }

    override fun view() {
        linearLayout {
            size(MATCH, WRAP)
            orientation(LinearLayout.HORIZONTAL)
            padding(dip(8))
            margin(0, 0, 0, dip(8))

            imageView {
                size(dip(32), dip(32))
                imageResource(socialNetwork.imageResource)
                layoutGravity(CENTER_VERTICAL)
            }

            textView {
                size(MATCH, dip(32))
                padding(dip(16), 0)
                text(socialNetwork.networkName)
                textSize(sip(18f))
                gravity(Gravity.CENTER_VERTICAL)
                textColor(ContextCompat.getColor(context, R.color.white))
                typeface(Typeface.createFromAsset(
                        context.assets,
                        "fonts/Asap-Regular.ttf"))
            }

            onClick {
                share(socialNetwork.packageName)
            }

        }
    }

    private fun share(appPackage: String? = null) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                context.getString(R.string.component_sharemethod_app_message) + " " + urlToShare)
        shareIntent.type = "text/plain"
        if (appPackage != null && context.isAppInstalled(appPackage))
            shareIntent.`package` = appPackage
        context.startActivity(shareIntent)
    }
}