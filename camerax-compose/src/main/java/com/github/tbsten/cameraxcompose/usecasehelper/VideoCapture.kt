package com.github.tbsten.cameraxcompose.usecasehelper

import androidx.camera.video.Recorder
import androidx.camera.video.VideoCapture

fun videoCaptureUseCase(
    recorderBuilder: Recorder.Builder.() -> Recorder.Builder = { this },
): VideoCapture<Recorder> {
    val recorder = Recorder.Builder()
        .recorderBuilder()
        .build()
    return VideoCapture.withOutput(recorder)
}
