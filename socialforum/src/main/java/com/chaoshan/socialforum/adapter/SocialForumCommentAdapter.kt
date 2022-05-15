package com.chaoshan.socialforum.adapter

import android.annotation.SuppressLint
import android.graphics.Outline
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chaoshan.data_center.dynamic.comment.Comment
import com.chaoshan.data_center.dynamic.comment.CommentClient
import com.chaoshan.data_center.dynamic.comment.GetCommentCountListener
import com.chaoshan.data_center.dynamic.dynamic.Dynamic
import com.chaoshan.data_center.dynamic.like.GetLikeCountCallBack
import com.chaoshan.data_center.dynamic.like.GetLikePersonList
import com.chaoshan.data_center.dynamic.like.Like
import com.chaoshan.data_center.dynamic.like.LikeClient
import com.chaoshan.data_center.togetname.center_getname
import com.chaoshan.data_center.togetname.getPersonal_data
import com.chaoshan.socialforum.databinding.SocialForumItemViewBinding
import com.chaoshan.socialforum.databinding.SocialForumLikeViewBinding
import com.chaoshan.socialforum.databinding.SocialForumMoreCommentViewholderBinding
import com.chaoshan.socialforum.viewholder.SocialForumCommentItemViewHolder
import com.chaoshan.socialforum.viewholder.SocialForumItemViewHolder
import com.chaoshan.socialforum.viewholder.SocialForumLikeItemViewHolder
import com.chaoshan.socialforum.viewmodel.SocialForumActivityViewModel
import com.chaoshan.socialforum.viewmodel.SocialForumMoreActivityViewModel

class SocialForumCommentAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var currentCYID: String
    private val viewModel = SocialForumMoreActivityViewModel()
    private var commentList: List<Comment>? = null
    private lateinit var currentCY: Dynamic
    fun setCurrentCYID(currentCYID: String) {
        this.currentCYID = currentCYID
    }

    fun setCurrentCY(currentCY: Dynamic) {
        this.currentCY = currentCY
    }

    @SuppressLint("NotifyDataSetChanged")
    fun reFresh() {
        notifyDataSetChanged()
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
            this.currentCY.let {
                if (it.text == "null" || it.text.isNullOrEmpty()) {
                    holder.binding.mainText.visibility = View.GONE
                } else {
                    holder.binding.mainText.text = it.text
                }
                if (it.theme == "null" || it.theme.isNullOrEmpty()) {
                    holder.binding.textSecond.visibility = View.GONE
                } else {
                    holder.binding.textSecond.text = "#" + it.theme
                }
                holder.binding.timeText.text = it.releaseTime


                Glide.with(holder.binding.root.context)
                    .load(it.picture)
                    .centerCrop()
                    .into(holder.binding.mainImage)
                it.dynamicId?.let { it1 ->
                    CommentClient.getDataCount(it1, object : GetCommentCountListener {
                        override fun getCount(count: Int) {
                            holder.binding.commentCount.text = count.toString()
                        }

                    })
                }
                getPersonal_data.center_getname(it.userID, object : center_getname {
                    override fun getname(name: String?) {
                        holder.binding.nameText.text = name
                    }
                })

            }


        } else {
            setRadius(holder.itemView.rootView, 10.0F)
        }
        if (position > 3) {
            holder as SocialForumCommentItemViewHolder
            holder.binding.iconMessage.visibility = View.INVISIBLE
        }
        if (position >= 2) {
            commentList?.let {
                holder as SocialForumCommentItemViewHolder
                holder.binding.mainComment.text = it[position - 2].comment
                holder.binding.commentTime.text = it[position - 2].time
            }

        }
        if (position == 1) {

            LikeClient.getLikePerson(this.currentCYID, object : GetLikePersonList {
                override fun getLikePersonList(likeList: List<Like>) {
                    var outString: String = ""
                    likeList.forEach {
                        getPersonal_data.center_getname(it.likePeopleId, object : center_getname {
                            override fun getname(name: String?) {
                                outString = "$outString;$name"
                                holder as SocialForumLikeItemViewHolder
                                holder.binding.commentName.text = outString
                            }
                        })
                    }

                    holder as SocialForumLikeItemViewHolder
                    holder.binding.commentName.text = outString
                }
            })

        }

    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return commentList?.size?.plus(2) ?: 0
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

    @SuppressLint("NotifyDataSetChanged")
    fun setData(comment: List<Comment>) {
        this.commentList = comment
        notifyDataSetChanged()
    }
}