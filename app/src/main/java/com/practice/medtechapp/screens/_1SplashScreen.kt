package com.practice.medtechapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.practice.medtechapp.ui.theme.AppTypography
import com.practice.medtechapp.ui.theme.PrimaryDark
import com.practice.medtechapp.ui.theme.SecondaryLight

@Composable
fun SplashScreen(navController: NavController) {
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("splash_animation.json"))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = 1
    )

    LaunchedEffect(progress) {
        if (progress == 1f) {
            navController.navigate("onboarding")
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(SecondaryLight), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier.size(200.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "MAA - My Alerting Ally",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryDark,
                textAlign = TextAlign.Center,
                style = AppTypography.titleLarge
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(navController = rememberNavController())
}


