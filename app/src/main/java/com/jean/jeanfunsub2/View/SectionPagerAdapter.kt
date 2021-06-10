package com.jean.jeanfunsub2.View

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.jean.jeanfunsub2.databinding.FragmentUserDetailBinding

class SectionPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    private val type = arrayListOf(
        "followers",
        "following"
    )

    var name: String? = null

    override fun createFragment(position: Int): Fragment {
        return UserDetailFragment.newInstance(name, type[position])
    }
    override fun getItemCount(): Int {
        return type.size
    }
}