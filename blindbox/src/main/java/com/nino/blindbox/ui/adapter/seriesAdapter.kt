package com.nino.blindbox.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nino.blindbox.R
import com.nino.blindbox.blindbox.series
import java.util.*
import androidx.recyclerview.widget.LinearLayoutManager




class seriesAdapter(var datas: LinkedList<series>) :
    RecyclerView.Adapter<seriesAdapter.ViewHolder>() {
    private var bAdapter: boxAdapter? = null
    private var mContext : Context? = null
    fun add(data: series) {
        if (datas == null) datas = LinkedList()
        datas!!.add(data)

        //如果使用notifyDataSetChanged()则没有添加的动画效果
        //notifyDataSetChanged();
        notifyItemInserted(datas!!.size - 1)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val seriesImage: ImageView = view.findViewById(R.id.detail_title_img) as ImageView
        val seriesName: TextView = view.findViewById(R.id.detail_title) as TextView
        val seriesNum: TextView = view.findViewById(R.id.detail_owned_num) as TextView
        val boxRecyclerView: RecyclerView = view.findViewById(R.id.recycler_content) as RecyclerView
    }

    fun remove(position: Int) {
        if (datas != null) {
            datas!!.removeAt(position)

            //如果使用notifyDataSetChanged()则没有移除的动画效果
            //notifyDataSetChanged();
            notifyItemRemoved(position)
        }
    }

    fun modify(data: series, position: Int) {
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
            LayoutInflater.from(parent.context).inflate(R.layout.bb_series, parent, false)
        mContext = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //为viewholder绑定数据
        val series = datas[position]
        holder.seriesImage.setImageResource(series.photoId)
        holder.seriesName.text = series.name
        holder.seriesNum.text = "（" + series.alreadyOwnedBoxNum.toString() + "个)"

        bAdapter = boxAdapter(series.boxes)
        val linearLayoutManager = LinearLayoutManager(mContext)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        holder.boxRecyclerView.layoutManager = linearLayoutManager
        holder.boxRecyclerView.adapter = bAdapter

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