package com.serial.decoder.feature_decoding.ui.analyzer

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

class BarcodeImageAnalyzer(
    val onBarcodeFound: (barcodes: List<Barcode>) -> Unit,
    val onBarcodeNotFound: () -> Unit,
    val onBarcodeFailed: (exception: java.lang.Exception) -> Unit,
) : ImageAnalysis.Analyzer {
    var lastScanTime: Long = 0

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val currentScanTime = System.currentTimeMillis()
        val canScan = currentScanTime - lastScanTime >= 1000
        if(canScan)
        {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            val options = buildBarcodeScannerOptions()
            val scanner = BarcodeScanning.getClient(options)

            val result = scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    if (barcodes.isNotEmpty()) onBarcodeFound(barcodes)
                    else onBarcodeNotFound()
                }
                .addOnFailureListener { exception ->
                    onBarcodeFailed(exception)
                }.addOnCompleteListener {
                    imageProxy.close()
                    lastScanTime = currentScanTime
                }
        }
        }else{
            imageProxy.close()
        }
    }


    fun buildBarcodeScannerOptions(): BarcodeScannerOptions {
        return BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()
    }

}
