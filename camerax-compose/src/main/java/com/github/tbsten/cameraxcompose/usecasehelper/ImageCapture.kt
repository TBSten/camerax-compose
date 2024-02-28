package com.github.tbsten.cameraxcompose.usecasehelper

import androidx.camera.core.ImageCapture

fun imageCaptureUseCase(builder: ImageCapture.Builder.() -> ImageCapture.Builder = { this }) =
    ImageCapture.Builder()
        .builder()
        .build()
