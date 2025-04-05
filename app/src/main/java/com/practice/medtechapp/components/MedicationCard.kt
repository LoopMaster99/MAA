package com.practice.medtechapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.practice.medtechapp.ui.theme.CardBackground
import com.practice.medtechapp.ui.theme.CardBorder
import com.practice.medtechapp.ui.theme.TextColor

@Composable
fun MedicationCard(
    medicationName: String,
    time: String,
    modifier: Modifier = Modifier,
    status: String? = null // Can be "taken", "missed", or null for pending
) {
    // Determine the card's background color based on medication status
    val cardColor = when (status) {
        "taken" -> Color(0xFFE8F5E9) // Light green for taken medications
        "missed" -> Color(0xFFFFEBEE) // Light red for missed medications
        else -> Color(0xFFF8F3EA)      // Custom background for pending medications
    }

    val borderColor = when (status) {
        "taken" -> Color(0xFF81C784) // Medium green border
        "missed" -> Color(0xFFEF9A9A) // Medium red border
        else -> Color(0xFFC5B39A)     // Darker border than CardBorder
    }

    Card(
        modifier = modifier
            .fillMaxWidth(0.9f)
            .padding(vertical = 6.dp)
            .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp), // Increased elevation
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = medicationName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextColor  // Use your custom text color defined in your theme
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Time: $time",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                // Display medication status if available
                if (status != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(
                                    when (status) {
                                        "taken" -> Color(0xFF4CAF50)
                                        "missed" -> Color(0xFFE53935)
                                        else -> Color.Gray
                                    }
                                )
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = when (status) {
                                "taken" -> "Taken"
                                "missed" -> "Missed"
                                else -> "Pending"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = when (status) {
                                "taken" -> Color(0xFF4CAF50)
                                "missed" -> Color(0xFFE53935)
                                else -> Color.Gray
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MedicationCardPreview() {
    MedicationCard(
        medicationName = "Aspirin",
        time = "8:00 AM",
        status = "taken" // Example status
    )
}
