package com.example.friend.adapter

import android.content.Context
import android.graphics.Outline
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.chaoshan.data_center.togetname.Headport
import com.chaoshan.data_center.friend.Friend
import com.chaoshan.data_center.togetname.center_getname
import com.chaoshan.data_center.togetname.getPersonal_data
import com.example.friend.R


class friendIconAdapter(var datas: List<Friend>) :
    RecyclerView.Adapter<friendIconAdapter.ViewHolder>() {
    private var mContext: Context? = null

    fun setData(datas: List<Friend>) {
        this.datas = datas
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var head: ImageView = view.findViewById(R.id.head) as ImageView
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
            LayoutInflater.from(parent.context).inflate(R.layout.icon_friend, parent, false)
        mContext = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //为viewholder绑定数据
        val friend = datas[position]

        //设置头像
        val headport = Headport()
        friend.id?.let { headport.setImage(it, holder.head) }

        setRadius(holder.head, 20F)
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
    }

    override fun getItemCount() = datas.size

}