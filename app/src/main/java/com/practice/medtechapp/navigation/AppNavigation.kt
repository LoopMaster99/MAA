package com.practice.medtechapp.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.practice.medtechapp.data.Medication
import com.practice.medtechapp.screens.HistoryScreen
import com.practice.medtechapp.screens.HomeScreen
import com.practice.medtechapp.screens.MedicationsScreen
import com.practice.medtechapp.screens.OnboardingScreen
import com.practice.medtechapp.screens.ProfileScreen
import com.practice.medtechapp.screens.SignInScreen
import com.practice.medtechapp.screens.SignUpScreen
import com.practice.medtechapp.screens.SplashScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(navController: NavHostController) {

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("onboarding") { OnboardingScreen(navController) }
        //composable("Register") { RegisterScreen(navController) }
        composable("signIn") { SignInScreen(navController) }
        composable("signUp") { SignUpScreen(navController) }
        composable("home") {
            // Sample medications for today with status
            val medications = listOf(
                Medication("1", "Aspirin", "100mg", "8:00 AM", "pending"),
                Medication("2", "Insulin", "10 units", "9:00 AM", "pending"),
                Medication("3", "Blood Pressure", "5mg", "10:00 AM", "pending")
            )

            HomeScreen(
                userName = "Krishnamm/",
                medications = medications,
                onNavigateToHistory = {
                    // This could now be repurposed for some other function
                    // or kept as history view
                    navController.navigate("history")
                },
                onNavigateToSchedule = {
                    // This now navigates to the medications list/upcoming view
                    navController.navigate("medications")
                },
                onNavigateToProfile = {
                    navController.navigate("profile")
                }
            )
        }
        composable("medications") {
            // Medications/Schedule screen
            MedicationsScreen(
                onNavigateToHome = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                onNavigateToProfile = {
                    navController.navigate("profile")
                }
            )
        }
        composable("profile") {
            ProfileScreen(
                onNavigateToHome = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                onNavigateToMedications = {
                    navController.navigate("medications")
                }
            )
        }
        // Keep history screen if you're still using it
        composable("history") {
            HistoryScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}