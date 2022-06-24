package com.dicoding.nadahasnim.mygithubpeoplelist.service

import com.dicoding.nadahasnim.mygithubpeoplelist.BuildConfig
import com.dicoding.nadahasnim.mygithubpeoplelist.model.ResponseDetailUser
import com.dicoding.nadahasnim.mygithubpeoplelist.model.ResponseListUsersItem
import com.dicoding.nadahasnim.mygithubpeoplelist.model.ResponseRepositories
import com.dicoding.nadahasnim.mygithubpeoplelist.model.ResponseSearchUsers
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("users")
    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    fun getAllUsers(): Call<List<ResponseListUsersItem>>

    @GET("search/users")
    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    fun searchUsers(
        @Query("q") query: String
    ): Call<ResponseSearchUsers>

    @GET("users/{username}")
    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<ResponseDetailUser>

    @GET("users/{username}/followers")
    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    fun getFollowers(
        @Path("username") username: String
    ): Call<List<ResponseListUsersItem>>

    @GET("users/{username}/following")
    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    fun getFollowing(
        @Path("username") username: String
    ): Call<List<ResponseListUsersItem>>

    @GET("users/{username}/repos")
    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    fun getRepositories(
        @Path("username") username: String
    ): Call<List<ResponseRepositories>>
}