package ar.com.encontrarpersonas.screens.chat

import ar.com.encontrarpersonas.models.ChatMessage
import ar.com.encontrarpersonas.models.Metadata

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
data class ChatState(
        val item: Metadata,
        val messagesList: List<ChatMessage> = listOf(ChatMessage(
                "999", "Hola!"),
                ChatMessage("999", "Bienvenido a Encontrar"),
                ChatMessage("999","Este es un mensaje de prueba. En el futuro, podrás reportar por este medio " +
                        "tus hallazgos. Cualquier detalle puede ser importante para recuperar a alguien."),
                ChatMessage("123", "Ok"),
                ChatMessage("123", "Entonces esta es una respuesta de prueba ;)")
        )
)