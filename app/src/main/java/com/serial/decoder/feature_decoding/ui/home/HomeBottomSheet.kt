@file:OptIn(ExperimentalPermissionsApi::class)

package com.serial.decoder.feature_decoding.ui.home

import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.mlkit.vision.barcode.common.Barcode
import com.serial.decoder.R
import com.serial.decoder.core.ui.*
import com.serial.decoder.core.util.HandlePermissionState
import com.serial.decoder.feature_decoding.ui.analyzer.BarcodeImageAnalyzer
import java.util.concurrent.Executors


@Composable
fun HomeBottomSheet(
    modifier: Modifier = Modifier,
    bottomSheetIsExpanded: Boolean,
    onBarcodeFound: (barcodes: List<Barcode>) -> Unit,
    onBarcodeFailed: (exception: java.lang.Exception) -> Unit,
    onBarcodeNotFound: () -> Unit
) {
    HomeBottomSheetContent(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.8f), bottomSheetIsExpanded = bottomSheetIsExpanded,
        onBarcodeFound = onBarcodeFound,
        onBarcodeFailed = onBarcodeFailed,
        onBarcodeNotFound = onBarcodeNotFound
    )
}

@Composable
fun HomeBottomSheetContent(
    modifier: Modifier = Modifier,
    bottomSheetIsExpanded: Boolean,
    onBarcodeFound: (barcodes: List<Barcode>) -> Unit,
    onBarcodeFailed: (exception: java.lang.Exception) -> Unit,
    onBarcodeNotFound: () -> Unit
) {
    Column {
        PeekBox()
        SerialScanText()
        if (bottomSheetIsExpanded)
            CameraBox(
                modifier = modifier, onBarcodeFound = onBarcodeFound,
                onBarcodeFailed = onBarcodeFailed,
                onBarcodeNotFound = onBarcodeNotFound
            )
        else
            EmptyBox(modifier = modifier)
    }
}

@Composable
fun PeekBox(modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        NonDraggableBox(modifier = modifier.weight(1f))
        QRBox()

    }
}

@Composable
fun NonDraggableBox(modifier: Modifier = Modifier) {
    Box(modifier = modifier
        .height(BOTTOM_SHEET_PEEK_HEIGHT)
        .background(color = Color.Transparent)
        .draggable(orientation = Orientation.Vertical, state = rememberDraggableState {
            // We do not want to with this NON-draggable box
        }
        )
    )
}

@Composable
fun QRBox(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(end = QUADRIPLE_SPACING)
            .size(BOTTOM_SHEET_PEEK_HEIGHT)
            .clip(RoundedCornerShape(topStart = TRIPLE_SPACING, topEnd = TRIPLE_SPACING))
            .background(MaterialTheme.colors.secondary), contentAlignment = Alignment.BottomCenter
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_qr_code),
            contentDescription = stringResource(
                id = R.string.bottom_sheet_puller
            ), tint = Color.Unspecified
        )
    }

}

@Composable
fun SerialScanText(modifier: Modifier = Modifier) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface)
            .padding(
                start = DOUBLE_SPACING,
                end = DOUBLE_SPACING,
                bottom = NORMAL_SPACING,
                top = NORMAL_SPACING
            ),
        text = stringResource(id = R.string.scan_serial_with_qr),
        style = MaterialTheme.typography.h3
    )
}

@Composable
fun EmptyBox(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(color = MaterialTheme.colors.secondaryVariant)
            .fillMaxWidth()
            .fillMaxHeight(0.8f)
    )
}

@Composable
fun CameraContent(
    modifier: Modifier = Modifier,
    onBarcodeFound: (barcodes: List<Barcode>) -> Unit,
    onBarcodeFailed: (exception: java.lang.Exception) -> Unit,
    onBarcodeNotFound: () -> Unit
) {
    val context = LocalContext.current
    val lifeCycleOwner = LocalLifecycleOwner.current
    var cameraProvider: ProcessCameraProvider? = null

    DisposableEffect(key1 = cameraProvider)
    {
        onDispose {
            cameraProvider?.let { it.unbindAll() } // We close the camera after de-composition
        }
    }


    AndroidView(modifier = modifier.fillMaxSize(), factory = { androidViewContext ->
        val previewView: PreviewView = PreviewView(androidViewContext)
        previewView.implementationMode = PreviewView.ImplementationMode.COMPATIBLE
        previewView
    }, update = { androidPreviewView: PreviewView ->

        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        val cameraExecutor = Executors.newSingleThreadExecutor()
        val barcodeImageAnalyzer = BarcodeImageAnalyzer(
            onBarcodeNotFound = onBarcodeNotFound,
            onBarcodeFailed = onBarcodeFailed,
            onBarcodeFound = onBarcodeFound
        )

        cameraProviderFuture.addListener(Runnable {
            cameraProvider = cameraProviderFuture.get()

            val previewUseCase = buildPreviewUseCase(androidPreviewView.surfaceProvider)
            val imageAnalysisUseCase = ImageAnalysis.Builder()
                .setBackpressureStrategy(STRATEGY_KEEP_ONLY_LATEST)
                .build()
            imageAnalysisUseCase.setAnalyzer(cameraExecutor, barcodeImageAnalyzer)


            val cameraSelector = buildCameraSelectorBackLens()



            cameraProvider?.let {
                it.unbindAll() // Release all previous usecases

                val camera = it.bindToLifecycle(
                    lifeCycleOwner, cameraSelector, previewUseCase, imageAnalysisUseCase
                )
            }


        }, ContextCompat.getMainExecutor(context))


    })
}

fun buildPreviewUseCase(surfaceProvider: Preview.SurfaceProvider): Preview {
    return Preview.Builder().build().apply {
        setSurfaceProvider(surfaceProvider)
    }
}

fun buildCameraSelectorBackLens(): CameraSelector {
    return CameraSelector.Builder()
        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
        .build()
}

@Composable
fun CameraBox(
    modifier: Modifier = Modifier, onBarcodeFound: (barcodes: List<Barcode>) -> Unit,
    onBarcodeFailed: (exception: java.lang.Exception) -> Unit,
    onBarcodeNotFound: () -> Unit
) {
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                cameraPermissionState.launchPermissionRequest()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }


    cameraPermissionState.HandlePermissionState(
        GrantedPermissionContent = {
            CameraContent(
                modifier = modifier,
                onBarcodeFound = onBarcodeFound,
                onBarcodeFailed = onBarcodeFailed,
                onBarcodeNotFound = onBarcodeNotFound
            )
        },
        DeniedOncePermissionContent = {
            DenialOnceContent(
                modifier = modifier
                    .background(MaterialTheme.colors.surface),
                cameraPermissionState = cameraPermissionState
            )
        }, DeniedPermanentlyContent = {
            PermanentDeniedContent(
                modifier = modifier
                    .background(MaterialTheme.colors.surface)
            )
        }
    )
}

@Composable
fun PermanentDeniedContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier, contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(
                id = R.string.x_permission_is_denied, stringResource(
                    id = R.string.camera
                )
            ), style = MaterialTheme.typography.h1,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun DenialOnceContent(modifier: Modifier = Modifier, cameraPermissionState: PermissionState) {
    Column(
        modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(
                id = R.string.x_permission_is_required_for_scanning_qr,
                stringResource(id = R.string.camera)
            ),
            style = MaterialTheme.typography.h1,
            textAlign = TextAlign.Center
        )

        PermissionRequestButton(permissionState = cameraPermissionState)
    }
}

@Composable
fun PermissionRequestButton(
    modifier: Modifier = Modifier,
    permissionState: PermissionState
) {
    Button(
        modifier = modifier
            .padding(NORMAL_SPACING)
            .fillMaxWidth(),
        onClick = { permissionState.launchPermissionRequest() }) {
        Text(
            stringResource(
                id = R.string.request_x_permission,
                stringResource(id = R.string.camera)
            )
        )
    }
}


