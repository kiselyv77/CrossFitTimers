package com.example.crossfit

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.crossfit.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Crossfit)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        NAV_CONTROLLER = findNavController(R.id.nav_host_fragment_activity_main)
        NAV_CONTROLLER.navigate(R.id.mainFragment)
    }
}