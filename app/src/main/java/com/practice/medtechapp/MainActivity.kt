package com.practice.medtechapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.practice.medtechapp.data.Medication
import com.practice.medtechapp.navigation.AppNavigation
import com.practice.medtechapp.ui.theme.MyMedTechTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyMedTechTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    val medications = listOf(
                        Medication("1", "Aspirin", "100mg", "8:00 AM", "pending"),
                        Medication("2", "Insulin", "10 units", "9:00 AM", "pending"),
                        Medication("3", "Blood Pressure", "5mg", "10:00 AM", "pending")
                    )
                    AppNavigation(navController = navController,
                        medications = medications)
                }
            }
        }
    }
}

