package com.serial.decoder.feature_decoding.ui.home


import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.serial.decoder.R
import com.serial.decoder.core.ui.BOTTOM_SHEET_PEEK_HEIGHT
import com.serial.decoder.core.ui.DOUBLE_SPACING
import com.serial.decoder.core.ui.NORMAL_SPACING
import com.serial.decoder.core.ui.ZERO_DP
import com.serial.decoder.feature_decoding.data.local.entity.Brands
import com.serial.decoder.feature_decoding.data.local.entity.ManufactureEntity
import com.serial.decoder.feature_decoding.ui.*
import com.serial.decoder.feature_decoding.ui.dialog.BrandsAlertDialog
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


object HomeDeepLinking {
    const val SCHEME = "HTTPS"
    const val HOST = "www.example.com"
    const val ARG_1_SERIAL = "serial"
    const val ARG_2_BRAND = "brand"
    const val ARGS = "$ARG_1_SERIAL={$ARG_1_SERIAL}&$ARG_2_BRAND={$ARG_2_BRAND}"
    const val URI = "$SCHEME://$HOST?$ARGS"
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    serial: String? = null,
    brand: String? = null,
    onFAQClicked: () -> Unit,
) {
    val viewModel: HomeViewModel = hiltViewModel()
    val scope = rememberCoroutineScope()
    val forCollection = viewModel.serialWithBrandFlow.collectAsState(initial = listOf())
    val uiState: State<HomeUIState> = viewModel.uiState.collectAsState()
    val bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetState)
    val isDialogVisible = rememberSaveable {
        mutableStateOf(false)
    }


    HandleDeepLinking(serial, brand,
        onBrandUpdate = { viewModel.updateBrand(brand!!) },
        onSerialUpdate = { viewModel.updateSerial(serial!!) })

    HandleUIEvents(isDialogVisible, viewModel.uiEvent, bottomSheetScaffoldState)
    HandleDialog(isDialogVisible.value,
        onBrandClicked = { viewModel.updateBrand(it) },
        onDismissClicked = { isDialogVisible.value = false })

    if (uiState.value.isLoading) {
        LoadingBox()
    } else {
        BottomSheetScaffold(
            scaffoldState = bottomSheetScaffoldState,
            modifier = modifier,
            topBar = {
                HomeAppBar(title = uiState.value.brand, onBrandButtonClicked = {
                    isDialogVisible.value = !isDialogVisible.value
                    viewModel.showDialog()
                }, onFAQButtonClicked = onFAQClicked)
            },
            sheetContent = {
                HomeBottomSheet(
                    bottomSheetIsExpanded = bottomSheetState.isExpanded,
                    onBarcodeFound = { barcodes ->
                        barcodes.first().rawValue?.let { viewModel.updateSerial(it) }
                        scope.launch { bottomSheetState.collapse() }
                    },
                    onBarcodeFailed = { it.localizedMessage?.let { it1 -> viewModel.showSnackBar(it1) } },
                    onBarcodeNotFound = { /* Do not do anything */}
                )
            },
            sheetBackgroundColor = Color.Transparent,
            sheetElevation = ZERO_DP,
            sheetPeekHeight = BOTTOM_SHEET_PEEK_HEIGHT,
        ) {
            HomeContent(
                value = uiState.value.serial,
                manufactureEntity = uiState.value.manufactureEntity,
                onValueChange = { serial ->
                    viewModel.updateSerial(serial)
                })
        }
    }
}

@Composable
private fun HandleDialog(
    isDialogVisible: Boolean,
    brands: Array<Brands> = Brands.values(),
    onBrandClicked: (string: String) -> Unit,
    onDismissClicked: () -> Unit
) {
    if (isDialogVisible) {
        BrandsAlertDialog(
            brands = brands,
            onBrandClicked = onBrandClicked, onDismissRequest = onDismissClicked
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun HandleUIEvents(
    isDialogVisible: MutableState<Boolean>,
    uiEvent: SharedFlow<HomeUIEvent>,
    bottomSheetScaffoldState: BottomSheetScaffoldState
) {
    LaunchedEffect(key1 = isDialogVisible)
    {
        uiEvent.collectLatest { event ->
            when (event) {
                is HomeUIEvent.ShowDialog -> isDialogVisible.value = true
                is HomeUIEvent.ShowSnackBar -> bottomSheetScaffoldState.snackbarHostState.showSnackbar(
                    event.message
                )
            }
        }
    }
}

@Composable
private fun HandleDeepLinking(
    serial: String?,
    brand: String?,
    onSerialUpdate: (serial: String) -> Unit,
    onBrandUpdate: (serial: String) -> Unit,
) {
    LaunchedEffect(key1 = true)
    {
        val isDeepLink =
            serial != null && serial.isNotEmpty()
                    && brand != null && brand.isNotEmpty()
                    && Brands.values().any { it.name == brand }

        if (isDeepLink) {
            onSerialUpdate(serial!!)
            onBrandUpdate(brand!!)
        }
    }
}


@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    value: String,
    manufactureEntity: ManufactureEntity,
    onValueChange: (string: String) -> Unit
) {
    Column(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceEvenly) {
        ManufactureItem(
            drawableResId = R.drawable.ic_manufactored_type,
            contentDescriptionStringResId = R.string.manufactored_type_image,
            title = stringResource(id = R.string.type_with_value, manufactureEntity.type.asString())
        )
        ManufactureItem(
            drawableResId = R.drawable.ic_manufactor_location,
            contentDescriptionStringResId = R.string.manufactored_location_image,
            title = stringResource(id = R.string.made_in, manufactureEntity.country.asString())
        )
        ManufactureItem(
            drawableResId = R.drawable.ic_manufactored_date,
            contentDescriptionStringResId = R.string.manufactored_date_image,
            title = manufactureEntity.date.asString()
        )

        SerialTextField(
            value = value,
            onValueChange = onValueChange
        )
    }

}

@Composable
fun ManufactureItem(
    modifier: Modifier = Modifier,
    @DrawableRes drawableResId: Int,
    @StringRes contentDescriptionStringResId: Int,
    title: String
) {
    Row(
        modifier = modifier
            .padding(horizontal = NORMAL_SPACING)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ManufactureImage(
            modifier = modifier.weight(0.3f),
            drawableResId = drawableResId,
            contentDescriptionStringResId = contentDescriptionStringResId
        )
        ManufactureTitle(
            modifier = modifier.weight(0.5f),
            title = title
        )
    }
}

@Composable
fun ManufactureImage(
    modifier: Modifier = Modifier,
    @DrawableRes drawableResId: Int,
    @StringRes contentDescriptionStringResId: Int
) {
    Image(
        modifier = modifier,
        painter = painterResource(id = drawableResId),
        contentDescription = stringResource(id = contentDescriptionStringResId),
    )
}


@Composable
fun ManufactureTitle(
    modifier: Modifier = Modifier,
    title: String
) {
    Text(
        modifier = modifier,
        text = title, style = MaterialTheme.typography.h2, textAlign = TextAlign.Center
    )
}

@Composable
fun SerialTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (string: String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier
            .padding(horizontal = DOUBLE_SPACING)
            .fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = stringResource(id = R.string.insert_serial_number)) })
}


@Composable
fun LoadingBox(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center)
    {
        CircularProgressIndicator()
    }
}