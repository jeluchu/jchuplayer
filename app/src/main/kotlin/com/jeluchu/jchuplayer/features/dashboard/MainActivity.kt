package com.jeluchu.jchuplayer.features.dashboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.jeluchu.jchuplayer.core.ui.navigation.Navigation
import com.jeluchu.jchuplayer.core.ui.theme.JchuplayerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JchuplayerTheme {
                Navigation()
            }
        }
    }
}