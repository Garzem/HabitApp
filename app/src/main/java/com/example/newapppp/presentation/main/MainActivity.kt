package com.example.newapppp.presentation.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.FloatingWindow
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.example.newapppp.R
import com.example.newapppp.common.ui.element.nav_drawer_item.BaseNavDrawerItem
import com.example.newapppp.common.ui.element.drawer.BaseDrawer
import com.example.newapppp.common.ui.theme.HabitTheme
import com.example.newapppp.databinding.FragmentContainerBinding
import com.example.newapppp.databinding.LayoutComposeBinding
import com.example.newapppp.presentation.main.state.MainEvent
import com.example.newapppp.presentation.redactor.RedactorFragmentDirections.Companion.actionNavAppInfoFragment
import com.example.newapppp.presentation.redactor.RedactorFragmentDirections.Companion.actionNavHomeFragment
import com.example.newapppp.presentation.util.setContentWithTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.layout_compose), DrawerHost {
    private var navController: NavController? = null
    private val binding: LayoutComposeBinding by viewBinding(LayoutComposeBinding::bind)
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.root.setContentWithTheme {
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

            val state by viewModel.state.collectAsStateWithLifecycle()
            MainScreen(drawerState = drawerState, state.navItem)

            val event by viewModel.event.collectAsStateWithLifecycle()
            LaunchedEffect(event) {
                when (event) {
                    MainEvent.OpenDrawer -> {
                        if (drawerState.isClosed) {
                            drawerState.open()
                        }
                    }

                    MainEvent.CloseDrawer -> {
                        if (drawerState.isOpen) {
                            drawerState.close()
                        }
                    }

                    else -> return@LaunchedEffect
                }
                viewModel.consumeEvents()
            }

        }

        viewModel.state.onEach { state ->
            if (state.connected) {
                viewModel.makeRequestForDataVerification()
            }
        }.launchIn(lifecycleScope)

    }


    @Composable
    private fun MainScreen(drawerState: DrawerState, selectedNavItem: NavItem?) {
        BaseDrawer(
            drawerState = drawerState,
            drawerMenu = {
                DrawerMenu(selectedNavItem)
            }
        ) {
            AndroidViewBinding(
                factory = { inflater, parent, attachToParent ->
                    FragmentContainerBinding.inflate(inflater, parent, attachToParent).also {
                        setupNavigationListener()
                    }
                }
            )
        }
    }

    @Composable
    private fun DrawerMenu(navItem: NavItem?) {
        ModalDrawerSheet(
            drawerContainerColor = HabitTheme.colors.background,
            windowInsets = WindowInsets(0, 0, 0, 0)
        ) {
            Column {
                DrawerHeader()

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .background(HabitTheme.colors.background)
                ) {
                    BaseNavDrawerItem(
                        labelStringId = R.string.home_page_title,
                        iconId = R.drawable.main_navigation_icon,
                        contentIconStringId = R.string.home_page_title_icon_string,

                        selected = navItem == NavItem.HOME,
                        shape = CutCornerShape(HabitTheme.dimensions.boxRadius),
                        onClick = { navController?.navigate(actionNavHomeFragment()) }
                    )

                    BaseNavDrawerItem(
                        labelStringId = R.string.info_about_app,
                        iconId = R.drawable.about_app_icon,
                        contentIconStringId = R.string.info_about_app_icon_string,

                        selected = navItem == NavItem.APP_INFO,
                        shape = CutCornerShape(HabitTheme.dimensions.boxRadius),
                        onClick = { navController?.navigate(actionNavAppInfoFragment()) }
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    private fun DrawerHeader() {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = HabitTheme.colors.onProfileBackground
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
//                    GlideImage(
//                        model = "https://free-png.ru/wp-content/uploads/2022/07/free-png.ru-95.png",
//                        loading = placeholder(R.drawable.loading_profile_icon),
//                        failure = placeholder(R.drawable.wifi_tethering),
//                        contentDescription = getString(R.string.profile_description_string),
//                    )
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
                    R.id.nav_home_fragment -> NavItem.HOME
                    R.id.nav_app_info_fragment -> NavItem.APP_INFO
                    else -> null
                }
                viewModel.onNavDestinationUpdated(navigationBarItem)
            }
        }
    }

    @Preview
    @Composable
    private fun DrawerMenuPreview() {
        DrawerMenu(NavItem.HOME)
    }

    override fun openDrawer() {
        viewModel.openDrawer()
    }
}
