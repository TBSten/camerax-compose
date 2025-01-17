# CameraX-Compose

> [!CAUTION]
> With the advent of the official camerax artifact for compose, this library is no longer available.
> 
> See:
> - https://developer.android.com/jetpack/androidx/releases/camera?hl=ja
> - https://medium.com/androiddevelopers/getting-started-with-camerax-in-jetpack-compose-781c722ca0c4

[![Release](https://jitpack.io/v/tbsten/camerax-compose.svg)](https://jitpack.io/#tbsten/camerax-compose)

`CameraX-Compose` is a library for simple and easy use of CameraX with Compose.

```kotlin
// This is all you need to use the camera.
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

## Table of Contents

- [How to install](#how-to-install)
- [Quick Start](#quick-start)
- [Other Documents](#other-documents)
- [Samples](#samples)
- [Licence](#licence)

## How to install

Add the following to build.gradle.kts at project level.

This library **must be combined with a camerax dependency**.

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
    // TODO camerax dependencies
    // For more information, see: https://developer.android.com/jetpack/androidx/releases/camera#dependencies
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
    // TODO camerax dependencies
    implementation("com.github.tbsten:camerax-compose:<current-version>")
}
```

## Quick Start

After installation is complete, paste the following source code into your Composable.

```kotlin
var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
val scope = rememberCoroutineScope()
val context = LocalContext.current

CameraPreview(
    onBind = {
        val executor = ContextCompat.getMainExecutor(context)
        // UseCases
        // You can also use an already existing UseCase if necessary.
        val preview = previewUseCase()
        val analysis = imageAnalysisUseCase(executor) {}
        imageCapture = imageCaptureUseCase()
        // bind
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

When a button is clicked, for example, call
`imageCapture.takePicture` is a convenience extension function provided by this library.

```kotlin
scope.launch {
    val executor = ContextCompat.getMainExecutor(context)
    // take and save picture
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

    // View saved images in an image viewer
    context.startActivity(
        Intent(Intent.ACTION_VIEW, image.savedUri)
    )
}
```

After launching the application and setting the appropriate permissions, you should be able to take
a picture. (This is a simple example; permissions must be set from the Settings app.)

## Samples

### ⭐️ Take a picture with the camera and save it

```kotlin
// Show preview by CameraPreview Composable
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

// Capture image
imageCapture?.let { imageCapture ->
    saveImage(scope, context, imageCapture)
}
```

[See more](./sample/image-capture/src/main/java/com/github/tbsten/cameraxcompose/sample/imagecapture/)

![image-capture-sample](./sample/image-capture/image-capture-sample.gif)

### ⭐️ Record and save with the camera

[See more](./sample/video-capture/src/main/java/com/github/tbsten/cameraxcompose/sample/videocapture/)

![video-capture-sample](./sample/video-capture/video-capture-sample.gif)

### ⭐️ Scan QR codes

[See more](./sample/qr-code/src/main/java/com/github/tbsten/cameraxcompose/sample/qrcode/)

![qr-code-sample](./sample/qr-code/qrcode-sample.gif)

## Other Documents

- [sample project](https://github.com/TBSten/nextjs-netlify-suspense-prac/tree/main/samle)
- [Author's implementation log](https://zenn.dev/tbsten/scraps/b04d31b4c01c99)
- [About contributing](./CONTRIBUTING.md)

## Licence

All code, unless specified otherwise, is licensed under
the [MIT](https://opensource.org/license/MIT) license.
