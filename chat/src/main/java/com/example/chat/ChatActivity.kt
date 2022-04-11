package com.example.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager


import kotlinx.android.synthetic.main.chat_room.*

class ChatActivity : AppCompatActivity() {

    private val msgList = ArrayList<Msg>()
    private var adapter: MsgAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat_room)
        initMsg()
        val layoutManager = LinearLayoutManager(this)

        chatRecycler.layoutManager = layoutManager
        adapter = MsgAdapter(msgList)
        chatRecycler.adapter = adapter
        send.setOnClickListener {
            when (it) {
                send -> {
                    val content = inputText.text.toString()
                    if (content.isNotEmpty()) {
                        val msg = Msg(content, Msg.TYPE_SENT)
                        msgList.add(msg)
                        adapter?.notifyItemInserted(msgList.size - 1) // 当有新消息时，刷新RecyclerView中的显示
                        chatRecycler.scrollToPosition(msgList.size - 1)  // 将RecyclerView定位到最后一行
                        inputText.setText("") // 清空输入框中的内容
                    }
                }
            }
        }

    }

    private fun initMsg() {
        val msg1 = Msg("Hello guy.", Msg.TYPE_RECEIVED)
        msgList.add(msg1)
        val msg2 = Msg("Hello. Who is that?", Msg.TYPE_SENT)
        msgList.add(msg2)
        val msg3 = Msg("This is Tom. Nice talking to you. ", Msg.TYPE_RECEIVED)
        msgList.add(msg3)
    }

}