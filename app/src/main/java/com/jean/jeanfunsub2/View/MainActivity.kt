package com.jean.jeanfunsub2.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.jean.jeanfunsub2.Model.User
import com.jean.jeanfunsub2.R
import com.jean.jeanfunsub2.ViewModel.MainViewModel
import com.jean.jeanfunsub2.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UserGridAdapter
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Github User"

        mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MainViewModel::class.java)

        adapter = UserGridAdapter()
        adapter.notifyDataSetChanged()
        binding.rvUser.setHasFixedSize(true)
        binding.rvUser.layoutManager = GridLayoutManager(this, 2)

        recycler()
        showLoading(true)
        searching()

        mainViewModel.Users()
        mainViewModel.getSearch().observe(this, { search ->
            if (search != "") {
                mainViewModel.findByUsername(search)
                setData()
            } else {
                mainViewModel.getUser()
                setData()
            }
        })

    }


    private fun searching() {
        mainViewModel.search.postValue("")
        binding.svSearch.apply {
            setOnClickListener { onActionViewExpanded() }
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    showLoading(true)
                    mainViewModel.findByUsername(query)
                    mainViewModel.setSearch(query)
                    setData()
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    showLoading(true)
                    if (newText.isNotEmpty()) {
                        mainViewModel.findByUsername(newText)
                        mainViewModel.setSearch(newText)
                        setData()
                    } else {
                        mainViewModel.Users()
                        setData()
                    }
                    return true
                }
            })
        }
    }

    fun setData() {
        mainViewModel.getState().observe(this, { state ->
            if(state) {
                mainViewModel.getUser().observe(this, { userItems ->
                    if (userItems != null) {
                        binding.dataNotFound.visibility = View.GONE
                        adapter.setData(userItems)
                        recycler()
                    }
                })
            } else {
                showLoading(false)
                binding.dataNotFound.visibility = View.VISIBLE
            }
        })
    }

    private fun showLoading(state: Boolean) {
        if(state) {
            binding.skProgressBar.visibility = View.VISIBLE
        } else {
            lifecycleScope.launch(Dispatchers.Default) {
                delay(1000L)
                withContext(Dispatchers.Main) {
                    binding.skProgressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun recycler() {
        showLoading(false)
        binding.rvUser.adapter = adapter

        adapter.setOnItemClickCallback(object : UserGridAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val moveIntent = Intent(this@MainActivity, DetailUser::class.java).apply{
                    putExtra(DetailUser.EXTRA_USER, data)
                }
                startActivity(moveIntent)
            }
        })
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item.itemId == R.id.menu_setTer) {
//            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
//            startActivity(mIntent)
//        }
//        return super.onOptionsItemSelected(item)
//    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.item_menu.option_menu, menu)
//        return true
//    }


}