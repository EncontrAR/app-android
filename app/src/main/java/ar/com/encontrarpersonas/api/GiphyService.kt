package ar.com.encontrarpersonas.api

import ar.com.encontrarpersonas.data.models.DataWrapper
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


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
interface GiphyService {

    companion object {
        val API_KEY = "dc6zaTOxFJmzC"
    }

    @GET("gifs/search")
    fun search(@Query("q") phrase: String,
               @Query("api_key") apiKey: String = API_KEY)
            : Call<DataWrapper>

    @GET("gifs/trending")
    fun trending(@Query("api_key") apiKey: String = API_KEY,
                 @Query("limit") limit : Int = 25)
            : Call<DataWrapper>

}