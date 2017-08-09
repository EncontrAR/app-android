package ar.com.encontrar.screens.home

import ar.com.encontrar.models.Metadata
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
    object FETCHING_GIFS : Action
    data class GIFS_ARRIVED(val gifs: List<Metadata>) : Action

    // Reducer
    val reducer = Reducer<HomeState> { state, action ->
        when (action) {
            is RESET -> HomeState()
            is FETCHING_GIFS -> state.copy(isFetching = true)
            is GIFS_ARRIVED -> state.copy(gifs = action.gifs, isFetching = false)
            else -> state
        }
    }
}