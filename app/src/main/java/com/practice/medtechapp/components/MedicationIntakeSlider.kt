package com.practice.medtechapp.components

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.practice.medtechapp.R
import com.practice.medtechapp.data.Medication
import com.practice.medtechapp.ui.theme.SecondaryDark
import com.practice.medtechapp.ui.theme.SecondaryLight
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun MedicationIntakeSlider(
    medications: List<Medication>,
    onMedicationTaken: (Medication) -> Unit,
    onMedicationMissed: (Medication) -> Unit
) {
    if (medications.isEmpty()) return

    var currentMedicationIndex by remember { mutableStateOf(0) }
    val currentMedication = medications[currentMedicationIndex]
    val coroutineScope = rememberCoroutineScope()

    // State for showing confirmation dialog
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var confirmationType by remember { mutableStateOf<String?>(null) }

    // Animated highlight position
    val highlightTransition = updateTransition(targetState = currentMedicationIndex, label = "highlightTransition")
    val highlightAlpha by highlightTransition.animateFloat(
        transitionSpec = { tween(300) },
        label = "highlightAlpha"
    ) { 1f }

    Column(modifier = Modifier.fillMaxWidth()) {
        // Dynamic medication cards
        medications.forEachIndexed { index, medication ->
            val isSelected = index == currentMedicationIndex

            Box(modifier = Modifier.fillMaxWidth()) {
                // The medication card
                MedicationCard(
                    medicationName = medication.name,
                    time = medication.scheduledTime,
                    status = medication.status,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )

                // Sliding indicator overlay (only visible for current medication)
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .padding(7.dp)
                            .alpha(highlightAlpha)
                            .border(
                                width = 2.dp,
                                color = SecondaryDark,
                                shape = RoundedCornerShape(12.dp)
                            )
                    )

                    // Swipe hints
                    Row(
                        modifier = Modifier
                            .matchParentSize()
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.light_bulb),
                            contentDescription = "Taken",
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Medication navigation with dot indicators
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            medications.forEachIndexed { index, _ ->
                DotIndicator(
                    selected = index == currentMedicationIndex,
                    onClick = { currentMedicationIndex = index }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Current medication action buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Taken button
            Button(
                onClick = {
                    confirmationType = "taken"
                    showConfirmationDialog = true
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier.weight(1f).padding(end = 8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.check_mark), // Replace with your PNG resource
                    contentDescription = "Taken",
                    modifier = Modifier.size(45.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Mark as Taken", color = SecondaryDark)
            }

            // Missed button
            Button(
                onClick = {
                    confirmationType = "missed"
                    showConfirmationDialog = true
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier.weight(1f).padding(start = 8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.cross2), // Replace with your PNG resource
                    contentDescription = "Missed",
                    modifier = Modifier.size(45.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Mark as Missed", color = SecondaryDark)
            }
        }
    }

    // Confirmation dialog
    if (showConfirmationDialog) {
        val isTaken = confirmationType == "taken"

        AlertDialog(
            onDismissRequest = { showConfirmationDialog = false },
            title = { Text(text = if (isTaken) "Confirm Medication Taken" else "Missed Medication") },
            text = {
                Text(
                    text = if (isTaken)
                        "Did you take ${currentMedication.name} (${currentMedication.dosage})?"
                    else
                        "Are you sure you missed your ${currentMedication.name}? This may affect your treatment."
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (isTaken) {
                            // Create a copy with updated status
                            val updatedMedication = currentMedication.copy(status = "taken")
                            onMedicationTaken(updatedMedication)
                        } else {
                            // Create a copy with updated status
                            val updatedMedication = currentMedication.copy(status = "missed")
                            onMedicationMissed(updatedMedication)
                        }

                        showConfirmationDialog = false

                        // Move to the next medication if available
                        coroutineScope.launch {
                            delay(300) // Small delay for visual feedback
                            if (currentMedicationIndex < medications.size - 1) {
                                currentMedicationIndex++
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isTaken) Color(0xFF4CAF50) else Color(0xFFE53935)
                    )
                ) {
                    Text(if (isTaken) "Yes, Confirm" else "Yes, I Missed It",
                        color = SecondaryLight)
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmationDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

// Dot indicator for medication navigation
@Composable
fun DotIndicator(
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .size(if (selected) 12.dp else 8.dp)
            .clip(CircleShape)
            .background(if (selected) Color(0xFF4CAF50) else Color.Gray.copy(alpha = 0.5f))
            .clickable(onClick = onClick)
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewMedicationIntakeSlider() {
    val sampleMedications = listOf(
        Medication("1", "Aspirin", "100mg", "8:00 AM" ),
        Medication("2", "Insulin", "10 units", "9:00 AM" ),
        Medication("3", "Blood Pressure Med", "5mg", "10:00 AM" ),
        Medication("4", "Pain Killer", "500mg", "11:00 AM" )
    )

    MedicationIntakeSlider(
        medications = sampleMedications,
        onMedicationTaken = { /* Handle taken action */ },
        onMedicationMissed = { /* Handle missed action */ }
    )
}