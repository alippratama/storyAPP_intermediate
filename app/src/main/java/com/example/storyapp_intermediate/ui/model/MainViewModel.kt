package com.example.storyapp_intermediate.ui.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp_intermediate.data.pref.UserModel
import com.example.storyapp_intermediate.data.repo.Repo
import com.example.storyapp_intermediate.data.response.DetailStoryResponse
import com.example.storyapp_intermediate.data.response.ListStoryItem
import com.example.storyapp_intermediate.data.response.NewStoryResponse
import com.example.storyapp_intermediate.data.response.Story
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class MainViewModel(private val repo: Repo) : ViewModel() {



    val story: LiveData<PagingData<ListStoryItem>> by lazy {
        repo.getStories().cachedIn(viewModelScope)
    }

    private val _stories = MutableLiveData<List<ListStoryItem>>()
    val stories: LiveData<List<ListStoryItem>> = _stories

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun getSession(): LiveData<UserModel> {
        return repo.getSession().asLiveData()
    }


    fun logout() {
        viewModelScope.launch {
            repo.logout()
        }
    }
    fun getStory(id: String) {
        viewModelScope.launch {
            val response = repo.getDetailStory(id)
            _story.value = (response.story)
        }
    }
    private val _story = MutableLiveData<Story>()
//    val story: LiveData<Story> = _story



    private val _addResponse = MutableLiveData<NewStoryResponse>()
    val addResponse: LiveData<NewStoryResponse> = _addResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun addStory(photo: MultipartBody.Part, description: RequestBody) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repo.addStory(photo, description)
                _addResponse.value = response

            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, NewStoryResponse::class.java)
                _error.value = errorResponse.message
            }
            _isLoading.value = false
        }
    }
}