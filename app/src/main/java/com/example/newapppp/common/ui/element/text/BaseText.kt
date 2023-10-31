package com.example.newapppp.common.ui.element.text

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.example.newapppp.common.ui.theme.HabitTheme

@Composable
fun BaseText(
    @StringRes textStringId: Int,
    color: Color = HabitTheme.colors.onBackground,
    textStyle: TextStyle = HabitTheme.typography.titleMedium,
    textAlign: TextAlign = TextAlign.Center,
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(textStringId),
        color = color,
        style = textStyle,
        textAlign = textAlign,
        modifier = modifier
    )
}