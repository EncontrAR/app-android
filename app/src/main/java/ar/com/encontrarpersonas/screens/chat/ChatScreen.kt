package ar.com.encontrarpersonas.screens.chat

import android.content.Context
import ar.com.encontrarpersonas.App
import ar.com.encontrarpersonas.R
import ar.com.encontrarpersonas.models.Metadata
import com.brianegan.bansa.BaseStore
import com.wealthfront.magellan.Screen
import trikita.anvil.Anvil


class ChatScreen(val item : Metadata) : Screen<ChatView>() {

    //Store
    val store = BaseStore(ChatState(item), ChatReducer().reducer)
    val presenter = ChatPresenter(store)

    // View layer
    override fun createView(context: Context): ChatView {
        store.subscribe { Anvil.render() }
        return ChatView(context)
    }

    override fun getTitle(context: Context?): String {
        return App.sInstance.getString(R.string.screen_chat_title)
    }
}
