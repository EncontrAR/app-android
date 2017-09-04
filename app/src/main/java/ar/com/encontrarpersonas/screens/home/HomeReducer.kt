package ar.com.encontrarpersonas.screens.home

import ar.com.encontrarpersonas.data.models.Campaign
import com.brianegan.bansa.Action
import com.brianegan.bansa.Reducer

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
class HomeReducer {

    // Actions
    object RESET : Action

    object REGISTERING_DEVICE : Action
    object FETCHING_CAMPAIGNS : Action
    object ERROR : Action
    data class CAMPAIGNS_PAGE_ARRIVED(val campaigns: List<Campaign>) : Action

    // Reducer
    val reducer = Reducer<HomeState> { oldState, action ->
        when (action) {
            is RESET -> HomeState()
            is FETCHING_CAMPAIGNS -> HomeState(isFetching = true)
            is REGISTERING_DEVICE -> HomeState(isRegisteringDevice = true)
            is ERROR -> HomeState(isOnError = true)
            is CAMPAIGNS_PAGE_ARRIVED -> {
                var combinedCampaigns = action.campaigns
                combinedCampaigns += oldState.campaigns
                oldState.copy(
                        campaigns = combinedCampaigns,
                        isFetching = false,
                        isRegisteringDevice = false,
                        isOnError = false)
            }
            else -> oldState
        }
    }
}