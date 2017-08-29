package ar.com.encontrarpersonas.components.share

import android.support.annotation.IntegerRes
import ar.com.encontrarpersonas.App
import ar.com.encontrarpersonas.R

enum class SocialNetwork(val networkName: String,
                         @IntegerRes val imageResource: Int,
                         val packageName: String?) {

    WHATSAPP("Whatsapp", R.drawable.ic_whatsapp, "com.whatsapp"),
    FACEBOOK("Facebook", R.drawable.ic_facebook, "com.facebook.katana"),
    TWITTER("Twitter", R.drawable.ic_twitter, "com.twitter.android"),
    INSTAGRAM("Instagram", R.drawable.ic_twitter, "com.instagram.android"),
    TELEGRAM("Telegram", R.drawable.ic_telegram, "org.telegram.messenger"),
    OTHER(App.sInstance.resources.getString(R.string.component_sharemethod_other),
            R.drawable.ic_share, null)

}