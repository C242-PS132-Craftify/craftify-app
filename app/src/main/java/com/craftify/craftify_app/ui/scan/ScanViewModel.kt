package com.craftify.craftify_app.ui.scan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.craftify.craftify_app.data.server.api.DetectionsItem
import com.craftify.craftify_app.data.server.api.RecommendationsItem

class ScanViewModel : ViewModel() {
    private val _detectionsList = MutableLiveData<List<DetectionsItem>>()
    private val _recommendationsList = MutableLiveData<List<RecommendationsItem>>()
    val detectionsList: LiveData<List<DetectionsItem>> get() = _detectionsList
    val recommendationsList: LiveData<List<RecommendationsItem>> get() = _recommendationsList

    fun setDetectionsList(list: List<DetectionsItem>?) {
        _detectionsList.value = list?.filterNotNull()
    }

    fun setRecommendationsList(list: List<RecommendationsItem>?) {
        _recommendationsList.value = list?.filterNotNull()
    }



}