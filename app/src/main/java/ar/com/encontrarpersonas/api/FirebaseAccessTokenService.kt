package ar.com.encontrarpersonas.api

import android.util.Log
import ar.com.encontrarpersonas.data.UserRepository
import ar.com.encontrarpersonas.data.models.DeviceUser
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * MIT License
 *
 * Copyright (c) 2017 Wolox S.A
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
class FirebaseAccessTokenService : FirebaseInstanceIdService() {

    /**
     * On the app's first run and on some other circumstances, Firebase will generate an access
     * token for the current device. This "Firebase token" is necessary to unequivocally identify
     * the device for sending targeted push notifications.
     *
     * Firebase's access token must be send to Encontrar Rest API as soon as possible, as it is
     * necessary to retrieve the "auth token" of the API. This other token is used for every
     * authenticated endpoint of Encontrar Rest API.
     */
    override fun onTokenRefresh() {
        val firebaseAuthToken = FirebaseInstanceId.getInstance().token

        if (firebaseAuthToken != null) {
            val deviceUser = DeviceUser(os = "android", deviceId = firebaseAuthToken)

            EncontrarRestApi
                    .deviceUser
                    .registerCurrentDevice(deviceUser)
                    .enqueue(object : Callback<DeviceUser> {
                        override fun onFailure(call: Call<DeviceUser>?, t: Throwable?) {
                            Log.e("Error", "Error: Couldn't send firebase token to the server")
                        }

                        override fun onResponse(call: Call<DeviceUser>?, response: Response<DeviceUser>?) {
                            if (response?.isSuccessful!!) {
                                UserRepository.setFirebaseNotificationsToken(firebaseAuthToken)
                                UserRepository.setApiAuthToken(response.body()!!.authToken!!)
                            } else {
                                Log.e("Error", "Error: There was an error while registering the device")
                            }
                        }

                    })
        }
    }
}