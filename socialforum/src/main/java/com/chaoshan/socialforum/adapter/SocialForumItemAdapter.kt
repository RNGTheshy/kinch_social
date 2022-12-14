package com.chaoshan.socialforum.adapter

import android.annotation.SuppressLint
import android.graphics.Outline
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chaoshan.data_center.dynamic.comment.CommentClient
import com.chaoshan.data_center.dynamic.comment.GetCommentCountListener
import com.chaoshan.data_center.dynamic.dynamic.Dynamic
import com.chaoshan.data_center.dynamic.like.GetLikeCountCallBack
import com.chaoshan.data_center.dynamic.like.LikeClient
import com.chaoshan.data_center.togetname.Headport
import com.chaoshan.data_center.togetname.center_getname
import com.chaoshan.data_center.togetname.getPersonal_data
import com.chaoshan.socialforum.activity.SocialForumMoreActivity
import com.chaoshan.socialforum.databinding.SocialForumItemViewBinding
import com.chaoshan.socialforum.viewholder.SocialForumItemViewHolder
import com.chaoshan.socialforum.viewmodel.SocialForumActivityViewModel

class SocialForumItemAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var data: List<Dynamic>? = null

    private val viewModel by lazy { SocialForumActivityViewModel() }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<Dynamic>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val item =
            SocialForumItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SocialForumItemViewHolder(item)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as SocialForumItemViewHolder
        data?.get(position)?.let { p ->
            holder.binding.root.setOnClickListener {
                p.dynamicId?.let { it1 ->
                    viewModel.currentDynamic.value = p
                    SocialForumMoreActivity.goTo(it.context, it1, p)
                }
            }
        }

        data?.let {
            if (it[position].text == "null" || it[position].text.isNullOrEmpty()) {
                holder.binding.mainText.visibility = View.GONE
            } else {
                holder.binding.mainText.text = it[position].text
            }
            if (it[position].theme == "null" || it[position].theme.isNullOrEmpty()) {
                holder.binding.textSecond.visibility = View.GONE
            } else {
                holder.binding.textSecond.text = "#" + it[position].theme
            }
            holder.binding.timeText.text = it[position].releaseTime

            it[position].dynamicId?.let {
                LikeClient.getLikeCount(it, object :
                    GetLikeCountCallBack {
                    override fun get(count: Int) {
                        holder.binding.likesText.text = count.toString()
                    }
                })
            }

            Glide.with(holder.binding.root.context)
                .load(it[position].picture)
                .centerCrop()
                .into(holder.binding.mainImage)

            it[position].dynamicId?.let { it1 ->
                CommentClient.getDataCount(it1, object : GetCommentCountListener {
                    override fun getCount(count: Int) {
                        holder.binding.commentCount.text = count.toString()
                    }

                })
            }

            getPersonal_data.center_getname(it[position].userID, object : center_getname {
                override fun getname(name: String?) {
                    holder.binding.nameText.text = name
                }
            })
            it[position].userID?.let { it1 -> Headport().setImage(it1, holder.binding.titleImage) }
            setRadius(holder.binding.titleImage, 5.0F)

        }
        setRadius(holder.itemView, 20.0F)

    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    /**
     * ????????????ImageView?????????
     * @param radius: ???????????? px
     */
    private fun setRadius(view: View, radius: Float) {
        view.clipToOutline = true
        view.outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                outline.setRoundRect(0, 0, view.width, view.height, radius)
            }
        }
        //????????????
        view.elevation = 20F;
    }
}