package ar.com.encontrarpersonas.screens.chat

import android.content.Context
import ar.com.encontrarpersonas.App
import ar.com.encontrarpersonas.R
import ar.com.encontrarpersonas.data.models.Campaign
import com.brianegan.bansa.BaseStore
import com.wealthfront.magellan.Screen
import trikita.anvil.Anvil


class ChatScreen(val item: Campaign) : Screen<ChatView>() {

    //Store
    val store = BaseStore(ChatState(item), ChatReducer().reducer)
    val presenter = ChatPresenter(store)

    // View layer
    override fun createView(context: Context): ChatView {
        store.subscribe { Anvil.render() }
        return ChatView(context)
    }

    override fun onShow(context: Context?) {
        super.onShow(context)

        // Trigger the connection with the server
        presenter.connectChat()
    }

    override fun onHide(context: Context?) {
        super.onHide(context)

        // Disconnect from the server to free up server-side resources
        presenter.disconnectChat()
    }

    override fun getTitle(context: Context?): String {
        return App.sInstance.getString(R.string.screen_chat_title)
    }
}
