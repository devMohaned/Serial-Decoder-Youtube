package com.serial.decoder.feature_decoding.ui.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.serial.decoder.R
import com.serial.decoder.core.ui.NORMAL_SPACING


@Composable
fun HomeAppBar(
    modifier: Modifier = Modifier,
    title: String,
    onBrandButtonClicked: () -> Unit,
    onFAQButtonClicked: () -> Unit
) {
    TopAppBar(
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.surface,
        navigationIcon = {
            AppBarIcon()
        },
        title = {
            AppBarTitle(title = title)
        },
        actions = {
            AppMenuActions(onBrandClicked = onBrandButtonClicked, onFAQClicked = onFAQButtonClicked)
        }
    )
}

@Composable
fun AppBarIcon(modifier: Modifier = Modifier) {
    Icon(
        modifier = modifier.padding(NORMAL_SPACING),
        painter = painterResource(id = R.drawable.ic_scanner),
        contentDescription = stringResource(id = R.string.app_icon),
        tint = Color.Unspecified
    )
}

@Composable
fun AppBarTitle(modifier: Modifier = Modifier, title: String) {
    Text(modifier = modifier, text = title, style = MaterialTheme.typography.h3)
}

@Composable
fun AppMenuActions(
    modifier: Modifier = Modifier,
    onBrandClicked: () -> Unit,
    onFAQClicked: () -> Unit
) {
    AppMenuItem(
        modifier = modifier,
        drawableResId = R.drawable.ic_apple,
        stringResId = R.string.change_manufactor,
        onClick = onBrandClicked
    )
    AppMenuItem(
        modifier = modifier,
        drawableResId = R.drawable.ic_info,
        stringResId = R.string.faq_button,
        onClick = onFAQClicked
    )
}

@Composable
fun AppMenuItem(
    modifier: Modifier = Modifier,
    @DrawableRes drawableResId: Int,
    @StringRes stringResId: Int,
    onClick: () -> Unit
) {
    IconButton(modifier = modifier, onClick = onClick) {
        Icon(
            painter = painterResource(id = drawableResId),
            contentDescription = stringResource(id = stringResId),
            tint = Color.Unspecified
        )
    }
}
