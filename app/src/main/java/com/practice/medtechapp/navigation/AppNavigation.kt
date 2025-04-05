package com.practice.medtechapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.practice.medtechapp.data.Medication
import com.practice.medtechapp.screens.HomeScreen
import com.practice.medtechapp.screens.OnboardingScreen
import com.practice.medtechapp.screens.SignInScreen
import com.practice.medtechapp.screens.SignUpScreen
import com.practice.medtechapp.screens.SplashScreen

@Composable
fun AppNavigation(navController: NavHostController) {

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("onboarding") { OnboardingScreen(navController) }
        composable("Register") { RegisterScreen(navController) }
        composable("signIn") { SignInScreen(navController) }
        composable("signUp") { SignUpScreen(navController) }
        composable("home") {
            // Assuming you have a way to get the list of medications
            val medications = listOf(
                Medication("1", "Aspirin", "100mg", "8:00 AM"),
                Medication("2", "Insulin", "10 units", "9:00 AM"),
                Medication("3", "Blood Pressure", "5mg", "10:00 AM")
            )

            HomeScreen(
                userName = "Krishnamm/",
                medications = medications,
                onNavigateToHistory = {
                    // Logic to navigate to history screen
                    navController.navigate("history")
                },
                onNavigateToSchedule = {
                    // Logic to navigate to schedule screen
                    navController.navigate("schedule")
                },
                onNavigateToProfile = {
                    // Logic to navigate to profile screen
                    navController.navigate("profile")
                }
            )
        }

    }
}
