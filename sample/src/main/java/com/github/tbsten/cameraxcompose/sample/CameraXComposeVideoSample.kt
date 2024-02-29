package com.github.tbsten.cameraxcompose.sample

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.provider.MediaStore
import androidx.camera.core.CameraSelector
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.github.tbsten.cameraxcompose.CameraPreview
import com.github.tbsten.cameraxcompose.usecasehelper.previewUseCase
import com.github.tbsten.cameraxcompose.usecasehelper.videoCaptureUseCase
import java.util.concurrent.Executor

@Composable
fun CameraXComposeVideoSample() {
    var videoCapture by remember { mutableStateOf<VideoCapture<Recorder>?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        CameraPreview(
            onBind = {
                // UseCases
                val preview = previewUseCase()
                videoCapture = videoCaptureUseCase()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    videoCapture,
                )
                // bind
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    videoCapture,
                )
            },
        )

        videoCapture?.let { videoCapture ->
            RecordingButton(
                videoCapture = videoCapture,
            )
        }
    }
}

@Composable
private fun RecordingButton(
    videoCapture: VideoCapture<Recorder>,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var recording by remember {
        mutableStateOf<Recording?>(null)
    }

    Button(
        onClick = {
            videoCapture.let { videoCapture ->
                recording =
                    if (recording == null) {
                        val executor = ContextCompat.getMainExecutor(context)
                        startVideoCapture(context, executor, videoCapture)
                    } else {
                        recording!!.stop()
                        null
                    }
            }
        },
        modifier = modifier,
    ) {
        Text(if (recording == null) "start" else "stop")
    }
}

@SuppressLint("MissingPermission") // TODO Check Permission
fun startVideoCapture(
    context: Context,
    executor: Executor,
    videoCapture: VideoCapture<Recorder>,
): Recording {
    val mediaStoreOutput =
        MediaStoreOutputOptions.Builder(
            context.contentResolver,
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
        )
            .setContentValues(
                ContentValues().apply {
                    put(
                        MediaStore.Video.Media.DISPLAY_NAME,
                        "CameraX-Recording-${System.currentTimeMillis()}.mp4",
                    )
                },
            )
            .build()

    return videoCapture.output
        .prepareRecording(context, mediaStoreOutput)
        .withAudioEnabled()
        .start(executor) {}
}
