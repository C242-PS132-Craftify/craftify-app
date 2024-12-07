package com.craftify.craftify_app.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.craftify.craftify_app.data.server.response.DataItem
import com.craftify.craftify_app.data.server.response.DetectionsItem

class HomeViewModel : ViewModel() {
    private val _craftList = MutableLiveData<List<DataItem>>()
    val craftList: LiveData<List<DataItem>> get() = _craftList

    fun setCraftList(list: List<DataItem>?) {
        _craftList.value = list?.filterNotNull()
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
}