package ar.com.encontrarpersonas.screens.detail

import android.content.Intent
import ar.com.encontrarpersonas.R

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
class DetailPresenter(val screen: DetailScreen) {

    fun shareCampaign() {
        val shareIntent = Intent()
        val campaign = screen.store.state.item

        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, screen.activity.getString(
                R.string.share_title,
                campaign.missingPerson?.name
        ))
        shareIntent.putExtra(Intent.EXTRA_TEXT, screen.activity.getString(
                R.string.share_description,
                "${campaign.missingPerson?.name} ${campaign.missingPerson?.lastname}"
        ))
        screen.activity.startActivity(shareIntent)
    }

}