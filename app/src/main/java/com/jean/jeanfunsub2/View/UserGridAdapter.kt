package com.jean.jeanfunsub2.View

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jean.jeanfunsub2.Model.User
import com.jean.jeanfunsub2.databinding.UserItemGridBinding
import com.bumptech.glide.Glide

class UserGridAdapter : RecyclerView.Adapter<UserGridAdapter.GridViewHolder>() {

    inner class GridViewHolder(private val binding: UserItemGridBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            with(binding) {
                with(user) {
                    Glide.with(itemView.context)
                        .load(avatar)
                        .into(sivAvatar)
                    tvUsername.text = user.username
                    itemView.setOnClickListener { onItemClickCallback.onItemClicked(user) }
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

    private val listGridUser = ArrayList<User>()

    fun setData(items: ArrayList<User>) {
        listGridUser.clear()
        listGridUser.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewgroup: ViewGroup, viewType: Int): GridViewHolder {
        val binding =
            UserItemGridBinding.inflate(LayoutInflater.from(viewgroup.context), viewgroup, false)
        return GridViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        holder.bind(listGridUser[position])
    }

    override fun getItemCount(): Int {
        return listGridUser.size
    }
}