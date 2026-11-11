package com.example.lab13animaciones.ejercicios

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Ejercicio3Screen() {
    var moved by remember { mutableStateOf(false) }
    val size by animateDpAsState(targetValue = if (moved) 150.dp else 80.dp, label = "size")
    val offsetY by animateDpAsState(targetValue = if (moved) 100.dp else 0.dp, label = "offset")

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { moved = !moved }) {
            Text("Mover y Escalar")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .offset(y = offsetY)
                .size(size)
                .background(Color.Red)
        )
    }
}
