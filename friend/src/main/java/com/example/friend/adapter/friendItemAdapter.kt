package com.example.friend.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Outline
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.chaoshan.data_center.togetname.Headport
import com.chaoshan.data_center.friend.Friend
import com.chaoshan.data_center.togetname.center_getname
import com.chaoshan.data_center.togetname.getPersonal_data
import com.example.friend.R


class friendItemAdapter(var datas: List<Friend>) :
    RecyclerView.Adapter<friendItemAdapter.ViewHolder>() {
    private var mContext: Context? = null

    fun setData(datas: List<Friend>) {
        this.datas = datas
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var headView: ImageView = view.findViewById(R.id.headView) as ImageView
        val name: TextView = view.findViewById(R.id.name) as TextView
        val location: TextView = view.findViewById(R.id.location) as TextView
        var state: ImageView = view.findViewById(R.id.state) as ImageView
    }

    //事件监听的回调接口
    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int)
        fun onItemLongClick(view: View?, position: Int)
    }

    private var mlistener: OnItemClickListener? = null
    fun setOnItemClickListener(mlistener: OnItemClickListener?) {
        this.mlistener = mlistener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        //加载布局创建viewholder
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_friend, parent, false)
        mContext = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //为viewholder绑定数据
        val friend = datas[position]

        //设置头像
        val headport = Headport()
        friend.id?.let { headport.setImage(it, holder.headView) }

        setRadius(holder.headView, 20F)
        //设置名字
        getPersonal_data.center_getname(friend.id, object : center_getname {
            override fun getname(name: String?) {
                holder.name.text = name
            }
        })

        //设置定位
        holder.location.text = friend.location

        //设置状态
        when (friend.state) {
            "正在睡觉" -> {
                holder.state.setImageResource(R.mipmap.state_sleep)
            }
        }

        if (mlistener != null) {
            holder.itemView.setOnClickListener {
                val pos = holder.layoutPosition
                mlistener!!.onItemClick(holder.itemView, pos)
            }
            holder.itemView.setOnLongClickListener {
                val pos = holder.layoutPosition
                mlistener!!.onItemLongClick(holder.itemView, pos)
                true
            }
        }
    }

    /**
     * 用于设置ImageView的圆角
     * @param radius: 图片圆角 px
     */
    private fun setRadius(view: View, radius: Float) {
        view.clipToOutline = true
        view.outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                outline.setRoundRect(0, 0, view.width, view.height, radius)
            }
        }
        //设置阴影
        view.elevation = 20F;
    }

    override fun getItemCount() = datas.size

}