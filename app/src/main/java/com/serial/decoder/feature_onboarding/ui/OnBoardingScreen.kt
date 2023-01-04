package com.serial.decoder.feature_onboarding.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.serial.decoder.R
import com.serial.decoder.core.ui.*
import kotlinx.coroutines.flow.collectLatest

const val FIRST_INDEX_PAGE = 0
const val SECOND_INDEX_PAGE = 1
const val THIRD_INDEX_PAGE = 2


@Composable
fun OnBoardingScreen(
    modifier: Modifier = Modifier,
    windowSize: WindowWidthSizeClass,
    viewModel: OnBoardingViewModel = hiltViewModel<OnBoardingViewModel>(),
    onHomeNavigation: () -> Unit
) {
    val uiState: State<OnBoardingUIState> = viewModel.uiState.collectAsState()

    val scrollingType = when (windowSize) {
        WindowWidthSizeClass.Compact -> OnBoardingScrollType.BOTTOM_NAV
        WindowWidthSizeClass.Medium -> OnBoardingScrollType.NO_BOTTOM
        WindowWidthSizeClass.Expanded -> OnBoardingScrollType.NO_BOTTOM
        else -> OnBoardingScrollType.BOTTOM_NAV
    }

    val canClickGetStarted: Boolean = uiState.value.isLoading == false
    if (scrollingType == OnBoardingScrollType.BOTTOM_NAV)
        HorizontalPagerLayout(
            modifier = modifier,
            scrollingType,
            canClickGetStarted,
            onGetStartedButtonClicked = handleOnGetStartedButtonClicked(viewModel)
        )
    else
        VerticalLayout(
            modifier = modifier,
            scrollingType,
            canClickGetStarted,
            onGetStartedButtonClicked = handleOnGetStartedButtonClicked(viewModel)
        )

    LaunchedEffect(key1 = true)
    {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is OnBoardingUIEvents.UpdateFirstTime -> onHomeNavigation()
            }
        }
    }
}

@Composable
private fun handleOnGetStartedButtonClicked(viewModel: OnBoardingViewModel): () -> Unit =
    { viewModel.updateFirstTime() }

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HorizontalPagerLayout(
    modifier: Modifier = Modifier,
    scrollType: OnBoardingScrollType,
    canClickGetStarted: Boolean,
    onGetStartedButtonClicked: () -> Unit
) {
    HorizontalPager(modifier = modifier, count = 3) { page ->
        when (page) {
            FIRST_INDEX_PAGE -> OnBoardingContent(
                imageResId = R.drawable.onboarding_image_one,
                contentDesResId = R.string.onboarding_select_brand_image,
                headerTextResId = R.string.select_a_brand,
                subTitleTextResId = R.string.onboarding_message_one,
                selectedPage = FIRST_INDEX_PAGE,
                scrollType = scrollType
            )
            SECOND_INDEX_PAGE -> OnBoardingContent(
                imageResId = R.drawable.onboarding_image_two,
                contentDesResId = R.string.onboarding_insert_serial_number_image,
                headerTextResId = R.string.insert_serial_number,
                subTitleTextResId = R.string.onboarding_message_two,
                selectedPage = SECOND_INDEX_PAGE, scrollType = scrollType

            )
            THIRD_INDEX_PAGE -> OnBoardingContent(
                imageResId = R.drawable.onboarding_image_three,
                contentDesResId = R.string.onboarding_view_details_about_device_image,
                headerTextResId = R.string.view_and_read,
                subTitleTextResId = R.string.onboarding_message_three,
                canClickGetStarted = canClickGetStarted,
                selectedPage = THIRD_INDEX_PAGE, scrollType = scrollType,
                onGetStartedButtonClicked = onGetStartedButtonClicked
            )
        }
    }
}

@Composable
fun VerticalLayout(
    modifier: Modifier = Modifier,
    scrollType: OnBoardingScrollType,
    canClickGetStarted: Boolean,
    onGetStartedButtonClicked: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {
        OnBoardingContent(
            imageResId = R.drawable.onboarding_image_one,
            contentDesResId = R.string.onboarding_select_brand_image,
            headerTextResId = R.string.select_a_brand,
            subTitleTextResId = R.string.onboarding_message_one,
            selectedPage = FIRST_INDEX_PAGE,
            scrollType = scrollType
        )
        GreyDivider()
        OnBoardingContent(
            imageResId = R.drawable.onboarding_image_two,
            contentDesResId = R.string.onboarding_insert_serial_number_image,
            headerTextResId = R.string.insert_serial_number,
            subTitleTextResId = R.string.onboarding_message_two,
            selectedPage = SECOND_INDEX_PAGE, scrollType = scrollType

        )
        GreyDivider()
        OnBoardingContent(
            imageResId = R.drawable.onboarding_image_three,
            contentDesResId = R.string.onboarding_view_details_about_device_image,
            headerTextResId = R.string.view_and_read,
            subTitleTextResId = R.string.onboarding_message_three,
            selectedPage = THIRD_INDEX_PAGE,
            scrollType = scrollType
        )
        GetStartedButton(
            enabled = canClickGetStarted,
            onGetStartedButtonClicked = onGetStartedButtonClicked
        )
    }
}


@Composable
fun OnBoardingContent(
    modifier: Modifier = Modifier,
    @DrawableRes imageResId: Int,
    @StringRes contentDesResId: Int,
    imageSize: Dp = ONBOARDING_IMAGE_SIZE,
    @StringRes headerTextResId: Int,
    @StringRes subTitleTextResId: Int,
    selectedPage: Int,
    scrollType: OnBoardingScrollType,
    canClickGetStarted: Boolean = false,
    onGetStartedButtonClicked: () -> Unit = {}
) {
    if (scrollType == OnBoardingScrollType.BOTTOM_NAV) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OnBoardingImage(
                imageResId = imageResId,
                contentDesResId = contentDesResId,
                size = imageSize,
                scrollType = scrollType
            )
            OnBoardingHeader(resId = headerTextResId)
            OnBoardingSubTitle(resId = subTitleTextResId)
            Spacer(modifier = modifier.weight(1f))
            if (selectedPage == THIRD_INDEX_PAGE)
                GetStartedButton(
                    enabled = canClickGetStarted,
                    onGetStartedButtonClicked = onGetStartedButtonClicked
                )
            else
                NavigationDots(currentPage = selectedPage)

        }
    } else {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OnBoardingImage(
                imageResId = imageResId,
                contentDesResId = contentDesResId,
                size = imageSize,
                scrollType = scrollType
            )
            Column(
                modifier = modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OnBoardingHeader(resId = headerTextResId)
                OnBoardingSubTitle(resId = subTitleTextResId)
            }
        }
    }
}

@Composable
fun OnBoardingImage(
    modifier: Modifier = Modifier,
    @DrawableRes imageResId: Int,
    @StringRes contentDesResId: Int,
    size: Dp,
    scrollType: OnBoardingScrollType,
) {
    val topPadding =
        if (scrollType == OnBoardingScrollType.BOTTOM_NAV) ONBOARDING_IMAGE_SPACING else DOUBLE_SPACING
    Image(
        modifier = modifier
            .padding(top = topPadding, bottom = DOUBLE_SPACING, start = DOUBLE_SPACING)
            .size(size),
        contentScale = ContentScale.Crop,
        painter = painterResource(id = imageResId),
        contentDescription = stringResource(id = contentDesResId)
    )
}

@Composable
fun OnBoardingHeader(modifier: Modifier = Modifier, @StringRes resId: Int) {
    Text(
        modifier = modifier.padding(horizontal = QUINRIPLE_SPACING, vertical = NORMAL_SPACING),
        textAlign = TextAlign.Center,
        text = stringResource(id = resId),
        style = MaterialTheme.typography.h1
    )
}

@Composable
fun OnBoardingSubTitle(modifier: Modifier = Modifier, @StringRes resId: Int) {
    Text(
        modifier = modifier.padding(horizontal = QUADRIPLE_SPACING),
        text = stringResource(id = resId),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.body1
    )
}

@Composable
fun NavigationDots(modifier: Modifier = Modifier, currentPage: Int) {

    Row(
        modifier = modifier.padding(bottom = QUINRIPLE_SPACING),
        horizontalArrangement = Arrangement.spacedBy(TRIPLE_SPACING)
    ) {
        Dot(selected = currentPage == FIRST_INDEX_PAGE)
        Dot(selected = currentPage == SECOND_INDEX_PAGE)
        Dot(selected = currentPage == THIRD_INDEX_PAGE)
    }
}

@Composable
fun Dot(modifier: Modifier = Modifier, selected: Boolean) {
    val color =
        if (selected) MaterialTheme.colors.primary else MaterialTheme.colors.secondaryVariant

    Box(
        modifier = modifier
            .size(DOT_SIZE)
            .clip(CircleShape)
            .background(color = color)
    )
}

@Composable
fun GetStartedButton(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    onGetStartedButtonClicked: () -> Unit
) {
    NormalButton(
        modifier = modifier
            .fillMaxWidth()
            .padding(DOUBLE_SPACING),
        enabled = enabled,
        onClick = onGetStartedButtonClicked
    ) {
        Text(text = stringResource(id = R.string.get_started))
    }
}