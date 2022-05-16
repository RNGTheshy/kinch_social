package com.example.friend.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.chaoshan.data_center.friend
import com.example.friend.R


class friendItemAdapter(var datas: LinkedList<friend>) :
    RecyclerView.Adapter<friendItemAdapter.ViewHolder>() {
    private var mContext : Context? = null
    fun add(data: friend) {
        if (datas == null) datas = LinkedList()
        datas!!.add(data)

        //如果使用notifyDataSetChanged()则没有添加的动画效果
        //notifyDataSetChanged();
        notifyItemInserted(datas!!.size - 1)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var headView: ImageView = view.findViewById(R.id.headView) as ImageView
        val name: TextView = view.findViewById(R.id.name) as TextView
        val location: TextView = view.findViewById(R.id.location) as TextView
        var state: ImageView = view.findViewById(R.id.state) as ImageView
    }

    fun remove(position: Int) {
        if (datas != null) {
            datas!!.removeAt(position)

            //如果使用notifyDataSetChanged()则没有移除的动画效果
            //notifyDataSetChanged();
            notifyItemRemoved(position)
        }
    }

    fun modify(data: friend, position: Int) {
        datas!![position] = data
        notifyDataSetChanged()
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
        val friend=datas[position]

        //设置头像
        holder.headView = friend.headView!!

        //设置名字
        holder.name.text = friend.name

        //设置定位
        holder.location.text = friend.location

        //设置状态
        holder.state = friend.state!!

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

    override fun getItemCount() = datas.size

}