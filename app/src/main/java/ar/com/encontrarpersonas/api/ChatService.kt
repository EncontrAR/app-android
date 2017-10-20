package ar.com.encontrarpersonas.api

import ar.com.encontrarpersonas.data.models.Chat
import ar.com.encontrarpersonas.data.models.ChatRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

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
interface ChatService {

    /**
     * Let the API know that the current user is requesting to start a conversation.
     *
     * If the chat between the current user and the backoffice hasn't been initialized for the
     * given campaign ID, then this API call will result in the chat being created and ready to use.
     *
     * This API call should always be performed before connecting to a chat to ensure that the
     * chat has been initialized server-side.
     */
    @POST("conversations")
    fun startConversation(@Body chatRequest: ChatRequest): Call<Chat>

}