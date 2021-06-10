package com.jean.jeanfunsub2.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jean.jeanfunsub2.Model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class DetailViewModel : ViewModel() {
    val userDetail = MutableLiveData<User>()
    val apiKey = "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"

    fun getDetailUser(): LiveData<User> = userDetail

    fun setUserDetail(username: String) {
        val url = "${MainViewModel.url}/users/$username"
        val client = AsyncHttpClient()
        client.addHeader("Authorization", apiKey)
        client.addHeader("User-Agent", "request")
        client.get(
            url, object : AsyncHttpResponseHandler() {
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>,
                    responseBody: ByteArray
                ) {
                    try {
                        val result = String(responseBody)
                        Log.d("Parsing", result)
                        val responseObject = JSONObject(result)
                        val user = User()
                        user.avatar = responseObject.getString("avatar_url")
                        user.name = responseObject.getString("name")
                        user.username = responseObject.getString("login")
                        user.repository = responseObject.getString("public_repos")
                        user.followers = responseObject.getString("followers")
                        user.following = responseObject.getString("following")
                        user.company = responseObject.getString("company")
                        user.location = responseObject.getString("location")
                        userDetail.postValue(user)

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
}