package ar.com.encontrarpersonas.screens.legal

import android.content.Context
import android.webkit.WebView
import android.webkit.WebViewClient
import com.wealthfront.magellan.BaseScreenView
import trikita.anvil.Anvil
import trikita.anvil.DSL.*
import trikita.anvil.RenderableView


class LegalView(context: Context) : BaseScreenView<LegalScreen>(context) {

    init {
        addView(object : RenderableView(context) {
            override fun view() {

                webView {
                    size(MATCH, MATCH)

                    init {
                        val webView = Anvil.currentView<WebView>()

                        webView.webViewClient = WebViewClient()
                        // The "https://www." protocol prefix is mandatory, otherwise it won't work
                        webView.loadUrl("https://www.google.com")
                    }
                }

            }
        })
    }
}