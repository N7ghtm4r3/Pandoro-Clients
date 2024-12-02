package com.tecknobit.pandoro

import android.os.Bundle
import android.os.StrictMode
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import com.tecknobit.equinoxcompose.helpers.session.setUpSession

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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