package com.example.storyapp_intermediate.ui.activity

import com.example.storyapp_intermediate.data.response.Story
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.storyapp_intermediate.databinding.ActivityDetailStoryBinding
import com.example.storyapp_intermediate.ui.model.MainViewModel
import com.example.storyapp_intermediate.ui.model.ViewModelFactory

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        supportActionBar?.apply {
            title = "Story Detail"
            setDisplayHomeAsUpEnabled(true)
        }

        val storyId = intent.getStringExtra("storyId")
        if (storyId != null) {
            viewModel.getStory(storyId)
        }

        viewModel.story.observe(this) { story ->
            binding.apply {
                namaDetail.text = story.name
                deskripsiDetail.text = story.description
                Glide.with(root)
                    .load(story.photoUrl)
                    .into(gambarDetail)
            }
        }
    }

    @Suppress("DEPRECATION")
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}