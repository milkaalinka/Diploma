package com.alinaincorporated.diploma

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.alinaincorporated.diploma.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by viewBinding {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initNavigationUI()
    }

    private fun initNavigationUI() {
        val navController = findNavControllerInNavHost()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
    }

    private fun findNavControllerInNavHost() : NavController {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        require(navHostFragment is NavHostFragment) {
            "Nav host fragment is missing"
        }
        return navHostFragment.navController
    }
}