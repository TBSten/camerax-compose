package com.github.tbsten.cameraxcompose

import android.content.Context
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner

data class OnBindScope(
    val cameraProvider: ProcessCameraProvider,
    val previewView: PreviewView,
    val context: Context,
    val lifecycleOwner: LifecycleOwner,
)
