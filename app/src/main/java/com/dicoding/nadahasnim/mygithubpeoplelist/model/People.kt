package com.dicoding.nadahasnim.mygithubpeoplelist.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class People(
    val username: String,
    val profileUrl: String,
) : Parcelable
