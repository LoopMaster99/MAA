package com.practice.medtechapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.practice.medtechapp.data.Medication
import com.practice.medtechapp.ui.theme.PrimaryDark
import com.practice.medtechapp.ui.theme.PrimaryLight
import com.practice.medtechapp.ui.theme.TextColor

@Composable
fun MedicationCard(
    medication: Medication,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    val cardColor = when(medication.status) {
        "taken" -> Color(0xFFE8F5E9)
        "missed" -> Color(0xFFFFEBEE)
        else -> Color.White
    }

    val timeColor = when {
        medication.time.contains("now", ignoreCase = true) -> Color(0xFFE53935)
        medication.time.contains("soon", ignoreCase = true) -> Color(0xFFFF9800)
        else -> Color(0xFF616161)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 80.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 1.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(PrimaryLight.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = null,
                    tint = PrimaryDark,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = medication.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = TextColor
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = medication.dose,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF757575)
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = medication.time,
                    style = MaterialTheme.typography.bodyMedium,
                    color = timeColor,
                    fontWeight = FontWeight.Bold
                )

                if (medication.status == "taken" || medication.status == "missed") {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (medication.status == "taken")
                                Icons.Default.Check else Icons.Default.Close,
                            contentDescription = null,
                            tint = if (medication.status == "taken")
                                Color(0xFF4CAF50) else Color(0xFFE53935),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (medication.status == "taken") "Taken" else "Missed",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (medication.status == "taken")
                                Color(0xFF4CAF50) else Color(0xFFE53935)
                        )
                    }
                }
            }
        }
    }
}