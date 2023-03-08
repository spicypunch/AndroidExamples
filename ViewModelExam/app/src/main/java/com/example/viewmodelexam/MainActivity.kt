package com.example.viewmodelexam

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.viewmodelexam.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        binding.user = userViewModel

        userViewModel.height.observe(this, Observer {
            binding.textViewHeight.text = it.toString()
        })
    }
}