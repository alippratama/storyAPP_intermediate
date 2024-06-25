package com.example.storyapp_intermediate

import com.example.storyapp_intermediate.data.response.ListStoryItem

object DataDummy {

    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val quote = ListStoryItem(
                "id + $i",
                "name + $i",
                "description + $i",
                "photoUrl + $i",
                "createdAt + $i",
                i.toDouble().toString(),
                i.toDouble()
            )
            items.add(quote)
        }
        return items
    }
}