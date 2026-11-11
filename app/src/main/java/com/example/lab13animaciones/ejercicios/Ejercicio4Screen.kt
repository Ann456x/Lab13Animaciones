package com.example.lab13animaciones.ejercicios

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

enum class EstadoPantalla { CARGANDO, CONTENIDO, ERROR }

@Composable
fun Ejercicio4Screen() {
    var estado by remember { mutableStateOf(EstadoPantalla.CARGANDO) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row {
            Button(onClick = { estado = EstadoPantalla.CARGANDO }) { Text("Cargando") }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { estado = EstadoPantalla.CONTENIDO }) { Text("Éxito") }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { estado = EstadoPantalla.ERROR }) { Text("Error") }
        }
        Spacer(modifier = Modifier.height(24.dp))
        
        AnimatedContent(
            targetState = estado,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "stateContent"
        ) { target ->
            when (target) {
                EstadoPantalla.CARGANDO -> CircularProgressIndicator()
                EstadoPantalla.CONTENIDO -> Text("¡Contenido cargado con éxito!", style = MaterialTheme.typography.titleMedium)
                EstadoPantalla.ERROR -> Text("Ocurrió un error al cargar datos.", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
