package ar.com.encontrarpersonas.screens.home

import android.text.TextUtils
import android.util.Log
import ar.com.encontrarpersonas.api.EncontrarRestApi
import ar.com.encontrarpersonas.data.UserRepository
import ar.com.encontrarpersonas.data.models.CampaignsPage
import com.brianegan.bansa.Store
import com.mcxiaoke.koi.async.asyncDelay
import com.mcxiaoke.koi.async.mainThread
import com.mcxiaoke.koi.async.mainThreadSafe
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
class HomePresenter(val store: Store<HomeState>) {

    private val REGISTERING_DEVICE_MAX_ATTEMPTS = 10

    fun startCampaignsRetrievalProcess() {

        if (TextUtils.isEmpty(UserRepository.getApiAuthToken())) {
            waitForDeviceRegistered()
        } else {
            fetchCampaigns()
        }
    }

    private fun waitForDeviceRegistered(timesChecked: Int = 1) {

        Log.i("Registering",
                "Registering device. Attempt $timesChecked / $REGISTERING_DEVICE_MAX_ATTEMPTS")

        if (timesChecked == 1)
            store.dispatch(HomeReducer.REGISTERING_DEVICE)

        // Check if we have already checked too many times, if so, abort and go to an error state
        if (timesChecked >= REGISTERING_DEVICE_MAX_ATTEMPTS) {
            mainThread {
                store.dispatch(HomeReducer.ERROR)
                return@mainThread
            }
        }

        // Wait and check if the device has been registered with the API. This must be done at
        // least once by FirebaseAccessTokenService.
        asyncDelay((5000 * timesChecked).toLong()) {
            if (TextUtils.isEmpty(UserRepository.getApiAuthToken())) {
                waitForDeviceRegistered(timesChecked + 1)
            } else {
                mainThreadSafe {
                    fetchCampaigns()
                }
            }
        }
    }

    private fun fetchCampaigns() {
        store.dispatch(HomeReducer.FETCHING_CAMPAIGNS)

        EncontrarRestApi
                .campaigns
                .getCampaignsList()
                .enqueue(object : Callback<CampaignsPage> {
                    override fun onFailure(call: Call<CampaignsPage>?, t: Throwable?) {
                        store.dispatch(HomeReducer.NO_INTERNET)
                    }

                    override fun onResponse(call: Call<CampaignsPage>?,
                                            response: Response<CampaignsPage>?) {
                        if (response?.isSuccessful!!) {

                            if (response.body()?.campaigns != null) {
                                store.dispatch(HomeReducer.CAMPAIGNS_PAGE_ARRIVED(
                                        response.body()!!.campaigns!!))
                            } else {
                                store.dispatch(HomeReducer.ERROR)
                            }

                        } else {

                            // Check if the current access token has been unauthorized by the API
                            if (response.code() == 401) {
                                UserRepository.setApiAuthToken("") // Clean dirty token
                                startCampaignsRetrievalProcess() // Restart campaigns retrieval
                            } else {
                                // If the code reached this point, something unusual happened
                                store.dispatch(HomeReducer.ERROR)
                            }

                        }
                    }
                })
    }
}