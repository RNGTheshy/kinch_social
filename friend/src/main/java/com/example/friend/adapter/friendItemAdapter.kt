package com.example.friend.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import com.chaoshan.data_center.friend.Friend
import com.example.friend.R


class friendItemAdapter(var datas: List<Friend?>) :
    RecyclerView.Adapter<friendItemAdapter.ViewHolder>() {
    private var mContext: Context? = null

    fun setData(datas: List<Friend?>) {
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
//        //为viewholder绑定数据
//        val friend = datas[position]
//
//        //设置头像
//        holder.headView = friend.headView!!
//
//        //设置名字
//        holder.name.text = friend.name
//
//        //设置定位
//        holder.location.text = friend.location
//
//        //设置状态
//        holder.state = friend.state!!

//        if (mlistener != null) {
//            holder.itemView.setOnClickListener {
//                val pos = holder.layoutPosition
//                mlistener!!.onItemClick(holder.itemView, pos)
//            }
//            holder.itemView.setOnLongClickListener {
//                val pos = holder.layoutPosition
//                mlistener!!.onItemLongClick(holder.itemView, pos)
//                true
//            }
//        }


    }

    override fun getItemCount() = datas.size

}