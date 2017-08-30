package ar.com.encontrarpersonas.screens.chat

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import ar.com.encontrarpersonas.R
import ar.com.encontrarpersonas.screens.chat.components.InputChatMessageComponent
import com.wealthfront.magellan.BaseScreenView
import trikita.anvil.Anvil
import trikita.anvil.DSL.*
import trikita.anvil.RenderableView
import trikita.anvil.recyclerview.Recycler


class ChatView(context: Context) : BaseScreenView<ChatScreen>(context) {

    init {
        addView(object : RenderableView(context) {
            override fun view() {
                setBackgroundColor(ContextCompat.getColor(context, R.color.theme_color_1))

                linearLayout {
                    size(MATCH, MATCH)
                    orientation(LinearLayout.VERTICAL)
                    margin(dip(12))

                    // Chat messages recycler view
                    v(RecyclerView::class.java) {
                        val recycler = Anvil.currentView<RecyclerView>()
                        size(MATCH, dip(0))
                        weight(1f)
                        padding(dip(0), dip(0), dip(0), dip(8))

                        // Layout manager
                        recycler.layoutManager =
                                LinearLayoutManager(
                                        context,
                                        LinearLayoutManager.VERTICAL,
                                        false)

                        // Fixed height for improved performance
                        recycler.setHasFixedSize(true)

                        // Simple items list adapter
                        recycler.adapter =
                                Recycler.Adapter.simple(screen.store.state.messagesList) {
                                    viewHolder ->

                                    val chatMessage = screen.store.state.messagesList[viewHolder.adapterPosition]

                                    linearLayout {
                                        size(WRAP, WRAP)
                                        orientation(LinearLayout.VERTICAL)

                                        // Separator after first message
                                        if (viewHolder.adapterPosition > 0) {
                                            view {
                                                size(WRAP, dip(16))
                                            }
                                        }

                                        // TODO Modify the logic to detect local messages
                                        if (chatMessage.userId != "123") {
                                            // Remote message view
                                            textView {
                                                size(WRAP, WRAP)
                                                layoutGravity(LEFT)
                                                text(chatMessage.message)
                                                textColor(ContextCompat.getColor(context, R.color.text_secondary))
                                                textSize(sip(16f))
                                                padding(dip(16), dip(20))
                                                background(ContextCompat.getDrawable(context, R.drawable.patch_chat_received))
                                            }
                                        } else {
                                            // Local message view
                                            textView {
                                                size(WRAP, WRAP)
                                                layoutGravity(RIGHT)
                                                text(chatMessage.message)
                                                textColor(ContextCompat.getColor(context, R.color.text_primary))
                                                textSize(sip(16f))
                                                padding(dip(16), dip(20))
                                                background(ContextCompat.getDrawable(context, R.drawable.patch_chat_sent))
                                            }
                                        }

                                    }
                                }

                    }

                    // The View that the user can use to send new messages
                    InputChatMessageComponent(context)
                }

            }

            // This fixes an issue with Anvil and RecyclerViews
            // https://github.com/zserge/anvil/issues/95
            override fun onDetachedFromWindow() {
                Anvil.unmount(this, false)
            }

        })
    }
}