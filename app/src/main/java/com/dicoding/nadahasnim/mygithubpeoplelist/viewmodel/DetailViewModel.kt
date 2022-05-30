package com.dicoding.nadahasnim.mygithubpeoplelist.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.nadahasnim.mygithubpeoplelist.config.ApiConfig
import com.dicoding.nadahasnim.mygithubpeoplelist.model.ResponseDetailUser
import com.dicoding.nadahasnim.mygithubpeoplelist.model.ResponseListUsersItem
import com.dicoding.nadahasnim.mygithubpeoplelist.model.ResponseRepositories
import com.dicoding.nadahasnim.mygithubpeoplelist.service.ResponseCall
import com.dicoding.nadahasnim.mygithubpeoplelist.service.Status
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(username: String) : ViewModel() {
    private val _detailUser = MutableLiveData<ResponseDetailUser>()
    val detailUser: LiveData<ResponseDetailUser> = _detailUser

    private val _followers = MutableLiveData<List<ResponseListUsersItem>>()
    val followers: LiveData<List<ResponseListUsersItem>> = _followers

    private val _following = MutableLiveData<List<ResponseListUsersItem>>()
    val following: LiveData<List<ResponseListUsersItem>> = _following

    private val _repositories = MutableLiveData<Int>()
    val repositories: LiveData<Int> = _repositories

    private val _responseCall = MutableLiveData<ResponseCall>()
    val responseCall: LiveData<ResponseCall> = _responseCall

    init {
        getUser(username)
    }

    fun getUser(username: String) {
        _responseCall.value = ResponseCall(Status.LOADING)

        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<ResponseDetailUser> {
            override fun onResponse(
                call: Call<ResponseDetailUser>,
                response: Response<ResponseDetailUser>
            ) {
                if (response.isSuccessful) {
                    _detailUser.value = response.body()
                    getFollowers(username)
                } else {
                    _responseCall.value = ResponseCall(Status.ERROR, response.message())
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseDetailUser>, t: Throwable) {
                _responseCall.value = ResponseCall(Status.ERROR, t.message)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun getFollowers(username: String) {
        _responseCall.value = ResponseCall(Status.LOADING)

        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<List<ResponseListUsersItem>> {
            override fun onResponse(
                call: Call<List<ResponseListUsersItem>>,
                response: Response<List<ResponseListUsersItem>>
            ) {
                if (response.isSuccessful) {
                    _followers.value = response.body()
                    getFollowing(username)
                } else {
                    _responseCall.value = ResponseCall(Status.ERROR, response.message())
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ResponseListUsersItem>>, t: Throwable) {
                _responseCall.value = ResponseCall(Status.ERROR, t.message)
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    private fun getFollowing(username: String) {
        _responseCall.value = ResponseCall(Status.LOADING)

        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<ResponseListUsersItem>> {
            override fun onResponse(
                call: Call<List<ResponseListUsersItem>>,
                response: Response<List<ResponseListUsersItem>>
            ) {
                if (response.isSuccessful) {
                    _following.value = response.body()
                    getRepositories(username)
                } else {
                    _responseCall.value = ResponseCall(Status.ERROR, response.message())
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ResponseListUsersItem>>, t: Throwable) {
                _responseCall.value = ResponseCall(Status.ERROR, t.message)
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    private fun getRepositories(username: String) {
        _responseCall.value = ResponseCall(Status.LOADING)

        val client = ApiConfig.getApiService().getRepositories(username)
        client.enqueue(object : Callback<List<ResponseRepositories>> {
            override fun onResponse(
                call: Call<List<ResponseRepositories>>,
                response: Response<List<ResponseRepositories>>
            ) {
                if (response.isSuccessful) {
                    _repositories.value = response.body()?.size
                    _responseCall.value = ResponseCall(Status.COMPLETED)
                } else {
                    _responseCall.value = ResponseCall(Status.ERROR, response.message())
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ResponseRepositories>>, t: Throwable) {
                _responseCall.value = ResponseCall(Status.ERROR, t.message)
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    companion object {
        private const val TAG = "DetailViewModel"
    }
}