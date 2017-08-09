package ar.com.encontrar.screens.home

import ar.com.encontrar.App
import ar.com.encontrar.R
import ar.com.encontrar.api.RestApi
import ar.com.encontrar.models.DataWrapper
import com.brianegan.bansa.Store
import com.mcxiaoke.koi.ext.toast
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

    val restApi = RestApi()

    fun getGifs() {
        store.dispatch(HomeReducer.FETCHING_GIFS)

        restApi.giphy.trending(limit = 100).enqueue(object : Callback<DataWrapper> {
            override fun onResponse(call: Call<DataWrapper>, response: Response<DataWrapper>) {
                if (response.isSuccessful) {
                    store.dispatch(HomeReducer.GIFS_ARRIVED(
                            response.body()!!.data)
                    )
                } else
                    App.sInstance.toast(R.string.error_network)
            }

            override fun onFailure(call: Call<DataWrapper>?, t: Throwable?) {
                App.sInstance.toast(R.string.error_network)
            }

        })
    }
}