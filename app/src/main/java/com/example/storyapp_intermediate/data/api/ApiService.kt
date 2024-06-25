package com.example.storyapp_intermediate.data.api

import com.example.storyapp_intermediate.data.response.DetailStoryResponse
import com.example.storyapp_intermediate.data.response.LoginResponse
import com.example.storyapp_intermediate.data.response.NewStoryResponse
import com.example.storyapp_intermediate.data.response.RegisterResponse
import com.example.storyapp_intermediate.data.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun dftrUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun lgnUser(
        @Field("email") email: String,
        @Field("password") password: String,
    ): LoginResponse

    @GET("stories")
    suspend fun getStori(position: Int, loadSize: Int): StoryResponse


    @GET("stories/{id}")
    suspend fun getDetailStori(
        @Path("id") id: String,
    ): DetailStoryResponse

    @Multipart
    @POST("stories")
    suspend fun tmbhStori(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): NewStoryResponse

    @GET("stories")
    suspend fun getStoriesWithLocation(
        @Query("location") location: Int = 1,
    ): StoryResponse
}