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
data class DeviceUser(
        @SerializedName("id") val id: Int? = null,
        @SerializedName("email") val email: String? = null,
        @SerializedName("name") val name: String? = null,
        @SerializedName("lastName") val lastname: String? = null,
        @SerializedName("dni") val nationalId: String? = null,
        @SerializedName("device_id") val deviceId: String? = null,
        @SerializedName("os") val os: String? = null,
        @SerializedName("alert_type") val alertType: Int? = null,
        @SerializedName("latitude") val latitude: String? = null,
        @SerializedName("longitude") val longitude: String? = null,
        @SerializedName("created_at") val createdAt: Date? = null,
        @SerializedName("updated_at") val updatedAt: Date? = null,
        @SerializedName("auth_token") val authToken: String? = null
) : Serializable

