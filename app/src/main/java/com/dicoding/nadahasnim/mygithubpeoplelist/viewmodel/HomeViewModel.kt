package com.dicoding.nadahasnim.mygithubpeoplelist.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.nadahasnim.mygithubpeoplelist.config.ApiConfig
import com.dicoding.nadahasnim.mygithubpeoplelist.model.ResponseListUsersItem
import com.dicoding.nadahasnim.mygithubpeoplelist.model.ResponseSearchUsers
import com.dicoding.nadahasnim.mygithubpeoplelist.service.ResponseCall
import com.dicoding.nadahasnim.mygithubpeoplelist.service.Status
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    private val _listUsers = MutableLiveData<List<ResponseListUsersItem>>()
    val listUsers: LiveData<List<ResponseListUsersItem>> = _listUsers

    private val _responseCall = MutableLiveData<ResponseCall>()
    val responseCall: LiveData<ResponseCall> = _responseCall

    init {
        fetchAllUsers()
    }

    fun fetchAllUsers() {
        _responseCall.value = ResponseCall(Status.LOADING)

        val client = ApiConfig.getApiService().getAllUsers()
        client.enqueue(object : Callback<List<ResponseListUsersItem>> {
            override fun onResponse(
                call: Call<List<ResponseListUsersItem>>,
                response: Response<List<ResponseListUsersItem>>
            ) {
                if (response.isSuccessful) {
                    _listUsers.value = response.body()
                    _responseCall.value = ResponseCall(Status.COMPLETED)
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

    fun searchUsers(query: String) {
        _responseCall.value = ResponseCall(Status.LOADING)

        val client = ApiConfig.getApiService().searchUsers(query)
        client.enqueue(object : Callback<ResponseSearchUsers> {
            override fun onResponse(
                call: Call<ResponseSearchUsers>,
                response: Response<ResponseSearchUsers>
            ) {
                if (response.isSuccessful) {
                    _listUsers.value = response.body()?.items
                    _responseCall.value = ResponseCall(Status.COMPLETED)
                } else {
                    _responseCall.value = ResponseCall(Status.ERROR, response.message())
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseSearchUsers>, t: Throwable) {
                _responseCall.value = ResponseCall(Status.ERROR, t.message)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }
}