package com.example.newapppp.common.ui.element.button

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import com.example.newapppp.R
import com.example.newapppp.common.ui.theme.HabitTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseButton(
    modifier: Modifier = Modifier,
    @StringRes textStringId: Int? = null,
    elevated: Boolean = true,
    isEnabled: Boolean = true,
    rounded: Boolean = true,
    colors: ButtonColors = BaseButtonDefaults.mainButtonColors,
    onClick: () -> Unit,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
) {
    CompositionLocalProvider(
        LocalMinimumInteractiveComponentEnforcement provides false
    ) {
        Button(
            modifier = modifier.fillMaxWidth(),
            onClick = onClick,
            colors = colors,
            shape = BaseButtonDefaults.getButtonShape(rounded),
            elevation = BaseButtonDefaults.getButtonElevation(elevated),
            enabled = isEnabled,
            contentPadding = contentPadding
        ) {
            Text(
                text = stringResource(textStringId ?: R.string.empty_string),
                style = HabitTheme.typography.titleMedium
            )
        }
    }
}