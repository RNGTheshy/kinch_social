package com.nino.blindbox.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nino.blindbox.R
import com.nino.blindbox.blindbox.box
import com.nino.blindbox.blindbox.series
import java.util.*

class boxAdapter(var datas: LinkedList<box>) :
    RecyclerView.Adapter<boxAdapter.ViewHolder>() {
    fun add(data: box) {
        if (datas == null) datas = LinkedList()
        datas!!.add(data)

        //如果使用notifyDataSetChanged()则没有添加的动画效果
        //notifyDataSetChanged();
        notifyItemInserted(datas!!.size - 1)
    }
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val boxImage: ImageView = view.findViewById(R.id.box_image) as ImageView
        val boxName: TextView = view.findViewById(R.id.boxName) as TextView
    }
    fun remove(position: Int) {
        if (datas != null) {
            datas!!.removeAt(position)

            //如果使用notifyDataSetChanged()则没有移除的动画效果
            //notifyDataSetChanged();
            notifyItemRemoved(position)
        }
    }

    fun modify(data: box, position: Int) {
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
            LayoutInflater.from(parent.context).inflate(R.layout.bb_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //为viewholder绑定数据
        val box=datas[position]
        holder.boxImage.setImageResource(box.photoId)
        holder.boxName.text = box.name
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