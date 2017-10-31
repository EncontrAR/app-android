package ar.com.encontrarpersonas.api

import ar.com.encontrarpersonas.data.models.Campaign
import ar.com.encontrarpersonas.data.models.DeviceUser
import ar.com.encontrarpersonas.data.models.Position
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

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
interface DeviceUserService {

    /**
     * Registers the current device on Encontrar Rest API.
     *
     * Non authenticated endpoint.
     *
     * This endpoint should be called as soon as possible, since it will retrieve the
     * API auth token required by the other endpoints.
     */
    @POST("finders")
    fun registerCurrentDevice(@Body deviceUser: DeviceUser): Call<DeviceUser>

    /**
     * Returns the DeviceInfo model of the current device registered in Encontrar Rest API.
     *
     * Authenticated endpoint.
     */
    @GET("finders/me")
    fun getLoggedDevice(): Call<DeviceUser>


    /**
     * Edits the messages of the current logged device on Encontrar Rest API.
     *
     * Authenticated endpoint.
     */
    @PUT("finders/edit")
    fun editLoggedDevice(@Body deviceUser: DeviceUser): Call<Void>

    /**
     * Sends an update of the position of the current logged device to Encontrar Rest API.
     * Returns a list of active campaigns on the new location of the device.
     *
     * Authenticated endpoint.
     */
    @PUT("finders/update_pos")
    fun updateLoggedDevicePosition(@Body position: Position): Call<List<Campaign>>

}