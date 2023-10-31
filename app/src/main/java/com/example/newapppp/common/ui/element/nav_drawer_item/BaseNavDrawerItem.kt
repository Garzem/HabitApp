package com.example.newapppp.common.ui.element.nav_drawer_item

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource

@Composable
fun BaseNavDrawerItem(
    shape: Shape,
    selected: Boolean,
    onClick: () -> Unit,
    @StringRes labelStringId: Int,
    @DrawableRes iconId: Int,
    @StringRes contentIconStringId: Int,
    colors: NavigationDrawerItemColors = BaseNavDrawerItemDefaults.navDrawerColors
) {
    NavigationDrawerItem(
        label = {
            Text(
                text = stringResource(id = labelStringId)
            )
        },
        icon = {
            Icon(
                painter = painterResource(id = iconId),
                contentDescription = stringResource(id = contentIconStringId)
            )
        },
        selected = selected,
        colors = colors,
        shape = shape,
        onClick = {
            onClick()
        }
    )
}