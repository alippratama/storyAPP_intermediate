package com.example.storyapp_intermediate.ui.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp_intermediate.data.repo.Repo
import com.example.storyapp_intermediate.data.response.ListStoryItem
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MapViewModel(private val repo: Repo) : ViewModel() {
    private val _stories = MutableLiveData<List<ListStoryItem>>()
    val stories: LiveData<List<ListStoryItem>> = _stories

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun getStoriesWithLocation() {
        viewModelScope.launch {
            try {
                val response = repo.getStoriesWithLocation()
                _stories.value = (response.listStory)
            } catch (e: HttpException) {
                if (e.code() == 401) {
                    _error.value = e.message()
                }
            }
        }
    }
}