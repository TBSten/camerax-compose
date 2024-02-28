package com.github.tbsten.cameraxcompose.util

import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.Executor
import kotlin.coroutines.resume

suspend fun ImageCapture.takePicture(executor: Executor): ImageProxy =
    suspendCancellableCoroutine { continuation ->
        takePicture(
            executor,
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)
                    continuation.resume(image)
                }

                override fun onError(exception: ImageCaptureException) {
                    super.onError(exception)
                    continuation.cancel(exception)
                }
            },
        )
    }

suspend fun ImageCapture.takePicture(
    outputFileOptions: ImageCapture.OutputFileOptions,
    executor: Executor,
): ImageCapture.OutputFileResults =
    suspendCancellableCoroutine { continuation ->
        takePicture(
            outputFileOptions,
            executor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    continuation.resume(outputFileResults)
                }

                override fun onError(exception: ImageCaptureException) {
                    continuation.cancel(exception)
                }
            },
        )
    }
