package com.example.chat


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.leancloud.chatkit.LCChatKit
import cn.leancloud.chatkit.activity.LCIMConversationActivity
import cn.leancloud.chatkit.activity.LCIMUserSelectActivity
import cn.leancloud.chatkit.utils.LCIMConstants
import cn.leancloud.im.LCIMOptions
import cn.leancloud.im.v2.LCIMClient
import cn.leancloud.im.v2.LCIMException
import cn.leancloud.im.v2.callback.LCIMClientCallback
import kotlinx.android.synthetic.main.chat_room.*

class ChatActivity : AppCompatActivity() {

//    private val msgList = ArrayList<Msg>()
//    private var adapter: MsgAdapter? = null
//    private val appService = ServiceCreator.create<ChatService>()
        companion object {
            public  fun goToChat(context:Context,usename:String,password:String){
                val intent = Intent(context,ChatActivity::class.java)
                intent.putExtra("usename",usename);
                intent.putExtra("password",password);
                context.startActivity(intent)
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat_room)
        LCIMOptions.getGlobalOptions().setAutoOpen(true)
            val userName =  intent.getStringExtra("usename")
            val password = intent.getStringExtra("password")
            if (TextUtils.isEmpty(userName)||TextUtils.isEmpty(password)){
                finish()
            }
            LCChatKit.getInstance().open(userName,password,null,object : LCIMClientCallback() {
                override fun done(client: LCIMClient, e: LCIMException?) {
                    if (e == null) {
                        val intent = Intent(this@ChatActivity,LCIMConversationActivity::class.java)
                        intent.putExtra(LCIMConstants.PEER_ID, "626684ceadc5786698ac1f09")
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@ChatActivity,e.toString(),Toast.LENGTH_SHORT).show()
                    }
                }
            })
//        send.setOnClickListener{
//            LCIMOptions.getGlobalOptions().setAutoOpen(true)
//            val userName = "147"
//            val password = "236"
//            LCChatKit.getInstance().open(userName,password,null,object : LCIMClientCallback() {
//                override fun done(client: LCIMClient, e: LCIMException?) {
//                    if (e == null) {
////                        val intent = Intent(this@ChatActivity,LCIMConversationActivity::class.java)
////                        intent.putExtra(LCIMConstants.PEER_ID, id)
////                        startActivity(intent)
//                        startActivity(Intent(this@ChatActivity,LCIMUserSelectActivity::class.java))
//                    } else {
//                        Toast.makeText(this@ChatActivity,e.toString(),Toast.LENGTH_SHORT).show()
//                    }
//                }
//            })
//        }





//        initMsg()
//        val layoutManager = LinearLayoutManager(this)
//
//        chatRecycler.layoutManager = layoutManager
//        adapter = MsgAdapter(msgList)
//        chatRecycler.adapter = adapter
//        send.setOnClickListener {
//            when (it) {
//                send -> {
//                    val content = inputText.text.toString()
//                    if (content.isNotEmpty()) {
//                        val msg = Msg(content, Msg.TYPE_SENT)
//                        msgList.add(msg)
//                        adapter?.notifyItemInserted(msgList.size - 1) // 当有新消息时，刷新RecyclerView中的显示
//                        chatRecycler.scrollToPosition(msgList.size - 1)  // 将RecyclerView定位到最后一行
//                        inputText.setText("") // 清空输入框中的内容
//
//
//                        appService.getMsg(content).enqueue(object : Callback<ReturnMsg> {
//                            override fun onResponse(call: Call<ReturnMsg>, response: Response<ReturnMsg>) {
//                                val msg = response.body()
//                                msg?.let {
//                                    msgList.add(Msg(it.content,Msg.TYPE_RECEIVED))
//                                    adapter?.notifyItemInserted(msgList.size - 1) // 当有新消息时，刷新RecyclerView中的显示
//                                    chatRecycler.scrollToPosition(msgList.size - 1)  // 将RecyclerView定位到最后一行
//                                }
//                            }
//
//                            override fun onFailure(call: Call<ReturnMsg>, t: Throwable) {
//                                t.printStackTrace()
//                            }
//                        })
//                    }
//                }
//            }
//        }

    }



//    private fun initMsg() {
//        val msg1 = Msg("Hello guy.", Msg.TYPE_RECEIVED)
//        msgList.add(msg1)
//        val msg2 = Msg("Hello. Who is that?", Msg.TYPE_SENT)
//        msgList.add(msg2)
//        val msg3 = Msg("This is Tom. Nice talking to you. ", Msg.TYPE_RECEIVED)
//        msgList.add(msg3)
//    }

}
