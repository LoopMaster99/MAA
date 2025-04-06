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
import com.practice.medtechapp.screens.RegisterScreen
import com.practice.medtechapp.screens.ScheduleScreen
import com.practice.medtechapp.screens.SignInScreen
import com.practice.medtechapp.screens.SignUpScreen
import com.practice.medtechapp.screens.SplashScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(navController: NavHostController, medications: List<Medication>) {

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("onboarding") { OnboardingScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("signIn") { SignInScreen(navController) }
        composable("signUp") { SignUpScreen(navController) }
        composable("home") {

            HomeScreen(
                userName = "Krishna",
                medications = medications,
                onNavigateToHistory = {

                    navController.navigate("history")
                },
                onNavigateToSchedule = {

                    navController.navigate("schedule")
                },
                onNavigateToProfile = {
                    navController.navigate("profile")
                }
            )
        }
        composable("schedule") {

            ScheduleScreen(
                medications = medications,  // Pass your medications list
                onNavigateBack = {
                    navController.popBackStack()
                },
                onUpdateMedication = { updatedMedication ->

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
                onNavigateToSchedule = {
                    navController.navigate("schedule")
                }
            )
        }

        composable("history") {
            HistoryScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}