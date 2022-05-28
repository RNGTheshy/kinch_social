package com.example.friend.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chaoshan.data_center.friend.Friend
import com.chaoshan.data_center.togetname.Headport
import com.chaoshan.data_center.togetname.center_getname
import com.chaoshan.data_center.togetname.getPersonal_data
import com.example.friend.MayBeFriendItemViewHolder
import com.example.friend.SentActivity
import com.example.friend.databinding.AddFriendAgreeItemBinding
import com.example.friend.databinding.AddNewFriendItemBinding
import com.example.friend.databinding.TextViewBinding
import com.example.friend.viewholder.AddFriendAgreeItemViewHolder
import com.example.friend.viewholder.TitleViewHolder

class MayBeFriend : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var agreeData: List<String>? = null
    private var mayBeData: List<Friend>? = null

    fun setAgreeData(listdata: List<String>) {
        agreeData = listdata
    }

    fun setMayBeData(listdata: List<Friend>) {
        mayBeData = listdata
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            0 -> {
                val binding = TextViewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return TitleViewHolder(binding)
            }
            1 -> {
                val binding = AddFriendAgreeItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return AddFriendAgreeItemViewHolder(binding)

            }
            2 -> {
                val binding = TextViewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return TitleViewHolder(binding)
            }
            3 -> {
                val binding = AddNewFriendItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return MayBeFriendItemViewHolder(binding)
            }
            else -> {
                val binding = TextViewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return TitleViewHolder(binding)
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == 0) {
            holder as TitleViewHolder
            holder.binding.mainText.text = "好友通知"
        }
        if (position > 0 && position <= agreeData?.size ?: 0) {


        }
        if (position == (agreeData?.size ?: 0) + 1) {
            holder as TitleViewHolder
            holder.binding.mainText.text = "可能认识的人"

        }
        if (position >= (agreeData?.size ?: 0) + 2) {
            holder as MayBeFriendItemViewHolder

            //为viewholder绑定数据
            if ((position - ((agreeData?.size ?: 0) + 2)) >= mayBeData?.size ?: 0) {
                return
            }
            val friend = mayBeData?.get(position - ((agreeData?.size ?: 0) + 2))

            //设置头像
            val headport = Headport()
            if (friend != null) {
                friend.id?.let { headport.setImage(it, holder.binding.headView) }
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
                var a = 1;
                a = 2;
                it.context.startActivity(Intent(it.context, SentActivity::class.java))
            }


        }
    }

    override fun getItemCount(): Int {
        return (agreeData?.size ?: 0) + (mayBeData?.size ?: 0) + 2;
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return 0;
        }
        if (position > 0 && position <= agreeData?.size ?: 0) {
            return 1;
        }
        return if (position == (agreeData?.size ?: 0) + 1) {
            2
        } else {
            3;
        }
    }
}