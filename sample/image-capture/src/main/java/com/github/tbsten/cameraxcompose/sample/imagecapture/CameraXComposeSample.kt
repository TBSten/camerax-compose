package com.github.tbsten.cameraxcompose.sample.imagecapture

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.MediaStore
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.github.tbsten.cameraxcompose.CameraPreview
import com.github.tbsten.cameraxcompose.usecasehelper.imageAnalysisUseCase
import com.github.tbsten.cameraxcompose.usecasehelper.imageCaptureUseCase
import com.github.tbsten.cameraxcompose.usecasehelper.previewUseCase
import com.github.tbsten.cameraxcompose.util.takePicture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
internal fun CameraXComposeSample() {
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Box(Modifier.fillMaxSize()) {
        CameraPreview(
            onBind = {
                val executor = ContextCompat.getMainExecutor(context)
                // UseCases
                val preview = previewUseCase()
                val analysis = imageAnalysisUseCase(executor) {}
                imageCapture = imageCaptureUseCase()
                // bind
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    analysis,
                    imageCapture,
                )
            },
        )

        Button(
            onClick = {
                imageCapture?.let { imageCapture ->
                    saveImage(scope, context, imageCapture)
                }
            },
            modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 20.dp),
        ) {
            Text("save")
        }
    }
}

fun saveImage(
    scope: CoroutineScope,
    context: Context,
    imageCapture: ImageCapture,
) {
    scope.launch {
        val executor = ContextCompat.getMainExecutor(context)
        val options =
            ImageCapture.OutputFileOptions
                .Builder(
                    context.contentResolver,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    ContentValues().apply {
                        put(MediaStore.MediaColumns.DISPLAY_NAME, "${System.currentTimeMillis()}")
                        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                            val appName = context.resources.getString(R.string.app_name)
                            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/$appName")
                        }
                    },
                )
                .build()
        val image = imageCapture.takePicture(options, executor)

        context.startActivity(
            Intent(Intent.ACTION_VIEW, image.savedUri),
        )
    }
}
