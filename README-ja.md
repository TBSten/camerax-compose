# CameraX-Compose

[![Release](https://jitpack.io/v/tbsten/camerax-compose.svg)](https://jitpack.io/#tbsten/camerax-compose)

`CameraX-Compose` は Compose で CameraX をシンプル・簡単に利用するためのライブラリです。

```kotlin
// このライブラリを使用するためのシンプルな例です。
CameraPreview(
    onBind = {
        val preview = previewUseCase()
        val analysis = imageAnalysisUseCase(executor) {}
        imageCapture = imageCaptureUseCase()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            CameraSelector.DEFAULT_BACK_CAMERA,
            preview,
            analysis,
            imageCapture,
        )
    },
)
```

## 目次

- [インストール](#インストール)
- [Quick Start](#quick-start)
- [参考資料](#参考資料)
- [サンプル](#サンプル)
- [ライセンス](#ライセンス)

## インストール

プロジェクトレベルの build.gradle.kts に以下を追加します。

このライブラリは**camerax の依存関係も必要**な点に注意してください。

### groovy

```groovy
// project settings.gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
// app/build.gradle
dependencies {
    // TODO cameraxの依存関係を追加すること
    // 詳しくはこちら: https://developer.android.com/jetpack/androidx/releases/camera#dependencies
    implementation 'com.github.tbsten:camerax-compose:<current-version>'
}
```

### kotlin

```kotlin
// settings.gradle.kts
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = URI("https://jitpack.io") }
    }
}
// app/build.gradle.kts
dependencies {
    // TODO cameraxの依存関係を追加すること
    // 詳しくはこちら: https://developer.android.com/jetpack/androidx/releases/camera#dependencies
    implementation("com.github.tbsten:camerax-compose:<current-version>")
}
```

## Quick Start

インストールが完了したら、以下のソースコードを Composable に貼り付けます。

```kotlin
var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
val scope = rememberCoroutineScope()
val context = LocalContext.current

CameraPreview(
    onBind = {
        val executor = ContextCompat.getMainExecutor(context)
        // ユースケース
        // 必要に応じて既にあるUseCaseを利用することもできます。
        val preview = previewUseCase()
        val analysis = imageAnalysisUseCase(executor) {}
        imageCapture = imageCaptureUseCase()
        // ユースケースをbindします
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            CameraSelector.DEFAULT_BACK_CAMERA,
            preview,
            analysis,
            imageCapture,
        )
    },
)
```

ボタンがクリックされた時などに、以下を呼び出します。
`imageCapture.takePicture` はこのライブラリが提供する便利な拡張関数です。

```kotlin
scope.launch {
    val executor = ContextCompat.getMainExecutor(context)
    // 写真を撮影して保存
    val options = ImageCapture.OutputFileOptions
        .Builder(
            context.contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, "${System.currentTimeMillis()}")
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                    val appName = context.resources.getString(R.string.app_name)
                    put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/${appName}")
                }
            },
        )
        .build()
    val image = imageCapture.takePicture(options, executor)

    // 保存した画像を画像ビューアで閲覧
    context.startActivity(
        Intent(Intent.ACTION_VIEW, image.savedUri)
    )
}
```

アプリケーションを起動し、適切なアクセス許可を設定すると、撮影できるようになります。
写真を撮ることができるはずです。(これは簡単な例であるためパーミッションは設定アプリから設定する必要があります)

## サンプル

### ⭐️ カメラで撮影して保存する

```kotlin
// プレビューを表示
CameraPreview(
    onBind = {
        // UseCases
        val preview = previewUseCase()
        imageCapture = imageCaptureUseCase()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            CameraSelector.DEFAULT_BACK_CAMERA,
            preview,
            imageCapture,
        )
    },
)

// 画像をキャプチャ
imageCapture?.let { imageCapture ->
    saveImage(scope, context, imageCapture)
}
```

[See more](./sample/image-capture/src/main/java/com/github/tbsten/cameraxcompose/sample/imagecapture/)

![image-capture-sample](./sample/image-capture/image-capture-sample.gif)

### ⭐️ カメラで録画して保存する

[See more](./sample/video-capture/src/main/java/com/github/tbsten/cameraxcompose/sample/videocapture/)

![video-capture-sample](./sample/video-capture/video-capture-sample.gif)

### ⭐️ QR コードを読み取る

[See more](./sample/qr-code/src/main/java/com/github/tbsten/cameraxcompose/sample/qrcode/)

![qr-code-sample](./sample/qr-code/qrcode-sample.gif)

## 参考資料

- [サンプルプロジェクト](https://github.com/TBSten/nextjs-netlify-suspense-prac/tree/main/samle)
- [作者の実装ログ](https://zenn.dev/tbsten/scraps/b04d31b4c01c99)
- [コントリビュートについて](./CONTRIBUTING-ja.md)

## ライセンス

All code, unless specified otherwise, is licensed under
the [MIT](https://opensource.org/license/MIT) license.
