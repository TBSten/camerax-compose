package com.github.tbsten.cameraxcompose.usecasehelper

import androidx.camera.video.Recorder
import androidx.camera.video.VideoCapture

/**
 * [VideoCapture]ユースケースに対応するUseCase helper。
 * Recorder以外のVideoOutputには対応していません。
 *
 * **使用例1** : 最小の例
 * ```kotlin
 * val videoCapture = videoCaptureUseCase()
 * ```
 *
 * **使用例2** : builderでオプションを指定
 * ```kotlin
 * val videoCapture = videoCaptureUseCase(
 *   recorderBuilder = { setTargetAspectRatio(AspectRatio.RATIO_4_3) },
 * )
 * ```
 *
 * @param recorderBuilder [Recorder.Builder] Builderでオプションを指定するためのコールバック。
 * @return [VideoCapture]ユースケースのインスタンス。
 */

fun videoCaptureUseCase(recorderBuilder: Recorder.Builder.() -> Recorder.Builder = { this }): VideoCapture<Recorder> {
    val recorder =
        Recorder.Builder()
            .recorderBuilder()
            .build()
    return VideoCapture.withOutput(recorder)
}
