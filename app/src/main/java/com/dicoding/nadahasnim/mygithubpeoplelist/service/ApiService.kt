package com.dicoding.nadahasnim.mygithubpeoplelist.service

import com.dicoding.nadahasnim.mygithubpeoplelist.model.ResponseDetailUser
import com.dicoding.nadahasnim.mygithubpeoplelist.model.ResponseListUsersItem
import com.dicoding.nadahasnim.mygithubpeoplelist.model.ResponseSearchUsers
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("users")
    @Headers("Authorization: token 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")
    fun getAllUsers(): Call<List<ResponseListUsersItem>>

    @GET("search/users")
    @Headers("Authorization: token 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")
    fun searchUsers(
        @Query("q") query: String
    ): Call<ResponseSearchUsers>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<ResponseDetailUser>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getFollowers(
        @Path("username") username: String
    ): Call<List<ResponseListUsersItem>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getFollowing(
        @Path("username") username: String
    ): Call<List<ResponseListUsersItem>>
}