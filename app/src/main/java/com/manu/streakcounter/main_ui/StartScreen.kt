package com.manu.streakcounter.main_ui

import android.app.Activity
import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import com.manu.streakcounter.R
import com.manu.streakcounter.navigation.Routes
import kotlinx.coroutines.delay

@Composable
fun Start(navController: NavController) {
    var isAnimating by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val activity = context as FragmentActivity
    val executor = ContextCompat.getMainExecutor(context)
    val isAppLockEnabled = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE).getBoolean("isAppLockEnabled",false)

    var promptInfo = remember {
        BiometricPrompt.PromptInfo.Builder()
            .setTitle("Unlock Streak Counter to continue")
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
            .build()
    }

    fun ShowBiometricPrompt() {

        val biometricPrompt = BiometricPrompt(activity,executor, object : BiometricPrompt.AuthenticationCallback(){
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                navController.navigate(Routes.Home)
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                (context as? Activity)?.finish()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
            }
        })

        when(BiometricManager.from(context).canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)){
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                promptInfo = BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Unlock Streak Counter to continue")
                    .setAllowedAuthenticators(BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                    .build()
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                promptInfo = BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Unlock Streak Counter to continue")
                    .setAllowedAuthenticators(BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                    .build()
            }
        }

        biometricPrompt.authenticate(promptInfo)
    }

    LaunchedEffect(Unit) {
        delay(25)
        isAnimating = true
        delay(1000)
        if(isAppLockEnabled) ShowBiometricPrompt() else navController.navigate(Routes.Home)
    }

    AnimatedVisibility(
        visible = isAnimating,
        enter = fadeIn(
            tween(1000)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        listOf(
                            Color(0xFFFFA726),
                            Color(0xFFFF5722)
                        )
                    )
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.applogo),
                contentDescription = null,
                modifier = Modifier
                    .height(200.dp)
                    .width(200.dp)
                    .clip(CircleShape)
            )
            Spacer(Modifier.height(10.dp))
            Text("Streak Counter", fontSize = 30.sp)
        }
    }
}
