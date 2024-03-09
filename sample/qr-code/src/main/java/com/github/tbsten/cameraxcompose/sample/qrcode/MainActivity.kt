package com.github.tbsten.cameraxcompose.sample.qrcode

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.github.tbsten.cameraxcompose.sample.qrcode.ui.theme.CameraxcomposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CameraxcomposeTheme {
                CameraXComposeQrCodeSample()
            }
        }
    }
}
