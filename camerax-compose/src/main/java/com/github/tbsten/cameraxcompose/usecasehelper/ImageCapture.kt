package com.github.tbsten.cameraxcompose.usecasehelper

import androidx.camera.core.ImageCapture

/**
 * [ImageCapture]ユースケースに対応するUseCase helper。
 *
 * **使用例1** : 最小の例
 * ```kotlin
 * val imageCapture = imageCaptureUseCase()
 * ```
 *
 * **使用例2** : builderでオプションを指定
 * ```kotlin
 * val imageCapture = imageCaptureUseCase(
 *   builder = { setTargetAspectRatio(AspectRatio.RATIO_4_3) },
 * )
 * ```
 *
 * @param builder [ImageCapture.Builder] Builderでオプションを指定するためのコールバック。
 * @return [ImageCapture]ユースケースのインスタンス。
 */
fun imageCaptureUseCase(builder: ImageCapture.Builder.() -> ImageCapture.Builder = { this }) =
    ImageCapture.Builder()
        .builder()
        .build()
