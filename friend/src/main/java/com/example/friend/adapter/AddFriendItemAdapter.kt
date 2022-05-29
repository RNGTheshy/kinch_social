package com.example.friend.adapter

import android.content.Intent
import android.graphics.Outline
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.recyclerview.widget.RecyclerView
import com.chaoshan.data_center.friend.Friend
import com.chaoshan.data_center.togetname.CallBackListUrl
import com.chaoshan.data_center.togetname.Headport
import com.chaoshan.data_center.togetname.center_getname
import com.chaoshan.data_center.togetname.getPersonal_data
import com.example.friend.SentActivity
import com.example.friend.databinding.AddNewFriendItemBinding
import com.example.friend.viewholder.MayBeFriendItemViewHolder

class AddFriendItemAdapter() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var data: List<Friend>? = null
    private var headUrl: List<String>? = null
    fun setData(list: List<Friend>) {
        data = list
        val list2: MutableList<String> = mutableListOf()
        list.forEach {
            list2.add(it.id ?: "")
        }
        if (list.isNotEmpty()) {
            Headport().getAllUrlByObject(list2, object : CallBackListUrl {
                override fun success(list: List<String>) {
                    headUrl = list
                    notifyDataSetChanged()
                }
            })
        }

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = AddNewFriendItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MayBeFriendItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as MayBeFriendItemViewHolder
        val friend = data?.get(position)

        //设置头像
        val headPort = Headport()
        headUrl?.let {
            if (it.isNotEmpty()) {
                headPort.saveToImage(
                    it.get(position),
                    holder.binding.headView.context,
                    holder.binding.headView
                )
            }
        }
        setRadius(holder.binding.headView, 20F)


        //设置名字
        if (friend != null) {
            getPersonal_data.center_getname(friend.id, object : center_getname {
                override fun getname(name: String?) {
                    holder.binding.name.text = name
                }
            })
        }

        holder.binding.root.setOnClickListener {
            it.context.startActivity(
                Intent(it.context, SentActivity::class.java)
                    .putExtra("fId", friend?.id)
            )
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

    override fun getItemCount(): Int {
        return data?.size ?: 0;
    }
}