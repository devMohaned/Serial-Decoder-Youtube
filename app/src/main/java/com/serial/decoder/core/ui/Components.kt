package com.serial.decoder.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.serial.decoder.ui.theme.CornerButtonShape


@Composable
fun GreyDivider(modifier: Modifier = Modifier) {
    Divider(
        modifier = modifier
            .fillMaxWidth()
            .height(DIVIDER_HEIGHT)
            .background(MaterialTheme.colors.secondary)
    )
}

@Composable
fun NormalButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Button(modifier = modifier.clip(CornerButtonShape), enabled = enabled, onClick = onClick) {
        content()
    }
}