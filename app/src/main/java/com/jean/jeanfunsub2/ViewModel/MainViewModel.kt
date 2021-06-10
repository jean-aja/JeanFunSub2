package com.jean.jeanfunsub2.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jean.jeanfunsub2.Model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import java.nio.channels.AsynchronousChannel

class MainViewModel : ViewModel() {
    val listUser = MutableLiveData<ArrayList<User>>()
    val state = MutableLiveData<Boolean>()
    val search = MutableLiveData<String>()
    val apiKey = "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"

    companion object {
        const val url = "https://api.github.com"
    }

    fun getUser(): LiveData<ArrayList<User>> = listUser
    fun getState(): LiveData<Boolean> = state
    fun getSearch(): LiveData<String> = search

    fun setSearch(text: String) {
        search.postValue(text)
    }

    //listuserny
    fun Users() {
        val listItems = ArrayList<User>()
        val url = "$url/users"
        val client = AsyncHttpClient()
        client.addHeader("Authorization", apiKey)
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    Log.d("Sukses parsing users", result)
                    val jsonArray = JSONArray(result)

                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val usersItems = User()
                        usersItems.avatar = jsonObject.getString("avatar_url")
                        usersItems.username = jsonObject.getString("login")
                        listItems.add(usersItems)
                    }
                    listUser.postValue(listItems)
                    state.postValue(true)
                } catch (e: Exception) {
                    Log.d("onSuccess: Gagal.....", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                Log.d("onFailure: Gagal.....", error.message.toString())
            }
        })
    }

    fun findByUsername(username: String?) {
        val listItems = ArrayList<User>()

        val url = "$url/search/users?q=$username"
        val client = AsyncHttpClient()
        client.addHeader("Authorization", apiKey)
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    Log.d("Sukses Parsing username", result)
                    val responseObject = JSONObject(result)
                    val count = responseObject.getInt("total_count")
                    if (count >= 1) {
                        val items = responseObject.getJSONArray("items")

                        for (i in 0 until items.length()) {
                            val item = items.getJSONObject(i)
                            val user = User()
                            user.avatar = item.getString("avatar_url")
                            user.username = item.getString("login")
                            listItems.add(user)
                        }
                        listUser.postValue(listItems)
                        state.postValue(true)
                    } else {
                        listUser.postValue(listItems)
                        state.postValue(false)
                    }
                } catch (e: Exception) {
                    Log.d("onSuccess: Gagal.....", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Log.d("onFailure: Gagal.....", error?.message.toString())
            }
        })
    }

    fun follow(username: String?, type: String?) {
        val listItems = ArrayList<User>()

        val url = "$url/users/$username/$type"
        val client = AsyncHttpClient()
        client.addHeader("Authorization", apiKey)
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    Log.d("Parsingfoll", result)
                    val responseArray = JSONArray(result)
                    if (responseArray.length() > 0) {

                        for (i in 0 until responseArray.length()) {
                            val item = responseArray.getJSONObject(i)
                            val user = User()
                            user.avatar = item.getString("avatar_url")
                            user.username = item.getString("login")
                            listItems.add(user)
                        }
                        listUser.postValue(listItems)
                        state.postValue(true)
                    } else {
                        listUser.postValue(listItems)
                        state.postValue(false)
                    }
                } catch (e: Exception) {
                    Log.d("onSuccess: Gagal.....", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Log.d("onFailure: Gagal.....", error?.message.toString())
            }
        })
    }

//end
}