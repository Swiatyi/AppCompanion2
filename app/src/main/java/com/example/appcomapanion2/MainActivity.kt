package com.example.appcomapanion2

// ╔══════════════════════════════════════════════╗
// ║  MainActivity.kt                             ║
// ║  PONTO DE ENTRADA do aplicativo              ║
// ║  Chama AppNavigation() que controla as telas ║
// ╚══════════════════════════════════════════════╝

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.appcomapanion2.ui.theme.AppComapanion2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppComapanion2Theme {
                // Ponto de entrada da navegação
                AppNavigation()
            }
        }
    }
}
