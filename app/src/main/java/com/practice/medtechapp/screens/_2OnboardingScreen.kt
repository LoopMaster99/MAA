package com.practice.medtechapp.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.launch
import com.practice.medtechapp.components.DotsIndicator
import com.practice.medtechapp.ui.theme.AppTypography
import com.practice.medtechapp.ui.theme.BackgroundLight
import com.practice.medtechapp.ui.theme.PrimaryDark
import com.practice.medtechapp.ui.theme.SecondaryDark

@Composable
fun OnboardingScreen(navController: NavController) {
    val pagerState = rememberPagerState(pageCount = { onboardingPages.size })
    val coroutineScope = rememberCoroutineScope()
    
    Column(
        modifier = Modifier.fillMaxSize().background(BackgroundLight),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            OnboardingPage(onboardingPages[page])
        }

        Spacer(modifier = Modifier.height(16.dp))

        DotsIndicator(pagerState = pagerState)

        Spacer(modifier = Modifier.height(24.dp))


        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (pagerState.currentPage != onboardingPages.lastIndex) {
                TextButton(onClick = { navController.navigate("signIn" ) }) {
                    Text("Skip",
                        color = SecondaryDark,
                        style = AppTypography.bodyLarge)
                }
            }

            Button(
                onClick = {
                    coroutineScope.launch {
                        if (pagerState.currentPage < onboardingPages.lastIndex) {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        } else {
                            navController.navigate("signIn")
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryDark)
            ) {
                Text(
                    if (pagerState.currentPage == onboardingPages.lastIndex) "Get Started" else "Next",
                    color = Color.White,
                    style = AppTypography.bodyLarge
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
