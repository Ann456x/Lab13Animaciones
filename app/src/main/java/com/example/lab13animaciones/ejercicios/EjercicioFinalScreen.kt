package com.example.lab13animaciones.ejercicios

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lab13animaciones.R
import kotlin.random.Random

enum class EstadoEstres { NORMAL, VIGOROSO, TEMEROSO }

@Composable
fun EjercicioFinalScreen() {
    var estres by remember { mutableIntStateOf(0) }
    var estado by remember { mutableStateOf(EstadoEstres.NORMAL) }
    var vigorosoImg by remember { mutableIntStateOf(R.drawable.gatovigoroso1) }
    var temerosoImg by remember { mutableIntStateOf(R.drawable.gatotemeroso1) }

    val escenarios = remember {
        listOf(
            R.drawable.bg_1, R.drawable.bg_2, R.drawable.bg_3, R.drawable.bg_4, R.drawable.bg_5,
            R.drawable.bg_6, R.drawable.bg_7, R.drawable.bg_8, R.drawable.bg_9, R.drawable.bg_10
        )
    }
    var currentBgIndex by remember { mutableIntStateOf(0) }
    var bossBgRes by remember { mutableIntStateOf(R.drawable.boz1) }

    // Animación de caminata al avanzar
    val walkOffset by animateFloatAsState(
        targetValue = if (estres % 50 != 0 && estado == EstadoEstres.NORMAL) 10f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy),
        label = "walk"
    )

    // Vibración en estado Temeroso
    val infiniteTransition = rememberInfiniteTransition(label = "infinite")
    val shakeOffset by infiniteTransition.animateFloat(
        initialValue = -8f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(30, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shakeOffset"
    )

    // Brillo intenso en la barra cuando es VIGOROSO
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(250, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    val currentOffsetX = when (estado) {
        EstadoEstres.NORMAL -> walkOffset
        EstadoEstres.TEMEROSO -> shakeOffset
        EstadoEstres.VIGOROSO -> 0f
    }

    val scale by animateFloatAsState(
        targetValue = if (estado == EstadoEstres.NORMAL) 1.0f else 1.25f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale"
    )

    val progressAnimated by animateFloatAsState(
        targetValue = estres / 100f,
        animationSpec = tween(300),
        label = "progress"
    )

    val actualBgResource = if (estado == EstadoEstres.NORMAL) {
        escenarios[currentBgIndex % escenarios.size]
    } else {
        bossBgRes
    }

    // Color de la barra de estrés según requerimientos
    val barColor = when (estado) {
        EstadoEstres.NORMAL -> Color(0xFFF0FFFF) // Blanca Fluorescente
        EstadoEstres.VIGOROSO -> Color(0xFFFFFFFF).copy(alpha = glowAlpha) // Blanca Brillante Pulsante
        EstadoEstres.TEMEROSO -> Color(0xFFFF0000) // Rojo Vivo
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedContent(
            targetState = actualBgResource,
            transitionSpec = { fadeIn(tween(500)) togetherWith fadeOut(tween(300)) },
            label = "bgTransition",
            modifier = Modifier.fillMaxSize()
        ) { bg ->
            Image(
                painter = painterResource(id = bg),
                contentDescription = "Escenario",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.45f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "DARKEST CATS: MAZE",
                color = Color(0xFFD4AF37),
                fontWeight = FontWeight.Black,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Seccion de Barra de Estres sin caracteres rotos
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Estres: $estres / 100",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { progressAnimated },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(14.dp)
                        .clip(RoundedCornerShape(7.dp)),
                    color = barColor,
                    trackColor = Color(0xFF222222)
                )
            }

            Spacer(modifier = Modifier.height(35.dp))

            // Sprite del Gato Heroe
            Box(
                modifier = Modifier
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = currentOffsetX
                    )
                    .size(250.dp)
                    .clickable {
                        if (estres >= 100) {
                            estres = 0
                            estado = EstadoEstres.NORMAL
                            currentBgIndex = Random.nextInt(escenarios.size)
                        } else {
                            estres += 25
                            currentBgIndex = (currentBgIndex + 1) % escenarios.size
                            if (estres >= 100) {
                                val esVigoroso = Random.nextBoolean()
                                bossBgRes = if (Random.nextBoolean()) R.drawable.boz1 else R.drawable.boz2
                                if (esVigoroso) {
                                    estado = EstadoEstres.VIGOROSO
                                    vigorosoImg = if (Random.nextBoolean()) R.drawable.gatovigoroso1 else R.drawable.gatovigoroso2
                                } else {
                                    estado = EstadoEstres.TEMEROSO
                                    temerosoImg = if (Random.nextBoolean()) R.drawable.gatotemeroso1 else R.drawable.gatotemeroso2
                                }
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                val currentImageRes = when (estado) {
                    EstadoEstres.NORMAL -> R.drawable.gatoencamino1
                    EstadoEstres.VIGOROSO -> vigorosoImg
                    EstadoEstres.TEMEROSO -> temerosoImg
                }

                Image(
                    painter = painterResource(id = currentImageRes),
                    contentDescription = "Gato Heroe",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(35.dp))

            // Textos de estado formateados sin fallos de codificacion
            AnimatedContent(
                targetState = estado,
                transitionSpec = {
                    (fadeIn(tween(300)) + scaleIn()).togetherWith(fadeOut(tween(150)) + scaleOut())
                },
                label = "textTransition"
            ) { target ->
                when (target) {
                    EstadoEstres.NORMAL -> Text("Toca al gato para avanzar por la mazmorra", color = Color.LightGray)
                    EstadoEstres.VIGOROSO -> Text("VIRTUD: VIGOROSO!", color = Color(0xFFFFD700), fontWeight = FontWeight.Black, fontSize = 22.sp)
                    EstadoEstres.TEMEROSO -> Text("AFLICCION: TEMEROSO!", color = Color(0xFFFF3333), fontWeight = FontWeight.Black, fontSize = 22.sp)
                }
            }
        }
    }
}