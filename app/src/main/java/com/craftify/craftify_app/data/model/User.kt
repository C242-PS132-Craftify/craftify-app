package com.craftify.craftify_app.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var username : String? = null,
    var email : String? = null,
    var profile_picture : String? = null
) : Parcelable
