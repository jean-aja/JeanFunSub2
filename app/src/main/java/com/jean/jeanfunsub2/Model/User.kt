package com.jean.jeanfunsub2.Model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var avatar: String? = null,
    var name: String? = null,
    var username: String? = null,
    var repository: String? = null,
    var followers: String? = null,
    var following: String? = null,
    var location: String? = null,
    var company: String? = null
) : Parcelable
