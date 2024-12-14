package com.tecknobit.pandoro

import android.os.Bundle
import android.os.StrictMode
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import com.tecknobit.equinoxcompose.helpers.session.setUpSession
import com.tecknobit.equinoxcompose.helpers.utils.ContextActivityProvider
import io.github.vinceglb.filekit.core.FileKit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FileKit.init(this)
        ContextActivityProvider.setCurrentActivity(this)
        setContent {
            enableEdgeToEdge()
            InitSession()
            App()
        }
    }
}

@Composable
@NonRestartableComposable
private fun InitSession() {
    val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
    StrictMode.setThreadPolicy(policy)
    setUpSession {
        // TODO: TO SET
    }
}