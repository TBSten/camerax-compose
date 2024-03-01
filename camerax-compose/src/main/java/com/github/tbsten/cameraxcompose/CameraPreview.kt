package com.github.tbsten.cameraxcompose

import android.view.ViewGroup
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner

/**
 * カメラのPreviewを表示するための **Composable** 関数。
 * プレビューの表示にはonBindコールバック内でPreviewユースケースをbindする必要があります。
 *
 *
 * **使用例**
 * ```kotlin
 * CameraPreview(
 *   onBind = {
 *     val preview = previewUseCase()
 *     cameraProvider.bindToLifecycle(
 *       lifecycleOwner,
 *       CameraSelector.DEFAULT_BACK_CAMERA,
 *       preview,
 *     )
 *   },
 * )
 * ```
 *
 * @param onBind UseCaseを用意してcameraProvider.bindToLifecycleを呼び出してください。レシーバとしてOnBindScopeを受け取るため、previewViewやcameraProviderなどにアクセスできます。
 *
 */
@Composable
fun CameraPreview(
    onBind: OnBindScope.() -> Unit,
    modifier: Modifier = Modifier,
    onInitPreviewView: PreviewView.() -> Unit = {},
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
) {
    val context = LocalContext.current
    AndroidView(
        modifier = modifier,
        factory = {
            PreviewView(it)
                .apply {
                    scaleType = PreviewView.ScaleType.FILL_CENTER
                    layoutParams =
                        ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                        )
                }.apply(onInitPreviewView)
                .also { previewView ->
                    val cameraProvider =
                        ProcessCameraProvider
                            .getInstance(
                                context,
                            ).get()
                    cameraProvider.unbindAll()

                    OnBindScope(
                        cameraProvider = cameraProvider,
                        previewView = previewView,
                        context = context,
                        lifecycleOwner = lifecycleOwner,
                    ).onBind()
                }
        },
    )
}
