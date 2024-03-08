package com.github.tbsten.cameraxcompose.sample.videocapture

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.github.tbsten.cameraxcompose.sample.CameraXComposeVideoSample
import com.github.tbsten.cameraxcompose.sample.videocapture.ui.theme.CameraxcomposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CameraxcomposeTheme {
                CameraXComposeVideoSample()
            }
        }
    }
}
