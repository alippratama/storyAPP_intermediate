package com.example.storyapp_intermediate.ui.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp_intermediate.data.pref.UserModel
import com.example.storyapp_intermediate.data.repo.Repo
import com.example.storyapp_intermediate.data.response.ErrorResponse
import com.example.storyapp_intermediate.data.response.LoginResponse
import com.example.storyapp_intermediate.data.response.Story
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(private val repository: Repo) : ViewModel() {
    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _story = MutableLiveData<Story>()
    val story: LiveData<Story> = _story

    fun login(email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.login(email, password)
                _loginResponse.postValue(response)

            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                _error.postValue(errorMessage!!)
            }
            _isLoading.value = false
        }
    }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun getStory(id: String) {
        viewModelScope.launch {
            val response = repository.getDetailStory(id)
            _story.value = (response.story)
        }
    }


}