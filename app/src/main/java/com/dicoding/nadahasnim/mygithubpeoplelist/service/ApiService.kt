package com.dicoding.nadahasnim.mygithubpeoplelist.service

import com.dicoding.nadahasnim.mygithubpeoplelist.model.ResponseDetailUser
import com.dicoding.nadahasnim.mygithubpeoplelist.model.ResponseListUsers
import com.dicoding.nadahasnim.mygithubpeoplelist.model.ResponseSearchUsers
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("users")
    @Headers("Authorization: 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")
    fun getAllUsers(): Call<ResponseListUsers>

    @GET("search/users")
    @Headers("Authorization: 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")
    fun searchUsers(
        @Query("q") query: String
    ): Call<ResponseSearchUsers>

    @GET("users/{username}")
    @Headers("Authorization: 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")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<ResponseDetailUser>

    @GET("users/{username}/followers")
    @Headers("Authorization: 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")
    fun getFollowers(
        @Path("username") username: String
    ): Call<ResponseListUsers>

    @GET("users/{username}/following")
    @Headers("Authorization: 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")
    fun getFollowing(
        @Path("username") username: String
    ): Call<ResponseListUsers>
}