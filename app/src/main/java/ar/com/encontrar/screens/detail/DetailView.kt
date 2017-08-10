package ar.com.encontrar.screens.detail

import android.content.Context
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.widget.ImageView
import android.widget.LinearLayout
import ar.com.encontrar.R
import ar.com.encontrar.components.lists.RemoteImageComponent
import ar.com.encontrar.components.share.ShareMethodComponent
import ar.com.encontrar.components.share.SocialNetwork
import com.facebook.drawee.drawable.ScalingUtils
import com.mcxiaoke.koi.ext.isAppInstalled
import com.wealthfront.magellan.BaseScreenView
import trikita.anvil.Anvil
import trikita.anvil.DSL.*
import trikita.anvil.RenderableView


class DetailView(context: Context) : BaseScreenView<DetailScreen>(context) {

    init {
        addView(object : RenderableView(context) {
            override fun view() {

                backgroundColor(ContextCompat.getColor(context, R.color.black))

                linearLayout {
                    size(MATCH, MATCH)
                    orientation(LinearLayout.VERTICAL)

                    frameLayout {
                        size(MATCH, 0)
                        weight(0.6f) // 60% of the screen height

                        RemoteImageComponent(
                                context,
                                MATCH,
                                MATCH,
                                screen.store.state.gif.images.original.webp,
                                ScalingUtils.ScaleType.FIT_CENTER)
                    }

                    v(CardView::class.java) {
                        size(MATCH, 0)
                        weight(0.4f) // 40% of the screen height
                        margin(dip(16), dip(4), dip(16), dip(64))

                        init {
                            Anvil.currentView<CardView>().radius = 16f
                        }

                        imageView {
                            size(MATCH, MATCH)
                            imageResource(R.drawable.drawable_primary_gradient)
                            scaleType(ImageView.ScaleType.FIT_XY)
                        }

                        scrollView {
                            linearLayout {
                                size(MATCH, MATCH)
                                orientation(LinearLayout.VERTICAL)
                                padding(dip(20), dip(20), dip(8), dip(20))

                                textView {
                                    size(MATCH, WRAP)
                                    textSize(sip(24f))
                                    text(R.string.screen_detail_share)
                                    margin(0, 0, 0, dip(24))
                                    textColor(ContextCompat.getColor(context, R.color.white))
                                    typeface(Typeface.createFromAsset(
                                            context.assets,
                                            "fonts/Pacifico-Regular.ttf"))
                                }

                                SocialNetwork.values()
                                        .filter {
                                            it.packageName == null ||
                                                    context.isAppInstalled(it.packageName)
                                        }
                                        .forEach {
                                            ShareMethodComponent(
                                                    context,
                                                    it,
                                                    screen.store.state.gif.url
                                            )
                                        }

                            }
                        }
                    }
                }
            }
        })
    }
}