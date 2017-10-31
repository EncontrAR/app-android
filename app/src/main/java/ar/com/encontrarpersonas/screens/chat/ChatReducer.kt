package ar.com.encontrarpersonas.screens.chat

import ar.com.encontrarpersonas.data.models.ChatMessage
import com.brianegan.bansa.Action
import com.brianegan.bansa.Reducer

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
class ChatReducer {

    // Actions
    object CONNECTING : Action

    object CONNECTED : Action
    object DISCONNECTED : Action
    object ON_ERROR : Action
    data class MESSAGE_ARRIVED(val chatMessage: ChatMessage) : Action
    data class MULTIPLE_MESSAGES_ARRIVED(val chatMessages: List<ChatMessage>) : Action
    data class SET_MESSAGE_EDITOR(val messageEditor: String) : Action

    // Reducer
    val reducer = Reducer<ChatState> { oldState, action ->
        when (action) {
            is CONNECTING -> oldState.copy(isConnecting = true, isConnected = false, onError = false)
            is CONNECTED -> oldState.copy(isConnecting = false, isConnected = true, onError = false)
            is DISCONNECTED -> oldState.copy(isConnecting = false, isConnected = false, onError = false)
            is ON_ERROR -> oldState.copy(isConnecting = false, isConnected = false, onError = true)
            is MESSAGE_ARRIVED -> oldState.copy(messagesList = oldState.messagesList.plus(action.chatMessage))
            is MULTIPLE_MESSAGES_ARRIVED -> {
                val joined = ArrayList<ChatMessage>()
                joined.addAll(oldState.messagesList)
                joined.addAll(action.chatMessages)
                oldState.copy(messagesList = joined)
            }
            is SET_MESSAGE_EDITOR -> oldState.copy(messageEditor = action.messageEditor)
            else -> oldState
        }
    }
}