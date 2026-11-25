package com.coronado.murocampus

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.lifecycle.lifecycleScope
import com.coronado.murocampus.data.remote.RetrofitClient
import com.coronado.murocampus.ui.theme.MuroCampusTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ===== PRUEBA TEMPORAL PASO 4 (borrar antes del commit) =====
        lifecycleScope.launch {
            try {
                val posts = RetrofitClient.api.listar()
                Log.d("MuroCampus", "Llegaron ${posts.size} publicaciones")
            } catch (e: Exception) {
                Log.e("MuroCampus", "Falló la llamada: $e")
            }
        }
        // ===== FIN PRUEBA TEMPORAL =====

        setContent {
            MuroCampusTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("MuroCampus") },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                ) { }
            }
        }
    }
}