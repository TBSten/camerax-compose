package com.github.tbsten.cameraxcompose.sample

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.camera.core.CameraSelector
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.github.tbsten.cameraxcompose.CameraPreview
import com.github.tbsten.cameraxcompose.usecasehelper.previewUseCase
import com.github.tbsten.cameraxcompose.usecasehelper.videoCaptureUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.Executor

@Composable
internal fun CameraXComposeVideoSample() {
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
    var outputFileName by remember {
        mutableStateOf<String?>(null)
    }
    val controller =
        remember {
            RecordingController(
                context,
                ContextCompat.getMainExecutor(context),
                videoCapture,
            )
        }
    val recordingState by controller.recordingState.collectAsState()
    val scope = rememberCoroutineScope()

    Button(
        onClick = {
            scope.launch {
                if (recordingState is RecordingState.Recording) {
                    val outputUri = controller.stop()
                    context.startActivity(
                        Intent(Intent.ACTION_VIEW, outputUri),
                    )
                } else {
                    outputFileName = "CameraX-Recording-${System.currentTimeMillis()}.mp4"
                    controller.start(outputFileName!!)
                }
            }
        },
        modifier = modifier,
    ) {
        Text(if (recordingState is RecordingState.Recording) "stop" else "start")
    }
}

@Suppress("MissingPermission")
class RecordingController(
    private val context: Context,
    private val executor: Executor,
    private val videoCapture: VideoCapture<Recorder>,
) {
    private var recording: Recording? = null
    var recordingState: MutableStateFlow<RecordingState> =
        MutableStateFlow(RecordingState.BeforeStart)
        private set

    // TODO check permission
    // issue#3で権限ヘルパー追加後に対応予定
    // https://github.com/TBSten/camerax-compose/issues/3
    fun start(outputFileName: String) {
        val mediaStoreOutput =
            MediaStoreOutputOptions.Builder(
                context.contentResolver,
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            )
                .setContentValues(
                    ContentValues().apply {
                        put(
                            MediaStore.Video.Media.DISPLAY_NAME,
                            outputFileName,
                        )
                    },
                )
                .build()

        recordingState.update { RecordingState.Recording }

        recording =
            videoCapture.output
                .prepareRecording(context, mediaStoreOutput)
                .withAudioEnabled()
                .start(executor) { event ->
                    // on complete
                    when (event) {
                        is VideoRecordEvent.Finalize -> {
                            val uri = event.outputResults.outputUri
                            recordingState.update { RecordingState.Finalize(uri) }
                        }
                    }
                }
    }

    suspend fun stop(): Uri {
        if (recordingState.value !is RecordingState.Recording) throw IllegalStateException("not recording !!")
        val resultFlow =
            recordingState
                .filterIsInstance<RecordingState.Finalize>()
        recording!!.stop()
        val result = resultFlow.first()
        return result.uri
    }
}

sealed interface RecordingState {
    data object BeforeStart : RecordingState

    data object Recording : RecordingState

    data class Finalize(val uri: Uri) : RecordingState
}
