package com.rskopyl.tyander.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.rskopyl.tyander.R
import com.rskopyl.tyander.databinding.ActivityMainBinding
import com.rskopyl.tyander.util.collectOnLifecycle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    private var _binding: ActivityMainBinding? = null
    private val binding get() = checkNotNull(_binding)

    private val navController by lazy {
        (supportFragmentManager
            .findFragmentById(R.id.fcv_top_level) as NavHostFragment)
            .navController
    }

    private fun setupUi() = binding.bnvTopLevel.run {
        setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            isVisible = destination.id in topLevelDestinationsIds
        }
    }

    private fun setupObservers() = viewModel.run {
        isNightModeEnabled.collectOnLifecycle(
            lifecycleOwner = this@MainActivity,
            collector = ::showNightMode
        )
    }

    private fun showNightMode(isEnabled: Boolean?) {
        if (isEnabled == null) return
        AppCompatDelegate.setDefaultNightMode(
            if (isEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUi()
        setupObservers()
    }

    override fun onSupportNavigateUp() =
        navController.navigateUp() || super.onSupportNavigateUp()

    private companion object {

        val topLevelDestinationsIds = setOf(
            R.id.matchListFragment,
            R.id.searchFragment,
            R.id.chatListFragment
        )
    }
}