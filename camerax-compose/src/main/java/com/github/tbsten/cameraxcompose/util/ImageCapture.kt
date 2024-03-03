package com.github.tbsten.cameraxcompose.util

import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.Executor
import kotlin.coroutines.resume

/**
 * [ImageCapture.takePicture(executor, callback)][ImageCapture.takePicture]をcoroutineで利用できるようにする拡張関数。
 * 取得した画像をImageProxyとして返し、画像を保存しません。
 * @param executor [ImageCapture.takePicture](executor, callback)のexecutor
 * @return [ImageCapture.OnImageCapturedCallback.onCaptureSuccess]で取得した画像のImageProxy
 * @see ImageCapture.takePicture
 */
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

/**
 * [ImageCapture.takePicture(outputFileOptions, executor, callback)][ImageCapture.takePicture]をcoroutineで利用できるようにする拡張関数。
 * 取得した画像を保存し、保存先などを[ImageCapture.OutputFileResults]として返します。
 * @param outputFileOptions [ImageCapture.takePicture](outputFileOptions, executor, callback)のoutputFileOptions
 * @param executor [ImageCapture.takePicture](outputFileOptions, executor, callback)のexecutor
 * @return [ImageCapture.OnImageSavedCallback.onImageSaved]で取得した画像のoutputFileResults。savedUriにアクセスすることで保存先のUriを取得できます。
 * @see ImageCapture.takePicture
 */
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
