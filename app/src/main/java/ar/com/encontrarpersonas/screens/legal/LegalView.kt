package ar.com.encontrarpersonas.screens.legal

import android.content.Context
import android.support.v4.content.ContextCompat
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import ar.com.encontrarpersonas.R
import com.wealthfront.magellan.BaseScreenView
import trikita.anvil.Anvil
import trikita.anvil.DSL.*
import trikita.anvil.RenderableView


class LegalView(context: Context) : BaseScreenView<LegalScreen>(context) {

    init {
        addView(object : RenderableView(context) {
            override fun view() {

                linearLayout {
                    size(MATCH, MATCH)
                    orientation(LinearLayout.VERTICAL)

                    webView {
                        size(MATCH, 0)
                        weight(1f)

                        init {
                            val webView = Anvil.currentView<WebView>()

                            webView.webViewClient = WebViewClient()
                            // The "https://www." protocol prefix is mandatory, otherwise it won't work
                            webView.loadUrl("https://www.google.com")
                        }
                    }

                    linearLayout {
                        size(MATCH, WRAP)
                        orientation(LinearLayout.HORIZONTAL)
                        gravity(RIGHT)
                        backgroundColor(ContextCompat.getColor(context, R.color.primary_background))
                        padding(dip(16), dip(8))

                        button{
                            size(WRAP, WRAP)
                            text(R.string.screen_legal_button_accept)
                        }
                    }
                }

            }
        })
    }
}