package com.example.newapppp.presentation.main_activity

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.newapppp.R
import com.example.newapppp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val binding: ActivityMainBinding by viewBinding(ActivityMainBinding::bind)
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        viewModel.connectionState.onEach { isConnected ->
            if (isConnected) {
                viewModel.makeRequestForDataVerification()
            }
        }.launchIn(lifecycleScope)


        val toolbar: Toolbar = binding.appBarMain.toolbar

        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val imageView = navView.getHeaderView(0)
            .findViewById<ImageView>(R.id.nav_header_avatar)
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home_fragment, R.id.nav_app_info_fragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        navView.setupWithNavController(navController)

        Glide.with(this)
            .load("https://free-png.ru/wp-content/uploads/2022/07/free-png.ru-95.png")
            .placeholder(R.drawable.wifi_tethering)
            .error(R.mipmap.ic_launcher)
            .into(imageView)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}