package com.example.storyapp_intermediate.data.response

import com.google.gson.annotations.SerializedName

data class NewStoryResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)
