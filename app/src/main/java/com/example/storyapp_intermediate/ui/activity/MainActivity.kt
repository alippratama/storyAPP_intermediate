package com.example.storyapp_intermediate.ui.activity

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp_intermediate.R
import com.example.storyapp_intermediate.databinding.ActivityMainBinding
import com.example.storyapp_intermediate.ui.adapter.StoryAdapter
import com.example.storyapp_intermediate.ui.model.MainViewModel
import com.example.storyapp_intermediate.ui.model.ViewModelFactory

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()

            } else {
                viewModel.error.observe(this){
                    showToast(it)
                }

                val connectivityManager =
                    getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                @Suppress("DEPRECATION") val activeNetworkInfo =
                    connectivityManager.activeNetworkInfo
                @Suppress("DEPRECATION") val isConnected =
                    activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting

                viewModel.getSession().observe(this) {
                    if (!isConnected) {
                        showToast("Tidak ada koneksi internet")
                    } else {
                        val adapter = StoryAdapter()
                        binding.rvStory.layoutManager = LinearLayoutManager(this)
                        binding.rvStory.adapter = adapter

                        viewModel.stories.observe(this) {
                            adapter.submitList(it)
                        }
                        viewModel.getStory(user.token)
                    }
                }

                binding.addFab.setOnClickListener {
                    startActivity(Intent(this, AddStoryActivity::class.java))
                }
            }
        }

        setupView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                viewModel.logout()
                true
            }
            R.id.action_maps -> {
                startActivity(Intent(this, MapsActivity::class.java))
                true
            }
            else -> false
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}