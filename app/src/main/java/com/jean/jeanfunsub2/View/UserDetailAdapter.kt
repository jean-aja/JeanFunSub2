package com.jean.jeanfunsub2.View

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jean.jeanfunsub2.Model.User
//import com.jean.jeanfunsub2.databinding.UserItemGridBinding
import com.jean.jeanfunsub2.databinding.UserItemRowBinding

class UserDetailAdapter : RecyclerView.Adapter<UserDetailAdapter.GridViewHolder>() {

    inner class GridViewHolder(private val binding: UserItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            with(binding) {
                with(user) {
                    Glide.with(itemView.context)
                        .load(avatar)
                        .into(sivAvatar)
                    tvUsername.text = username
                }
            }
        }
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }

    private val detailGridUser = ArrayList<User>()

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): UserDetailAdapter.GridViewHolder {
        val binding:UserItemRowBinding =
            UserItemRowBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return GridViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        holder.bind(detailGridUser[position])
    }

    override fun getItemCount(): Int = detailGridUser.size

    fun setData(items: ArrayList<User>) {
        detailGridUser.clear()
        detailGridUser.addAll(items)
        notifyDataSetChanged()
    }
}