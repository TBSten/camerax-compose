package com.github.tbsten.cameraxcompose

import android.view.ViewGroup
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun CameraPreview(
    onBind: OnBindScope.() -> Unit,
    onInitPreviewView: PreviewView.() -> Unit = {},
) {
    var bindingState by remember {
        mutableStateOf<BindingState>(BindingState.Initial)
    }
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    AndroidView(
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
        },
        update = { previewView ->
            bindingState =
                when (bindingState) {
                    is BindingState.Initial ->
                        try {
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

                            BindingState.Success
                        } catch (e: Exception) {
                            BindingState.Failed
                        }

                    is BindingState.Finish -> bindingState
                }
        },
    )
}
