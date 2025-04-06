package com.practice.medtechapp.screens

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.practice.medtechapp.R

data class OnboardingPageData(
    @DrawableRes val imageRes: Int,
    val title: String,
    val description: String
)

val onboardingPages = listOf(
    OnboardingPageData(
        R.drawable.capsule,
        "Welcome",
        "Easily track and manage your medications with smart reminders."
    ),
    OnboardingPageData(
        R.drawable.notify,
        "Stay on Schedule",
        "Receive timely alerts so you never miss a dose."
    ),
    OnboardingPageData(
        R.drawable.clock,
        "Monitor Your Progress",
        "Track your medication history and ensure proper adherence."
    ),
    OnboardingPageData(
        R.drawable.stethoscope,
        "Scan Medications",
        "Quickly scan medicines to get detailed information and dosage instructions."
    ),
    OnboardingPageData(
        R.drawable.result,
        "Secure and Easy",
        "Your data is safe, and the app is designed for effortless use."
    )
)

