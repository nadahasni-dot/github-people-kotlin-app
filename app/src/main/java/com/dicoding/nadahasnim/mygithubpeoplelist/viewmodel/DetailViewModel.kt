package com.dicoding.nadahasnim.mygithubpeoplelist.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.nadahasnim.mygithubpeoplelist.config.ApiConfig
import com.dicoding.nadahasnim.mygithubpeoplelist.model.ResponseDetailUser
import com.dicoding.nadahasnim.mygithubpeoplelist.service.ResponseCall
import com.dicoding.nadahasnim.mygithubpeoplelist.service.Status
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {
    private val _detailUser = MutableLiveData<ResponseDetailUser>()
    val detailUser: LiveData<ResponseDetailUser> = _detailUser

    private val _responseCall = MutableLiveData<ResponseCall>()
    val responseCall: LiveData<ResponseCall> = _responseCall

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
                    _responseCall.value = ResponseCall(Status.COMPLETED)
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

    companion object {
        private const val TAG = "DetailViewModel"
    }
}