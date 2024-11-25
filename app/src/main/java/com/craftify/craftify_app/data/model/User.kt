package com.craftify.craftify_app.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var id : String = "0",
    var username : String? = null,
    var email : String? = null,
) : Parcelable
