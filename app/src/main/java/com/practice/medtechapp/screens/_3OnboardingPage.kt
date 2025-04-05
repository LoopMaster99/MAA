package com.practice.medtechapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practice.medtechapp.ui.theme.AppTypography
import com.practice.medtechapp.ui.theme.PrimaryDark
import com.practice.medtechapp.ui.theme.SecondaryDark

@Composable
fun OnboardingPage(pageData: OnboardingPageData) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = pageData.imageRes),
            contentDescription = null,
            modifier = Modifier
                .size(250.dp)
                .padding(bottom = 20.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = pageData.title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryDark,
            style = AppTypography.titleLarge,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = pageData.description,
            fontSize = 16.sp,
            color = SecondaryDark,
            style = AppTypography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

