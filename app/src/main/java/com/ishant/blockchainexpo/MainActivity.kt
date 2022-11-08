package com.ishant.blockchainexpo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import com.ishant.blockchainexpo.databinding.ActivityHomeBinding
import com.ishant.blockchainexpo.presentation.home.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this,R.layout.activity_home)
        viewModel.getAllCoin()
        binding.bottomNavigation.menu.get(1).isChecked = true
    }
}