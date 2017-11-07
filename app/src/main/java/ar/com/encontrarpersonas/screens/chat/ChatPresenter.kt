package ar.com.encontrarpersonas.screens.chat

import ar.com.encontrarpersonas.api.EncontrarRestApi
import ar.com.encontrarpersonas.data.UserRepository
import ar.com.encontrarpersonas.data.models.*
import com.brianegan.bansa.Store
import com.crashlytics.android.Crashlytics
import com.google.gson.Gson
import com.hosopy.actioncable.ActionCable
import com.hosopy.actioncable.Channel
import com.hosopy.actioncable.Consumer
import com.hosopy.actioncable.Subscription
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URI

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
class ChatPresenter(val store: Store<ChatState>) {

    companion object {
        // This is the maximum length that will be allowed for each new message sent by the finder
        val MESSAGE_MAX_LENGTH = 140
    }

    // Constants
    private val SERVER_URL = "ws://encontrar-stage.herokuapp.com/cable/"
    private val ACTION_GET_HISTORY = "historial"
    private val ACTION_NEW_MESSAGE = "create"
    private val CHAT_CHANNEL = "ChatChannel"
    private val CHANNEL_PARAM_TOKEN = "finder_token"
    private val CHANNEL_PARAM_CONVERSATION_ID = "conversation_id"
    private val PAYLOAD_TYPE = "type"
    private val PAYLOAD_TYPE_HISTORIAL = "historial"
    private val PAYLOAD_TYPE_NEW_MESSAGE = "new_message"

    private val chatUri by lazy { URI(SERVER_URL) }
    private val consumer by lazy {
        val options = Consumer.Options()
        options.reconnection = true
        options.reconnectionMaxAttempts = 10

        ActionCable.createConsumer(chatUri, options)!!
    }

    private var subscription: Subscription? = null

    private fun getChatChannel(chatId: Int): Channel {
        val chatChannel = Channel(CHAT_CHANNEL)
        chatChannel.addParam(CHANNEL_PARAM_TOKEN, UserRepository.getApiAuthToken())
        chatChannel.addParam(CHANNEL_PARAM_CONVERSATION_ID, chatId)
        return chatChannel
    }

    private fun getSubscription(channel: Channel): Subscription {
        return consumer.subscriptions.create(channel)
    }

    /**
     * Request to open a chat session to the server and connectChat to it.
     *
     * IMPORTANT: Make sure to call this method before attempting to send new messages or any other
     * communication operation.
     *
     * IMPORTANT: Call ChatPresenter#disconnectChat() to free up resources on the server when done using
     * the chat channel.
     */
    fun connectChat() {
        store.dispatch(ChatReducer.CONNECTING)

        EncontrarRestApi
                .chat
                .startConversation(ChatRequest(store.state.campaign.id!!))
                .enqueue(object : Callback<Chat> {
                    override fun onResponse(call: Call<Chat>?, response: Response<Chat>?) {
                        if (response != null && response.isSuccessful) {
                            startSubscription(response.body()!!.id)
                        } else {
                            store.dispatch(ChatReducer.ON_ERROR)
                        }
                    }

                    override fun onFailure(call: Call<Chat>?, t: Throwable?) {
                        store.dispatch(ChatReducer.ON_ERROR)
                    }
                })
    }

    /**
     * Open a socket and connectChat to the chat channel of the current campaign
     */
    private fun startSubscription(campaignId: Int) {
        subscription = getSubscription(getChatChannel(campaignId))
                .onConnected {
                    store.dispatch(ChatReducer.CONNECTED)
                    requestOlderMessages() // Ask the server to send previous messages
                }
                .onRejected {
                    store.dispatch(ChatReducer.ON_ERROR)
                    Crashlytics.log("The server rejected the connection while trying to " +
                            "subscribe for the chat channel")
                }
                .onReceived { jsonElement ->

                    if (jsonElement.asJsonObject.has(PAYLOAD_TYPE)) {
                        if (jsonElement.asJsonObject.get(PAYLOAD_TYPE).asString == PAYLOAD_TYPE_NEW_MESSAGE) {
                            val receivedPayload = Gson().fromJson(jsonElement, ChatPayloadMessage::class.java)
                            store.dispatch(ChatReducer.MESSAGE_ARRIVED(receivedPayload.chatMessage))
                        } else if (jsonElement.asJsonObject.get(PAYLOAD_TYPE).asString == PAYLOAD_TYPE_HISTORIAL) {
                            val receivedPayload = Gson().fromJson(jsonElement, ChatPayloadHistorial::class.java)
                            store.dispatch(ChatReducer.MULTIPLE_MESSAGES_ARRIVED(receivedPayload.messages))
                        }
                    }

                }
                .onDisconnected {
                    store.dispatch(ChatReducer.DISCONNECTED)
                }
                .onFailed {
                    store.dispatch(ChatReducer.ON_ERROR)
                    Crashlytics.log("An error occurred while trying to subscribe to the chat")
                }

        consumer.connect()
    }

    /**
     * Disconnect the socket and free up resources on the server
     */
    fun disconnectChat() {
        consumer.disconnect()
        subscription = null
    }

    /**
     * Request the server to send older messages
     */
    fun requestOlderMessages() {
        if (subscription != null) {
            subscription?.perform(ACTION_GET_HISTORY)
        } else {
            store.dispatch(ChatReducer.ON_ERROR)
            Crashlytics.log("Can't request messages on a non-initialized subscription. " +
                    "Must call ChatPresenter#connectChat() before.")
        }
    }

    /**
     * Send a new message to the server
     */
    fun sendMessage(message: String) {
        if (subscription != null) {
            if (message.isNotEmpty()) {
                subscription?.perform(
                        ACTION_NEW_MESSAGE,
                        Gson().toJsonTree(
                                (ChatMessage(message = trimMessageToMaxAllowedSize(message)))
                        ).asJsonObject
                )
            }
        } else {
            store.dispatch(ChatReducer.ON_ERROR)
            Crashlytics.log("Can't send new messages on a non-initialized subscription. " +
                    "Must call ChatPresenter#connectChat() before.")
        }
    }

    /**
     * Trims the message to be sent to the maximum allowed length for each message
     */
    private fun trimMessageToMaxAllowedSize(message: String): String {
        val messageWithoutSpaces = message.trim()
        return messageWithoutSpaces.substring(
                0,
                Math.min(messageWithoutSpaces.length, MESSAGE_MAX_LENGTH)
        )
    }

}