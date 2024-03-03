package com.github.tbsten.cameraxcompose.usecasehelper

import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import com.github.tbsten.cameraxcompose.CameraPreview
import com.github.tbsten.cameraxcompose.OnBindScope

/**
 * [CameraPreview]のonBindコールバック内で使用できる[Preview]ユースケースに対応するUseCase helper。
 * 自動的に `setSurfaceProvider(previewView.surfaceProvider)` が実行されます。
 *
 * **使用例1** : 最小の例
 * ```kotlin
 * CameraPreview(
 *   onBind = {
 *     val preview = previewUseCase()
 *     cameraProvider.bindToLifecycle(
 *       ...,
 *       preview,
 *     )
 *   }
 * )
 * ```
 *
 * **使用例2** : builderでオプションを指定
 * ```kotlin
 * CameraPreview(
 *   onBind = {
 *     val preview = previewUseCase(
 *       builder = { setTargetAspectRatio(AspectRatio.RATIO_4_3) },
 *     )
 *     cameraProvider.bindToLifecycle(
 *       ...,
 *       preview,
 *     )
 *   }
 * )
 * ```
 *
 * @param builder [Preview.Builder] Builderでオプションを指定するためのコールバック。
 * @return [Preview]ユースケースのインスタンス。
 */
fun OnBindScope.previewUseCase(builder: Preview.Builder.() -> Preview.Builder = { this }) =
    previewUseCase(
        builder = builder,
        previewView = previewView,
    )

/**
 * [Preview]ユースケースに対応するUseCase helper。
 * 渡したpreviewViewで自動的に `setSurfaceProvider(previewView.surfaceProvider)` が実行されます。
 *
 * **使用例1** : 最小の例
 * ```kotlin
 * val preview = previewUseCase()
 * ```
 *
 * **使用例2** : builderでオプションを指定
 * ```kotlin
 * val preview = imageCaptureUseCase(
 *   builder = { setTargetAspectRatio(AspectRatio.RATIO_4_3) },
 * )
 * ```
 *
 * @param builder [Preview.Builder] Builderでオプションを指定するためのコールバック。
 * @return [Preview]ユースケースのインスタンス。
 */
fun previewUseCase(
    builder: Preview.Builder.() -> Preview.Builder = { this },
    previewView: PreviewView,
) = Preview.Builder()
    .builder()
    .build()
    .apply {
        setSurfaceProvider(previewView.surfaceProvider)
    }
