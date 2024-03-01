package com.github.tbsten.cameraxcompose.usecasehelper

import androidx.camera.core.ImageAnalysis
import java.util.concurrent.Executor

/**
 * [ImageAnalysis]ユースケースに対応するUseCase helper。
 *
 * **使用例1** : 最小の例
 * ```kotlin
 * val imageAnalysis = imageAnalysisUseCase(executor) {
 *   it.use { imageProxy ->
 *     // TODO
 *   }
 * }
 * ```
 *
 * **使用例2** : builderでオプションを指定
 * ```kotlin
 * val imageAnalysis = imageAnalysisUseCase(
 *   executor = executor,
 *   builder = { setTargetAspectRatio(AspectRatio.RATIO_4_3) },
 * ) {
 *   it.use { imageProxy ->
 *     // TODO
 *   }
 * }
 * ```
 *
 * @param executor [ImageAnalysis.setAnalyzer] に渡すexecutor。
 * @param builder [ImageAnalysis.Builder] Builderでオプションを指定するためのコールバック。
 * @param analyzer 画像のImageProxyを受け取って画像を処理するコールバック。ImageProxy.closeを呼び出さないと次のAnalyzerが起動しないので注意。
 * @return [ImageAnalysis]ユースケースのインスタンス。
 */
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
