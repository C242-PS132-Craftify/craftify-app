package com.craftify.craftify_app.utils

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import com.craftify.craftify_app.R

class CustomLoadingDialog(context: Context) {

    @SuppressLint("InflateParams")
    private val dialog: Dialog = Dialog(context).apply {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(false)
        val view = LayoutInflater.from(context).inflate(R.layout.custom_loading, null)
        setContentView(view)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    fun show() {
        dialog.show()
    }

    fun dismiss() {
        dialog.dismiss()
    }
}
