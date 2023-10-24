package com.example.newapppp.presentation.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.lifecycle.lifecycleScope
import androidx.navigation.FloatingWindow
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.newapppp.R
import com.example.newapppp.common.ui.theme.HabitTheme
import com.example.newapppp.common.ui.topbar.BaseTopBar
import com.example.newapppp.databinding.FragmentContainerBinding
import com.example.newapppp.databinding.LayoutComposeBinding
import com.example.newapppp.presentation.util.setContentWithTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.layout_compose) {
    private var appBarConfiguration: AppBarConfiguration? = null
    private var navController: NavController? = null
    private val binding: LayoutComposeBinding by viewBinding(LayoutComposeBinding::bind)
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.root.setContentWithTheme {
            MainScreen()
        }

        viewModel.state.onEach { state ->
            if (state.connected) {
                viewModel.makeRequestForDataVerification()
            }
        }.launchIn(lifecycleScope)

    }


    @Composable
    fun MainScreen() {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                DrawerMenu()
            }
        ) {
            Scaffold(
                topBar = {
                    BaseTopBar(
                        title = stringResource(id = R.string.habits_string),
                        openMenuActionClick =
                        {
                            scope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        }
                    )
                },
                containerColor = HabitTheme.colors.background
            ) { padding ->
                Column(
                    modifier = Modifier.clickable {
                        navController?.navigate()
                    }
                ) {
                    AndroidViewBinding(factory = { inflater, parent, attachToParent ->
                        FragmentContainerBinding.inflate(inflater, parent, attachToParent).also {
                            setupNavigationListener()
                        }
                    }
                    )
                }
            }
        }
    }

    @Composable
    fun DrawerMenu() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(HabitTheme.dimensions.paddingBig)
        ) {
            DrawerHeader()
            Divider(modifier = Modifier.fillMaxWidth())

            NavigationDrawerItem(
                label = {
                    Text(
                        text = stringResource(id = R.string.home_page_title)
                    )
                },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.main_navigation_icon),
                        contentDescription = stringResource(id = R.string.info_about_app_icon_string)
                    )
                },
                selected = false,
                shape = CutCornerShape(HabitTheme.dimensions.boxRadius),
                onClick = { navController?.navigate("nav_app_info_fragment") }
            )

            NavigationDrawerItem(
                label = {
                    Text(
                        text = stringResource(id = R.string.info_about_app)
                    )
                },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.about_app_icon),
                        contentDescription = stringResource(id = R.string.home_page_title_icon_string)
                    )
                },
                selected = false,
                shape = CutCornerShape(HabitTheme.dimensions.boxRadius),
                onClick = { navController?.navigate("nav_home_fragment") }
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(HabitTheme.colors.background)
            )
        }
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun DrawerHeader() {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = HabitTheme.colors.onAvatarBackground
                )
        ) {
            Column(
                modifier = Modifier
                    .padding(HabitTheme.dimensions.paddingHuge)
            ) {
                Box(
                    modifier = Modifier
                        .size(HabitTheme.dimensions.profile)
                        .background(
                            shape = CircleShape,
                            color = HabitTheme.colors.background
                        )
                ) {
                    GlideImage(
                        model = "https://free-png.ru/wp-content/uploads/2022/07/free-png.ru-95.png",
                        loading = placeholder(R.drawable.loading_profile_icon),
                        failure = placeholder(R.drawable.wifi_tethering),
                        contentDescription = getString(R.string.profile_description_string),
                    )
                }
                Spacer(modifier = Modifier.height(HabitTheme.dimensions.spacer))
                Text(
                    text = stringResource(id = R.string.user_name_string),
                    style = HabitTheme.typography.titleLarge
                )
                Text(
                    text = stringResource(id = R.string.about_me_string),
                    style = HabitTheme.typography.titleSmall
                )
            }
        }
    }

    private fun setupNavigationListener() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.navController
        navController?.addOnDestinationChangedListener { controller, destination, _ ->
            if (destination !is FloatingWindow) {
                val navigationBarItem = when (destination.id) {
                    R.id.nav_app_info_fragment -> {}
                    R.id.nav_redactor_fragment -> {}
                    R.id.nav_color_choose_dialog -> {}
                    R.id.nav_filter_dialog -> {}
                    R.id.nav_home_fragment -> {}
                    else -> null
                }
                viewModel.onNavDestinationUpdated(navigationBarItem, controller)
            }
        }
    }

    @Preview
    @Composable
    fun DrawerMenuPreview() {
        DrawerMenu()
    }
}
