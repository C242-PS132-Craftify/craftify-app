package com.craftify.craftify_app.ui.result

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.craftify.craftify_app.data.repository.SavedCraftRepository
import com.craftify.craftify_app.di.Injection

class ResultViewModelFactory(private val repository: SavedCraftRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResultViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ResultViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    companion object {
        @Volatile
        private var instance: ResultViewModelFactory? = null
        fun getInstance(context: Context): ResultViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ResultViewModelFactory(Injection.provideResultRepository(context))
            }.also { instance = it }
    }
}
