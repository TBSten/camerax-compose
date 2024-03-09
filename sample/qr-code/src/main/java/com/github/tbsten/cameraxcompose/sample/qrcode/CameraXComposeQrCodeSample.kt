package com.github.tbsten.cameraxcompose.sample.qrcode

import android.graphics.Bitmap
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.github.tbsten.cameraxcompose.CameraPreview
import com.github.tbsten.cameraxcompose.usecasehelper.imageAnalysisUseCase
import com.github.tbsten.cameraxcompose.usecasehelper.previewUseCase
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.Executor

@Composable
internal fun CameraXComposeQrCodeSample() {
    val context = LocalContext.current
    DetectQrCodeCameraPreview(
        onDetectQrCode = { result ->
            if (result.barcodes.isEmpty()) return@DetectQrCodeCameraPreview
            val scanText = result.barcodes[0].displayValue ?: return@DetectQrCodeCameraPreview
            Toast.makeText(context, scanText, Toast.LENGTH_SHORT).show()
        },
        modifier =
            Modifier
                .fillMaxSize(),
    )
}

@Composable
private fun DetectQrCodeCameraPreview(
    onDetectQrCode: (BarcodeScanResult) -> Unit,
    modifier: Modifier = Modifier,
) {
    CameraPreview(
        onBind = {
            val executor = ContextCompat.getMainExecutor(context)
            // UseCases
            val preview = previewUseCase()
            val analysis =
                barcodeScanUseCase(
                    executor = executor,
                    onSuccess = onDetectQrCode,
                    onFailure = {
                        Toast.makeText(context, "${it.message}", Toast.LENGTH_SHORT).show()
                    },
                )
            // bind
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                analysis,
            )
        },
        modifier = modifier,
    )
}

@OptIn(ExperimentalGetImage::class)
private fun barcodeScanUseCase(
    executor: Executor,
    onSuccess: (BarcodeScanResult) -> Unit,
    onFailure: (Exception) -> Unit,
): ImageAnalysis {
    val barcodeScanner =
        BarcodeScanning
            .getClient(
                BarcodeScannerOptions.Builder()
                    .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                    .build(),
            )
    val analysis =
        imageAnalysisUseCase(
            executor = executor,
        ) { proxy ->
            val image = proxy.image
            if (image == null) {
                proxy.close()
                return@imageAnalysisUseCase
            }
            val inputImage = InputImage.fromMediaImage(image, proxy.imageInfo.rotationDegrees)
            val bitmap = proxy.toBitmap()
            barcodeScanner.process(inputImage)
                .addOnSuccessListener {
                    onSuccess(
                        BarcodeScanResult(
                            image = bitmap,
                            barcodes = it,
                        ),
                    )
                }
                .addOnFailureListener(onFailure)
                .addOnCompleteListener { proxy.close() }
        }
    return analysis
}

data class BarcodeScanResult(
    val image: Bitmap,
    val barcodes: List<Barcode>,
)
