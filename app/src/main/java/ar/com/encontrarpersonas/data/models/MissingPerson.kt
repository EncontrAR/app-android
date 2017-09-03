package ar.com.encontrarpersonas.data.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

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
data class MissingPerson(
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String,
        @SerializedName("lastname") val lastname: String,
        @SerializedName("dni") val nationalId: String,
        @SerializedName("age") val age: Int,
        @SerializedName("photo") val photoUrl: String,
        @SerializedName("created_at") val createdAt: Date,
        @SerializedName("updated_at") val updatedAt: Date,
        @SerializedName("gender") val gender: String,
        @SerializedName("last_known_loc_lat") val lastKnownLocationLat: String,
        @SerializedName("last_known_loc_long") val lastKnownLocationLong: String

) : Serializable