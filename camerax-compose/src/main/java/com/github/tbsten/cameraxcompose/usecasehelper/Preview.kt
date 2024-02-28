package com.github.tbsten.cameraxcompose.usecasehelper

import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import com.github.tbsten.cameraxcompose.OnBindScope

fun OnBindScope.previewUseCase(builder: Preview.Builder.() -> Preview.Builder = { this }) =
    previewUseCase(
        builder = builder,
        previewView = previewView,
    )

fun previewUseCase(
    builder: Preview.Builder.() -> Preview.Builder = { this },
    previewView: PreviewView,
) = Preview.Builder()
    .builder()
    .build()
    .apply {
        setSurfaceProvider(previewView.surfaceProvider)
    }
