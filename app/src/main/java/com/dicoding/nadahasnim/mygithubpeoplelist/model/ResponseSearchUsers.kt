package com.dicoding.nadahasnim.mygithubpeoplelist.model

import com.google.gson.annotations.SerializedName

data class ResponseSearchUsers(

	@field:SerializedName("total_count")
	val totalCount: Int,

	@field:SerializedName("incomplete_results")
	val incompleteResults: Boolean,

	@field:SerializedName("items")
	val items: List<ResponseListUsersItem>
)