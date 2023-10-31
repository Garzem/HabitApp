package com.example.newapppp.presentation.app_info

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.newapppp.R
import com.example.newapppp.common.ui.element.text.BaseText
import com.example.newapppp.common.ui.screen.BaseDrawerScreen
import com.example.newapppp.common.ui.theme.HabitTheme
import com.example.newapppp.databinding.LayoutComposeBinding
import com.example.newapppp.presentation.main.MainActivity
import com.example.newapppp.presentation.util.setContentWithTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppInfoFragment : Fragment(R.layout.layout_compose) {
    private val binding by viewBinding(LayoutComposeBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.setContentWithTheme {
            AppInfoScreen()
        }
    }

    @Composable
    private fun AppInfoScreen() {
        BaseDrawerScreen(
            titleId = R.string.info_about_app,
            onMenuActionClick = {
                (activity as? MainActivity)?.openDrawer()
            }
        ) {
            AppInfoContent()
        }
    }

    @Composable
    private fun AppInfoContent() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(HabitTheme.dimensions.paddingNormal),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.habit_icon),
                contentDescription = stringResource(R.string.icon_of_the_app_title)
            )

            BaseText(textStringId = R.string.achiever_title)
            BaseText(
                textStringId = R.string.version_1_0,
                color = HabitTheme.colors.anotherTextColor,
                textStyle = HabitTheme.typography.titleSmall
            )
            BaseText(
                textStringId = R.string.developers_text_title,
                textStyle = HabitTheme.typography.titleLarge
            )
            BaseText(textStringId = R.string.developer_one_name)
            BaseText(textStringId = R.string.developer_two_name)
        }
    }

    @Preview
    @Composable
    private fun AppInfoScreenPreview() {
        AppInfoScreen()
    }
}