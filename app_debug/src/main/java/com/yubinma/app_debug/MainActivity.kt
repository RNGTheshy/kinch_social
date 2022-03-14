package com.yubinma.app_debug

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chaoshan.login.LoginActivity
import com.chaoshan.login.LoginComeActivity
import com.nino.blindbox.ui.activity.HomeActivity
import com.nino.blindbox.ui.activity.WelcomeActivity
import com.yubinma.app_debug.databinding.ActivityMainBinding
import com.yubinma.fishprawncrab.FishPrawnCrabMainActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var adapter = DebugAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        DebugItem("mainActivity") {
            openFishPrawnCrabMainActivity()
        },
        DebugItem("BlindBox Show") {
            openBlindBoxActivity()
        },
        DebugItem("home Activty") {
            openBlindBoxHomeActivity()
        },
        DebugItem("login_come Activty") {
            openLogin()
        },
        DebugItem("login Activty") {
            openComeLogin()
        }
    )

    private fun openFishPrawnCrabMainActivity() {
        val intent = Intent(this, FishPrawnCrabMainActivity::class.java)
        startActivity(intent)
    }

    private fun openLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun openComeLogin() {
        val intent = Intent(this, LoginComeActivity::class.java)
        startActivity(intent)
    }

    private fun openBlindBoxActivity() {
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
    }

    private fun openBlindBoxHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
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