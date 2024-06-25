package com.example.storyapp_intermediate.di

import android.content.Context
import com.example.storyapp_intermediate.data.api.ApiConfig
import com.example.storyapp_intermediate.data.pref.Pref
import com.example.storyapp_intermediate.data.pref.dataStore
import com.example.storyapp_intermediate.data.repo.Repo
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): Repo {
        val pref = Pref.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return Repo.getInstance(apiService, pref)
    }
}