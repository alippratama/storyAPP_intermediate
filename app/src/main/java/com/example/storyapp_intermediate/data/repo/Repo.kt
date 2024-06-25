package com.example.storyapp_intermediate.data.repo

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storyapp_intermediate.data.api.ApiService
import com.example.storyapp_intermediate.data.pref.Pref
import com.example.storyapp_intermediate.data.pref.UserModel
import com.example.storyapp_intermediate.data.response.DetailStoryResponse
import com.example.storyapp_intermediate.data.response.ListStoryItem
import com.example.storyapp_intermediate.data.response.LoginResponse
import com.example.storyapp_intermediate.data.response.NewStoryResponse
import com.example.storyapp_intermediate.data.response.RegisterResponse
import com.example.storyapp_intermediate.data.response.StoryResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class Repo private constructor(
    private val apiService: ApiService,
    private val userPreference: Pref,
) {
    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return apiService.dftrUser(name, email, password)
    }

    suspend fun login(email: String, password: String): LoginResponse {
        return apiService.lgnUser(email, password)
    }

    fun getStories(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = { StoryPagingSource(apiService) }
        ).liveData
    }



    suspend fun getDetailStory(id: String): DetailStoryResponse {
        return apiService.getDetailStori(id)
    }

    suspend fun addStory(photo: MultipartBody.Part, description: RequestBody): NewStoryResponse {
        return apiService.tmbhStori(photo, description)
    }

    suspend fun getStoriesWithLocation(): StoryResponse {
        return apiService.getStoriesWithLocation()
    }

    companion object {
        fun getInstance(apiService: ApiService, userPreference: Pref) =
            Repo(apiService, userPreference)
    }
}