package com.jean.jeanfunsub2.View

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jean.jeanfunsub2.Model.User
import com.jean.jeanfunsub2.ViewModel.MainViewModel
import com.jean.jeanfunsub2.databinding.FragmentUserDetailBinding


class UserDetailFragment : Fragment() {

    private lateinit var adapter: UserDetailAdapter
    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: FragmentUserDetailBinding

    //    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//        }
//    }
    companion object {
        private const val Username = "username"
        private const val TypeUser = "typeUser"

        fun newInstance(username: String?, typeUser: String) = UserDetailFragment().apply {
            arguments = Bundle().apply {
                putString(username, Username)
                putString(typeUser, TypeUser)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserDetailBinding.inflate(inflater, container, false)
        adapter = UserDetailAdapter()
        mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(mainViewModel::class.java)

        recyclerView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = arguments?.getString(Username)
        val type = arguments?.getString(TypeUser)

        if (type != null) {
            mainViewModel.follow(username, type)
        }

        showLoading(true)
        inflateData()
    }

    private fun recyclerView() {
        adapter.notifyDataSetChanged()
        binding.rvDetail.setHasFixedSize(true)
        binding.rvDetail.layoutManager = LinearLayoutManager(context)
        binding.rvDetail.adapter = adapter

        adapter.setOnItemClickCallback(object : UserDetailAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val moveToDetail = Intent(context, DetailUser::class.java)
                moveToDetail.putExtra(DetailUser.EXTRA_USER, data)
                startActivity(moveToDetail)

            }
        })
    }


    private fun dataNotFound(state: Boolean) {
        if (state) {
            binding.tvDataNotFound.visibility = View.VISIBLE
        } else {
            binding.tvDataNotFound.visibility = View.GONE
        }
    }

    @Suppress("DEPRECATION")
    private fun inflateData() {
        mainViewModel.getState().observe(viewLifecycleOwner, { state ->
            if (state) {
                mainViewModel.getUser().observe(viewLifecycleOwner, { listUser ->
                    adapter.setData(listUser)
                    dataNotFound(false)
                    showLoading(false)
                })
            } else {
                mainViewModel.getUser().observe(viewLifecycleOwner, { userItems ->
                    adapter.setData(userItems)

                    showLoading(false)
                    val delayTime = 1500L
                    Handler().postDelayed({
                        dataNotFound(true)
                    }, delayTime)
                })
            }
        })
    }

    @Suppress("DEPRECATION")
    private fun showLoading(state: Boolean) {
        val delayTime = 1500L
        if (state) {
            binding.skProgressBar.visibility = View.VISIBLE
        } else {
            Handler().postDelayed({
                binding.skProgressBar.visibility = View.GONE
            }, delayTime)
        }
    }
}