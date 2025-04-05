package com.practice.medtechapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.pager.PagerState

@Composable
fun DotsIndicator(pagerState: PagerState) {
    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
        repeat(5) { index ->
            val color = if (index == pagerState.currentPage) Color.Blue else Color.Gray
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}
