package com.jean.jeanfunsub2.View

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.jean.jeanfunsub2.Model.User
import com.jean.jeanfunsub2.R
import com.jean.jeanfunsub2.ViewModel.DetailViewModel
import com.jean.jeanfunsub2.databinding.UserDetailBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailUser : AppCompatActivity() {
    private lateinit var binding: UserDetailBinding
    private lateinit var detailViewModel: DetailViewModel

    companion object {
        const val EXTRA_USER = "extra_user"
        private val TAB_TITLES = intArrayOf(
            R.plurals.follower,
            R.plurals.following
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Detail User"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setTabLayout()
        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailViewModel::class.java)

        showLoadingDetail(true)
        detailViewModel.setUserDetail(intent.getParcelableExtra<User>(EXTRA_USER)?.username as String)
        setData()
    }

    private fun setTabLayout(){
        val user: User = intent?.getParcelableExtra<User>(EXTRA_USER) as User

        val sectionsPagerAdapter = SectionPagerAdapter(this)
        sectionsPagerAdapter.name = user.username

        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = binding.tlTabLayout
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getQuantityText(TAB_TITLES[position],2)
        }.attach()
    }

    private fun validatingData(data: String): String {
        val message: String
        if (data != "null") {
            message = data
        } else {
            val language:String = resources.getString(R.string.noData)
            message = language
        }
        return message
    }

    private fun showLoadingDetail(state: Boolean) {
        if(state) {
            binding.skProgressBar.visibility = View.VISIBLE
        } else {
            lifecycleScope.launch(Dispatchers.Default) {
                delay(2500L)
                withContext(Dispatchers.Main) {
                    binding.skProgressBar.visibility = View.GONE
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setData() {
        detailViewModel.getDetailUser().observe(this, { userDataItems ->
            if (userDataItems != null) {
                showLoadingDetail(false)
                with(binding) {
                    Glide.with(this@DetailUser)
                        .load(userDataItems.avatar)
                        .into(sivDetailAvatar)

                    tvDetailName.text = validatingData(userDataItems.name as String)
                    tvDetailUsername.text = validatingData(userDataItems.username as String)
                    tvDetailCountRepository.text = validatingData(userDataItems.repository as String)
                    tvDetailCountFollowers.text = validatingData(userDataItems.followers as String)
                    tvDetailCountFollowing.text = validatingData(userDataItems.following as String)
                    tvDetailCompany.text = validatingData(userDataItems.company as String)
                    tvDetailLocation.text = validatingData(userDataItems.location as String)
                }
            }
        })
    }


}