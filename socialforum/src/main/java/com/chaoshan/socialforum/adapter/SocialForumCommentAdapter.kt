package com.chaoshan.socialforum.adapter

import android.graphics.Outline
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.recyclerview.widget.RecyclerView
import com.chaoshan.data_center.dynamic.like.GetLikeCountCallBack
import com.chaoshan.data_center.dynamic.like.LikeClient
import com.chaoshan.socialforum.activity.SocialForumMoreActivity
import com.chaoshan.socialforum.databinding.SocialForumItemViewBinding
import com.chaoshan.socialforum.databinding.SocialForumLikeViewBinding
import com.chaoshan.socialforum.databinding.SocialForumMoreCommentViewholderBinding
import com.chaoshan.socialforum.viewholder.SocialForumCommentItemViewHolder
import com.chaoshan.socialforum.viewholder.SocialForumItemViewHolder
import com.chaoshan.socialforum.viewholder.SocialForumLikeItemViewHolder

class SocialForumCommentAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var currentCYID: String

    fun setCurrentCYID(currentCYID: String) {
        this.currentCYID = currentCYID
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            0 -> {
                val binding = SocialForumItemViewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return SocialForumItemViewHolder(binding)
            }
            1 -> {
                val binding = SocialForumLikeViewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return SocialForumLikeItemViewHolder(binding)
            }
            else -> {
                val binding = SocialForumMoreCommentViewholderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return SocialForumCommentItemViewHolder(binding)
            }

        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == 0) {
            holder as SocialForumItemViewHolder
            LikeClient.getLikeCount(currentCYID, object :
                GetLikeCountCallBack {
                override fun get(count: Int) {
                    holder.binding.likesText.text = count.toString()
                }
            })
            setRadius(holder.itemView.rootView, 40.0F)



        } else {

            setRadius(holder.itemView.rootView, 10.0F)
        }
        if (position > 3) {
            holder as SocialForumCommentItemViewHolder
            holder.binding.iconMessage.visibility = View.INVISIBLE
        }

    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return 10
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
}