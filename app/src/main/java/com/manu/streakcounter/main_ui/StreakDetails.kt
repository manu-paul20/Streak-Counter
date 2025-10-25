package com.manu.streakcounter.main_ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.manu.streakcounter.R
import com.manu.streakcounter.database.HomeScreenViewModel
import com.manu.streakcounter.database.Streak
import com.manu.streakcounter.navigation.Routes
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StreakDetails(
    viewModel: HomeScreenViewModel = viewModel(),
    streak: Streak,
    navController: NavController
) {
    ScreenBackHandler()

    val topAppbarColor = listOf(Color(0xFFFFA726), Color(0xFFFF5722))
    var streakCount by remember { mutableStateOf(streak.currentStreak) }
    var lastUpdateTime by remember { mutableStateOf(streak.lastUpdateTime) }
    val context = LocalContext.current
    var isGreetDialogVisible by remember { mutableStateOf(false) }
    LaunchedEffect(streakCount, streak.targetStreak) {
        if (streakCount == streak.targetStreak) {
            isGreetDialogVisible = true
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                viewModel.updateStreak(
                                    Streak(
                                        streak.streakName,
                                        streakCount,
                                        lastUpdateTime,
                                        streak.targetStreak
                                    )
                                )
                                navController.navigate(Routes.Home)
                            }
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(
                            text = streak.streakName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            modifier = Modifier.fillMaxWidth(0.85f)
                        )
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                viewModel.deleteStreak(
                                    Streak(
                                        streak.streakName,
                                        streakCount,
                                        lastUpdateTime,
                                        streak.targetStreak
                                    )
                                )
                                navController.navigate(Routes.Home)
                                Toast.makeText(context, "Streak deleted", Toast.LENGTH_SHORT).show()
                            }
                        )

                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                modifier = Modifier.background(Brush.linearGradient(topAppbarColor))
            )
        }
    )
    {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Last updated at:",
                    fontSize = 16.sp
                )
                Text(lastUpdateTime, fontSize = 16.sp)
                Spacer(Modifier.height(10.dp))
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    FireStreakAnimation()
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = "${streakCount / 7} ${if (streakCount / 7 > 1) "weeks" else "week"} ${streakCount % 7} ${if (streakCount % 7 > 1) "days" else "day"}",
                        fontSize = 50.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(Modifier.width(10.dp))
                    FireStreakAnimation()
                }
                Spacer(Modifier.height(30.dp))
                CustomizedButtons(
                    onClickResetButton = {
                        streakCount = 0
                        lastUpdateTime = CurrentDateTime()
                        viewModel.updateStreak(
                            Streak(
                                streak.streakName,
                                streakCount,
                                lastUpdateTime,
                                streak.targetStreak
                            )
                        )
                    },
                    onClickIncreaseButton = {
                        streakCount++
                        lastUpdateTime = CurrentDateTime()
                        viewModel.updateStreak(
                            Streak(
                                streak.streakName,
                                streakCount,
                                lastUpdateTime,
                                streak.targetStreak
                            )
                        )
                    }
                )
                Spacer(Modifier.height(50.dp))
                if (streak.targetStreak > 0) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Your current progress",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Spacer(Modifier.height(20.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            LinearProgressIndicator(
                                progress = { streakCount.toFloat() / streak.targetStreak.toFloat() },
                                color = Color.Blue,
                                trackColor = Color.LightGray,
                                gapSize = -50.dp,
                                modifier = Modifier
                                    .fillMaxWidth(0.8f)
                                    .size(30.dp)
                            )
                            Spacer(Modifier.width(10.dp))
                            Text("$streakCount / ${streak.targetStreak} days")
                        }

                    }
                }
            }
        }
    }
    if (isGreetDialogVisible) {
        GreetDialog(
            onClickReset = {
                streakCount = 0
                viewModel.updateStreak(
                    Streak(
                        streakName = streak.streakName,
                        lastUpdateTime = streak.lastUpdateTime,
                        currentStreak = streakCount,
                        targetStreak = streak.targetStreak
                    )
                )
                isGreetDialogVisible = false
            },
            onClickNew = {
                viewModel.updateStreak(
                    Streak(
                        streakName = streak.streakName,
                        currentStreak = streakCount,
                        lastUpdateTime = streak.lastUpdateTime,
                        targetStreak = it // setting new target streak
                    )
                )
                isGreetDialogVisible = false
                navController.navigate(Routes.Home)
                Toast.makeText(context, "Streak updated", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

@Composable
fun CustomizedButtons(
    onClickResetButton: () -> Unit,
    onClickIncreaseButton: () -> Unit
) {
    val increaseButtonColor = listOf(Color(0xFF4CAF50), Color(0xFF81C784))
    val resetButtonColor = listOf(Color(0xFFE53935), Color(0xFFEF5350))
    Row(
        Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Button(
            onClick = onClickResetButton,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(resetButtonColor),
                    shape = RoundedCornerShape(50.dp)
                )
                .weight(1f),
        ) {
            Text("Reset")
        }
        Spacer(Modifier.width(10.dp))
        Button(
            onClick = onClickIncreaseButton,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(increaseButtonColor),
                    shape = RoundedCornerShape(50.dp)
                )
                .weight(1f),
        ) {
            Text("Increase")
        }
    }
}

fun CurrentDateTime(): String {
    val dateTime = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
    return dateTime.format(formatter)
}

@Composable
private fun FireStreakAnimation() {
    val composition = rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.firestreak))

    val progress = animateLottieCompositionAsState(
        composition = composition.value,
        isPlaying = true,
        iterations = LottieConstants.IterateForever
    )

    LottieAnimation(
        modifier = Modifier.size(70.dp),
        composition = composition.value,
        progress = { progress.value }
    )
}

@Composable
fun GreetDialog(
    onClickReset: () -> Unit,
    onClickNew: (Int) -> Unit
) {
    val greetColor = listOf(Color(0xFFFFA726), Color(0xFFFF5722))
    var newTargetStreak by remember { mutableStateOf("") }
    var isNewTargetDialogVisible by remember { mutableStateOf(false) }
    var isTargetStreakError by remember { mutableStateOf(false) }
    var targetStreakErrorMessage by remember { mutableStateOf("") }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Dialog(
            onDismissRequest = {}
        ) {
            Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(
                    Modifier.padding(10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (isNewTargetDialogVisible) {
                        Text(
                            text = "Set New Target",
                            fontSize = 30.sp
                        )
                        Spacer(Modifier.height(10.dp))
                        OutlinedTextField(
                            value = newTargetStreak,
                            shape = RoundedCornerShape(10.dp),
                            onValueChange = {
                                newTargetStreak = it
                                isTargetStreakError = false
                                targetStreakErrorMessage = ""
                            },
                            isError = isTargetStreakError,
                            label = { Text("Enter new target streak") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            supportingText = { Text(targetStreakErrorMessage) }
                        )
                        Button(
                            onClick = {
                                try {
                                    onClickNew(newTargetStreak.toInt())
                                } catch (e: Exception) {
                                    isTargetStreakError = true
                                    targetStreakErrorMessage = "Please enter a number"
                                }
                            }
                        ) {
                            Text("Submit")
                        }
                    } else {
                        Text(
                            text = AnnotatedString(
                                text = "Congratulations !",
                                spanStyle = SpanStyle(brush = Brush.linearGradient(greetColor))
                            ),
                            fontSize = 30.sp,
                            fontWeight = FontWeight.ExtraBold,
                        )
                        LottieAnim(
                            composition = LottieCompositionSpec.RawRes(R.raw.cutebeardancing),
                            modifier = Modifier.size(100.dp)
                        )
                        Spacer(Modifier.height(20.dp))
                        Text(
                            text = "You have reached your goal",
                            fontSize = 20.sp
                        )
                        Spacer(Modifier.height(10.dp))
                        Row {
                            TextButton(
                                onClick = { onClickReset() },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "Reset streak",
                                    fontSize = 16.sp
                                )
                            }
                            TextButton(
                                onClick = { isNewTargetDialogVisible = true },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "New target",
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LottieAnim(
    composition: LottieCompositionSpec,
    modifier: Modifier = Modifier
) {
    val anim by rememberLottieComposition(composition)
    val progress = animateLottieCompositionAsState(
        composition = anim,
        isPlaying = true,
        iterations = LottieConstants.IterateForever
    )

    LottieAnimation(
        composition = anim,
        progress = { progress.value },
        modifier = modifier
    )
}