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
        @SerializedName("id") val id: Int? = null,
        @SerializedName("name") val name: String? = null,
        @SerializedName("lastname") val lastname: String? = null,
        @SerializedName("dni") val nationalId: String? = null,
        @SerializedName("age") val age: Int? = null,
        @SerializedName("photo") val photoUrl: String? = null,
        @SerializedName("created_at") val createdAt: Date? = null,
        @SerializedName("updated_at") val updatedAt: Date? = null,
        @SerializedName("gender") val gender: String? = null,
        @SerializedName("last_known_loc_lat") val lastKnownLocationLat: String? = null,
        @SerializedName("last_known_loc_long") val lastKnownLocationLong: String? = null

) : Serializable