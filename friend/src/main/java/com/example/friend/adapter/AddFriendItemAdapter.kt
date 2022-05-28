package com.example.friend.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chaoshan.data_center.friend.Friend
import com.chaoshan.data_center.togetname.Headport
import com.chaoshan.data_center.togetname.center_getname
import com.chaoshan.data_center.togetname.getPersonal_data
import com.example.friend.SentActivity
import com.example.friend.databinding.AddNewFriendItemBinding
import com.example.friend.viewholder.MayBeFriendItemViewHolder

class AddFriendItemAdapter() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var data: List<Friend>? = null
    fun setData(list: List<Friend>) {
        data = list
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
        if (friend != null) {
            friend.id?.let { headPort.setImage(it, holder.binding.headView) }
        }


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

    override fun getItemCount(): Int {
        return data?.size ?: 0;
    }
}