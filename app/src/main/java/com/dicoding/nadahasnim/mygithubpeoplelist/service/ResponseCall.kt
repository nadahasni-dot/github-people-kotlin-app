package com.dicoding.nadahasnim.mygithubpeoplelist.service

enum class Status {
    LOADING, ERROR, COMPLETED
}

data class ResponseCall(val status: Status = Status.LOADING, val message: String? = null)