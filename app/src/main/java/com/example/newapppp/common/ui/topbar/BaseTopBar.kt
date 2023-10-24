package com.example.newapppp.common.ui.topbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.newapppp.R
import com.example.newapppp.common.ui.theme.HabitTheme
import com.example.newapppp.common.ui.theme.bold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseTopBar(
    title: String?,
    backActionClick: (() -> Unit)? = null,
    openMenuActionClick: (() -> Unit)? = null
) {
    Box {
        TopAppBar(
            colors = BaseTopBarDefaults.topAppBarColors(),
            title = {
                Text(
                    text = title ?: "",
                    maxLines = 1,
                    style = HabitTheme.typography.titleMedium.bold,
                    overflow = TextOverflow.Ellipsis
                )
            },
            navigationIcon = {
                backActionClick?.let {
                    IconButton(
                        onClick = backActionClick
                    ) {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            painter = painterResource(id = R.drawable.ic_arrow_left),
                            tint = HabitTheme.colors.onPrimary,
                            contentDescription = null
                        )
                    }
                }
                openMenuActionClick?.let {
                    IconButton(
                        onClick = openMenuActionClick
                    ) {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            painter = painterResource(id = R.drawable.ic_menu),
                            tint = HabitTheme.colors.onPrimary,
                            contentDescription = null)
                    }
                }
            }
        )
    }
}

@Preview
@Composable
fun BaseTopBarPreview() {
    BaseTopBar(null)
}