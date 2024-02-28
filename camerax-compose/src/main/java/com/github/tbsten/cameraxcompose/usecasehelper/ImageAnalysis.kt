package com.github.tbsten.cameraxcompose.usecasehelper

import androidx.camera.core.ImageAnalysis
import java.util.concurrent.Executor

fun imageAnalysisUseCase(
    executor: Executor,
    builder: ImageAnalysis.Builder.() -> ImageAnalysis.Builder = { this },
    analyzer: ImageAnalysis.Analyzer,
) = ImageAnalysis.Builder()
    .builder()
    .build()
    .apply {
        setAnalyzer(executor, analyzer)
    }
