package com.manu.streakcounter.main_ui

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.edit
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.manu.streakcounter.database.HomeScreenViewModel
import com.manu.streakcounter.database.Streak
import com.manu.streakcounter.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(viewModel: HomeScreenViewModel = viewModel(), navController: NavController) {
    ScreenBackHandler()
    val topAppBarColor = listOf(Color(0xFFFFA726), Color(0xFFFF5722))
    var isDialogOpened by remember { mutableStateOf(false) }
    val streaks by viewModel.streakList.collectAsStateWithLifecycle(initialValue = emptyList())
    val streakList = remember(streaks) {
        streaks.chunked(2).map { chunk ->
            if (chunk.size == 1) listOf(chunk[0], null) else chunk
        }
    }
    var customArrangement by remember { mutableStateOf(Arrangement.Top) }
    val dropDownExpanded = remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.background(Brush.linearGradient(topAppBarColor)),
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = "Streak Counter",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                        )
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More options",
                            modifier = Modifier.clickable { dropDownExpanded.value = true }
                        )
                        Box {
                            CustomDropDown(
                                dropDownExpanded.value,
                                { dropDownExpanded.value = false },
                                {
                                    isDialogOpened = true
                                    dropDownExpanded.value = false
                                }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            verticalArrangement = customArrangement,
        ) {
            if (streaks.isNotEmpty()) {
                customArrangement = Arrangement.Top
                items(streakList) { subStreakList ->
                    if (subStreakList.last() == null) {
                        StreakRow(subStreakList.first(), navController)
                    } else {
                        StreakRow(subStreakList.first(), subStreakList.last(), navController)
                    }
                }
            } else {
                customArrangement = Arrangement.Center
                item {

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No streaks added",
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                }
            }
        }
    }
    if (isDialogOpened) {
        AddStreakDialog(
            onDismiss = { isDialogOpened = false },
            streaks = streaks,
            viewModel = viewModel
        )
    }
}

@Composable
fun StreakRow(firstStreak: Streak?, secondStreak: Streak?, navController: NavController) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ElevatedCard(
            modifier = Modifier
                .padding(10.dp)
                .weight(1f)
                .clickable {
                    navController.navigate(
                        Routes.DetailsScreen(
                            streakName = firstStreak?.streakName ?: "",
                            streakCount = firstStreak?.currentStreak ?: 0,
                            lastUpdateTime = firstStreak?.lastUpdateTime ?: "",
                            targetStreak = firstStreak?.targetStreak ?: -1,
                            lastIncreasePressTime = firstStreak?.lastIncreasePressDate?:"NA"
                        )
                    )
                },
            elevation = CardDefaults.elevatedCardElevation(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = firstStreak?.streakName ?: "",
                    fontSize = 18.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${firstStreak?.currentStreak?.div(7)}W ${
                        firstStreak?.currentStreak?.rem(
                            7
                        )
                    }D",
                    fontSize = 24.sp
                )
            }
        }
        ElevatedCard(
            modifier = Modifier
                .padding(10.dp)
                .weight(1f)
                .clickable {
                    navController.navigate(
                        Routes.DetailsScreen(
                            streakName = secondStreak?.streakName ?: "",
                            streakCount = secondStreak?.currentStreak ?: 0,
                            lastUpdateTime = secondStreak?.lastUpdateTime ?: "",
                            targetStreak = secondStreak?.targetStreak ?: -1,
                            lastIncreasePressTime = secondStreak?.lastIncreasePressDate?:"NA"

                        )
                    )
                },
            elevation = CardDefaults.elevatedCardElevation(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = secondStreak?.streakName ?: "",
                    fontSize = 18.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${secondStreak?.currentStreak?.div(7)}W ${
                        secondStreak?.currentStreak?.rem(
                            7
                        )
                    }D",
                    fontSize = 24.sp
                )
            }
        }
    }
}

@Composable
fun StreakRow(firstStreak: Streak?, navController: NavController) {
    ElevatedCard(
        modifier = Modifier
            .padding(10.dp)
            .clickable {
                navController.navigate(
                    Routes.DetailsScreen(
                        streakName = firstStreak?.streakName ?: "",
                        streakCount = firstStreak?.currentStreak ?: 0,
                        lastUpdateTime = firstStreak?.lastUpdateTime ?: "",
                        targetStreak = firstStreak?.targetStreak ?: -1,
                        lastIncreasePressTime = firstStreak?.lastIncreasePressDate?:"NA"
                    )
                )
            },
        elevation = CardDefaults.elevatedCardElevation(10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(0.48f)
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = firstStreak?.streakName ?: "",
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${(firstStreak?.currentStreak)?.div(7)}W ${firstStreak?.currentStreak?.rem(7)}D",
                fontSize = 24.sp
            )
        }
    }
}

@Composable
fun AddStreakDialog(viewModel: HomeScreenViewModel, onDismiss: () -> Unit, streaks: List<Streak>) {
    val context = LocalContext.current
    var streakName by remember { mutableStateOf("") }
    var isStreakNameError by remember { mutableStateOf(false) }
    var isStreakExists = streaks.any { it.streakName == streakName }
    var nameErrorMessage by remember { mutableStateOf("") }
    var targetStreak by remember { mutableStateOf("") }
    var targetStreakUnit by remember { mutableStateOf("Days") }
    var dropDownExtended by remember { mutableStateOf(false) }
    val onClickDropDown: (String) -> Unit = { targetStreakUnit = it }
    var isTargetStreakError by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Dialog(onDismissRequest = onDismiss) {
            Card {
                Column(
                    modifier = Modifier.padding(10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Add Streak",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Spacer(Modifier.height(10.dp))
                    OutlinedTextField(
                        value = streakName,
                        onValueChange = {
                            if (it.length <= 20) {
                                streakName = it.uppercase()
                            }
                            isStreakNameError = false
                        },
                        singleLine = true,
                        label = { Text("Streak name") },
                        shape = RoundedCornerShape(10.dp),
                        isError = isStreakNameError,
                        supportingText = { Text(nameErrorMessage) },
                        trailingIcon = { Text("${streakName.length}/20") }
                    )
                    Spacer(Modifier.height(20.dp))
                    Text(
                        "Wanna set a target ? , Do it by setting a target streak",
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(10.dp))
                    OutlinedTextField(
                        value = targetStreak,
                        label = { Text("Enter Target") },
                        supportingText = { Text("Please enter a Integer as target streak") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        onValueChange = {
                            targetStreak = it
                            isTargetStreakError = false
                        },
                        isError = isTargetStreakError,
                        shape = RoundedCornerShape(10.dp),
                        trailingIcon = {
                            Row(
                                modifier = Modifier.clickable { dropDownExtended = true },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(targetStreakUnit)
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    modifier = Modifier.rotate(if (dropDownExtended) 180f else 0f),
                                    contentDescription = null
                                )
                            }
                            DropdownMenu(
                                expanded = dropDownExtended,
                                onDismissRequest = { dropDownExtended = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Days") },
                                    onClick = {
                                        onClickDropDown("Days")
                                        dropDownExtended = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Weeks") },
                                    onClick = {
                                        onClickDropDown("Weeks")
                                        dropDownExtended = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Months") },
                                    onClick = {
                                        onClickDropDown("Months")
                                        dropDownExtended = false
                                    }
                                )
                            }

                        }
                    )
                    Button(
                        onClick = {
                            nameErrorMessage = if (streakName.isBlank()) {
                                "Streak name cannot be empty"
                            } else if (isStreakExists) {
                                "Streak already exists"
                            } else {
                                ""
                            }
                            if (streakName.isNotBlank() && !isStreakExists) {
                                try {
                                    val targetStreakInDays = if (targetStreak.isBlank()) {
                                        -1
                                    } else {
                                        when (targetStreakUnit) {
                                            "Days" -> targetStreak.toInt()
                                            "Weeks" -> targetStreak.toInt() * 7
                                            else -> targetStreak.toInt() * 30
                                        }
                                    }
                                    viewModel.addStreak(
                                        Streak(
                                            streakName = streakName,
                                            currentStreak = 0,
                                            lastUpdateTime = CurrentDateTime(),
                                            targetStreak = targetStreakInDays,
                                            lastIncreasePressDate = "NA"
                                        )
                                    )
                                    onDismiss()
                                    Toast.makeText(context, "Streak added", Toast.LENGTH_SHORT)
                                        .show()
                                } catch (e: Exception) {
                                    isTargetStreakError = true
                                }

                            } else {
                                isStreakNameError = true
                            }
                        }
                    ) {
                        Text("Add")
                    }
                }
            }
        }
    }
}

@Composable
private fun CustomDropDown(expanded: Boolean, onDismiss: () -> Unit, onClickAddTask: () -> Unit) {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
    var isAppLockEnabled by remember { mutableStateOf(prefs.getBoolean("isAppLockEnabled", false)) }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
    ) {

        DropdownMenuItem(
            onClick = onClickAddTask,
            text = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.AddCircle, null)
                    Spacer(Modifier.width(5.dp))
                    Text("Add Streak", fontSize = 20.sp)
                }
            }
        )
        HorizontalDivider()
        DropdownMenuItem(
            onClick = { isAppLockEnabled = !isAppLockEnabled },
            text = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Lock, null)
                    Spacer(Modifier.width(5.dp))
                    Text("App Lock", fontSize = 20.sp)
                    Spacer(Modifier.width(5.dp))
                    Switch(
                        checked = isAppLockEnabled,
                        onCheckedChange = {
                            isAppLockEnabled = !isAppLockEnabled
                            context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE).edit {
                                putBoolean("isAppLockEnabled", isAppLockEnabled)
                            }
                        }
                    )
                }
            }
        )
    }

}

@Composable
fun ScreenBackHandler() {
    val maxBackPressInterval = 2000L
    val currentTime = System.currentTimeMillis()
    var lastBackPressTime by remember { mutableStateOf(0L) }
    val context = LocalContext.current
    BackHandler(enabled = true) {
        if (currentTime - lastBackPressTime < maxBackPressInterval) {
            (context as? Activity)?.finish()
        } else {
            Toast.makeText(context, "Press back again to exit", Toast.LENGTH_SHORT).show()
            lastBackPressTime = currentTime
        }
    }
}