package ar.com.encontrar.components.search

import android.support.annotation.ColorInt
import android.support.v4.content.ContextCompat
import ar.com.encontrar.App
import ar.com.encontrar.R

enum class SearchCategory(val categoryName: String,
                          val searchTerm: String,
                          @ColorInt val categoryColor: Int) {

    HAPPY("Happy", "happy", ContextCompat.getColor(App.sInstance, R.color.black));

}