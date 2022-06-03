package com.alinaincorporated.diploma.ui.splash_screen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.alinaincorporated.diploma.R
import com.alinaincorporated.diploma.databinding.FragmentSplashScreenBinding


class SPlashScreenFragment: Fragment(R.layout.fragment_splash_screen) {

    private val binding: FragmentSplashScreenBinding by viewBinding(
        FragmentSplashScreenBinding::bind
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        

    }
}