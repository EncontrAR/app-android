package ar.com.encontrarpersonas.api

import ar.com.encontrarpersonas.data.models.Campaign
import ar.com.encontrarpersonas.data.models.CampaignsPage
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
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
interface CampaignsService {

    /**
     * Retrieves the list of campaigns of the Encontrar Rest API. Use the "page" and "limit"
     * parameters to modify the response.
     * By default, the endpoint returns 25 elements per page.
     *
     * Authenticated endpoint.
     */
    @GET("campaigns/index_all")
    fun getCampaignsList(@Query("page") page: Int,
                         @Query("limit") limit: Int): Call<CampaignsPage>

    /**
     * Gets a specific Campaign data from Encontrar Rest API by providing its ID.
     *
     * Authenticated endpoint.
     */
    @GET("campaigns/{campaignId}")
    fun getCampaign(@Path("campaignId") campaignId: Int): Call<Campaign>

}