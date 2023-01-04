package com.serial.decoder.feature_faq.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import com.serial.decoder.R
import com.serial.decoder.core.ui.*
import com.serial.decoder.feature_faq.data.PLAY_STORE
import com.serial.decoder.feature_faq.data.YOUTUBE


@Composable
fun FAQScreen(modifier: Modifier = Modifier, onBackButtonPressed: () -> Unit) {
    Scaffold(
        topBar = { TopFAQBar(onBackButtonPressed = onBackButtonPressed) },
        bottomBar = { BottomButtons() }
    ) {
        FAQContent()
    }


}

@Composable
fun TopFAQBar(modifier: Modifier = Modifier, onBackButtonPressed: () -> Unit) {
    TopAppBar(
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.surface,
        navigationIcon = { BackButtonIcon(onBackButtonPressed = onBackButtonPressed) },
        title = { FAQTitle() }
    )
}


@Composable
fun FAQTitle(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(id = R.string.frequently_asked_questions),
        style = MaterialTheme.typography.h3,
        modifier = modifier
    )
}


@Composable
fun BackButtonIcon(modifier: Modifier = Modifier, onBackButtonPressed: () -> Unit) {
    IconButton(modifier = modifier, onClick = onBackButtonPressed) {
        Icon(
            painter = painterResource(id = R.drawable.ic_back), contentDescription = stringResource(
                id = R.string.back_button
            ),
            tint = Color.Unspecified
        )
    }
}


@Composable
fun FAQContent(modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier, contentPadding = PaddingValues(NORMAL_SPACING)) {
        item {
            Question(
                question = stringResource(id = R.string.faq_1),
                answer = stringResource(id = R.string.ans_1)
            )
        }
        item {
            Question(
                question = stringResource(id = R.string.faq_2),
                answer = stringResource(id = R.string.ans_2)
            )
        }
        item {
            Question(
                question = stringResource(id = R.string.faq_3),
                answer = stringResource(id = R.string.ans_3)
            )
        }
    }
}

@Composable
fun Question(modifier: Modifier = Modifier, question: String, answer: String) {

    val isExpanded = remember {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = modifier
                .padding(horizontal = QUADRIPLE_SPACING)
                .padding(top = NORMAL_SPACING)
                .fillMaxWidth()
                .background(MaterialTheme.colors.primaryVariant)
                .clickable {
                    isExpanded.value = !isExpanded.value
                },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val painterIcon =
                if (isExpanded.value) painterResource(id = R.drawable.ic_collapse) else painterResource(
                    id = R.drawable.ic_dropdown
                )
            Icon(
                modifier = modifier.padding(NORMAL_SPACING),
                painter = painterIcon,
                contentDescription = stringResource(
                    id = R.string.dropdown_icon
                ), tint = Color.Unspecified
            )

            Text(
                modifier = modifier.padding(NORMAL_SPACING), text = question,
                style = MaterialTheme.typography.body1
            )
        }

        AnimatedVisibility(
            visible = isExpanded.value,
            enter = expandVertically(
                spring(
                    stiffness = Spring.StiffnessLow,
                    visibilityThreshold = IntSize.VisibilityThreshold
                )
            ), exit = shrinkVertically()
        ) {
            Text(
                modifier = modifier.padding(horizontal = QUINRIPLE_SPACING),
                text = answer,
                style = MaterialTheme.typography.subtitle1
            )
        }

    }
}

@Composable
fun BottomButtons() {
    val uriHandler = LocalUriHandler.current
    Column {
        ContactDeveloperButton() {
            uriHandler.openUri(YOUTUBE)
        }
        RateTheAppButton {
            uriHandler.openUri(PLAY_STORE)
        }
    }
}


@Composable
fun ContactDeveloperButton(modifier: Modifier = Modifier, onButtonPressed: () -> Unit) {
    NormalButton(
        modifier = modifier
            .padding(horizontal = DOUBLE_SPACING, vertical = NORMAL_SPACING)
            .fillMaxWidth(), onClick = onButtonPressed
    ) {
        Text(text = stringResource(id = R.string.contact_developer))

    }
}


@Composable
fun RateTheAppButton(modifier: Modifier = Modifier, onButtonPressed: () -> Unit) {
    NormalButton(
        modifier = modifier
            .padding(horizontal = DOUBLE_SPACING, vertical = NORMAL_SPACING)
            .fillMaxWidth(), onClick = onButtonPressed
    ) {
        Text(text = stringResource(id = R.string.rate_the_app))
    }
}
