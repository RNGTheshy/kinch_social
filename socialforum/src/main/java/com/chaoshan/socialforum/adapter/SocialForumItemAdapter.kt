package com.chaoshan.socialforum.adapter

import android.annotation.SuppressLint
import android.graphics.Outline
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chaoshan.data_center.dynamic.Dynamic
import com.chaoshan.socialforum.databinding.SocialForumItemViewBinding
import com.chaoshan.socialforum.viewholder.SocialForumItemViewHolder
import kotlinx.coroutines.GlobalScope
import kotlin.concurrent.thread

class SocialForumItemAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var data: List<Dynamic>? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<Dynamic>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val item = SocialForumItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SocialForumItemViewHolder(item)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as SocialForumItemViewHolder
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


            Glide.with(holder.binding.root.context)
                    .load(it[position].picture)
                    .centerCrop()
                    .into(holder.binding.mainImage)
        }
        setRadius(holder.itemView, 20.0F)

    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
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