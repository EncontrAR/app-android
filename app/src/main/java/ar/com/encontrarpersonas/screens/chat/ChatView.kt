package ar.com.encontrarpersonas.screens.chat

import android.content.Context
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.View.OnClickListener
import android.widget.LinearLayout
import ar.com.encontrarpersonas.R
import ar.com.encontrarpersonas.screens.chat.components.*
import com.mcxiaoke.koi.ext.hideSoftKeyboard
import com.wealthfront.magellan.BaseScreenView
import trikita.anvil.Anvil
import trikita.anvil.DSL.*
import trikita.anvil.RenderableView


class ChatView(context: Context) : BaseScreenView<ChatScreen>(context) {

    init {
        addView(object : RenderableView(context) {
            override fun view() {
                setBackgroundColor(ContextCompat.getColor(context, R.color.theme_color_1))

                linearLayout {
                    size(MATCH, MATCH)
                    orientation(LinearLayout.VERTICAL)
                    margin(dip(12))

                    // Display a connecting message
                    if (screen.store.state.isConnecting) {
                        ConnectingChatComponent(context, R.string.screen_chat_state_connecting)
                    }

                    // Display an error message
                    if (screen.store.state.onError) {
                        ErrorChatComponent(context, R.string.screen_chat_state_error, {
                            // On retry, attempt to connect to the chat again
                            screen.presenter.connectChat()
                        })
                    }

                    if (screen.store.state.isConnected) {
                        // Display the messages list
                        MessagesListComponent(context, screen.store.state.messagesList)
                    }

                    if (!screen.store.state.isConnected
                            && !screen.store.state.isConnecting
                            && !screen.store.state.onError) {

                        // Display a disconnected status message
                        DisconnectedChatComponent(context, R.string.screen_chat_state_disconnected, {
                            // On retry, attempt to connect to the chat again
                            screen.presenter.connectChat()
                        })
                    }

                    // The View that the user can use to send new messages
                    InputChatMessageComponent(
                            context,
                            text = screen.store.state.messageEditor,
                            enabled = screen.store.state.isConnected,
                            onMessageChange = object : TextWatcher {
                                override fun afterTextChanged(s: Editable?) {

                                }

                                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                                }

                                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                                    screen.store.dispatch(ChatReducer.SET_MESSAGE_EDITOR(s.toString()))
                                }

                            },
                            onSendClick = OnClickListener {
                                screen.presenter.sendMessage(screen.store.state.messageEditor)
                                screen.store.dispatch(ChatReducer.SET_MESSAGE_EDITOR(""))
                                hideSoftKeyboard()
                            }
                    )
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