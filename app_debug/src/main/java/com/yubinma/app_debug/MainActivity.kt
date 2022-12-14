package com.yubinma.app_debug

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chaoshan.data_center.SettingsPreferencesDataStore
import com.chaoshan.data_center.activitymanger.ActivityManager
import com.chaoshan.login.LoginActivity
import com.chaoshan.login.LoginComeActivity
import com.example.chat.ChatActivity
import com.example.chat.InforActivity
import com.chaoshan.socialforum.activity.SocialForumActivity
import com.example.friend.friendMainActivity
import com.example.kinch_home.Home_Activity
import com.example.kinch_home.WelcomeActivity
import com.example.setting.SettingMainActivity
import com.yubinma.app_debug.databinding.ActivityMainBinding
import com.yubinma.person_center.PersonCenter2Activity

class MainActivity : AppCompatActivity(), ActivityManager.IRecordPage {
    private lateinit var binding: ActivityMainBinding
    private var adapter = DebugAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        openKinchHome()
//        finish()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        val recyclerView = binding.debugMenu
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.adapter = adapter
        adapter.submitList(debugItem)
    }


    /**
     * 配置 debug 菜单
     */
    class DebugItem(val title: String, val invoke: () -> Unit)

    /**
     * 配置 debug 菜单内容.
     */
    private val debugItem = listOf(
        DebugItem("login_come Activty") {
            openLogin()
        },
        DebugItem("login Activty") {
            openComeLogin()
        },
        DebugItem("Kinch_home") {
            openKinchHome()
        },
        DebugItem("Infor") {
            openInfor()
        },
        DebugItem("Chat") {
            openChat()
        },
        DebugItem("朋友圈") {
            openSoci()
        },
        DebugItem("personActivity") {
            openPersonCenter()
        },
        DebugItem("openSetting") {
            openSetting()
        },
        DebugItem("sss") {
            openLogin2()
        }
        , DebugItem("friend") {
            openFirend();
        }
        , DebugItem("welcome") {
            openWelcome();
        }


    )
    private fun openWelcome() {
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
    }
    private fun openSetting() {
        val intent = Intent(this, SettingMainActivity::class.java)
        startActivity(intent)
    }

    private fun openFirend() {
        val intent = Intent(this, friendMainActivity::class.java)
        startActivity(intent)
    }

    private fun openPersonCenter() {
        val intent = Intent(this, PersonCenter2Activity::class.java)
        startActivity(intent)
    }

    private fun openChat() {
        val intent = Intent(this, ChatActivity::class.java)
        startActivity(intent)
    }

    private fun openInfor() {
        val intent = Intent(this, InforActivity::class.java)
        startActivity(intent)
    }

    private fun openSoci() {
        val intent = Intent(this, SocialForumActivity::class.java)
        startActivity(intent)

    }


    private fun openKinchHome() {
        val intent: Intent = if (SettingsPreferencesDataStore.getCurrentUserObjetID() == "NULL") {
            Intent(this, LoginActivity::class.java)
        } else {
            Intent(this, Home_Activity::class.java)
        }
        startActivity(intent)
    }


    private fun openLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun openLogin2() {
        val intent = Intent(this, friendMainActivity::class.java)
        startActivity(intent)
    }
//    friendMainActivity

    private fun openComeLogin() {
        val intent = Intent(this, LoginComeActivity::class.java)
        startActivity(intent)
    }


    class DebugVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val btnAction = itemView.findViewById<Button>(R.id.btn_action)
        fun bind(item: DebugItem) {
            btnAction.text = item.title
            btnAction.setOnClickListener { item.invoke() }
        }
    }

    class DebugAdapter : RecyclerView.Adapter<DebugVH>() {

        private var data = emptyList<DebugItem>()

        @SuppressLint("NotifyDataSetChanged")
        fun submitList(data: List<DebugItem>) {
            this.data = data
            this.notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DebugVH {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_debug, parent, false)
            return DebugVH(view)
        }

        override fun onBindViewHolder(holder: DebugVH, position: Int) {
            val item = data[position]
            holder.bind(item)
        }

        override fun getItemCount(): Int {
            return data.size
        }
    }
}