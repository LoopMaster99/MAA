package com.practice.medtechapp.components

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.unit.dp
import com.practice.medtechapp.data.Medication
import com.practice.medtechapp.ui.theme.PrimaryDark
import com.practice.medtechapp.ui.theme.SecondaryDark
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun MedicationIntakeSlider(
    medications: List<Medication>,
    onMedicationTaken: (Medication) -> Unit,
    onMedicationMissed: (Medication) -> Unit
) {
    if (medications.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFF5F5F5))
                .border(1.dp, Color(0xFFEEEEEE), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color(0xFFBDBDBD),
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "No medications scheduled",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF9E9E9E)
                )
            }
        }
        return
    }

    var currentMedicationIndex by remember { mutableStateOf(0) }
    val currentMedication = medications[currentMedicationIndex]
    val coroutineScope = rememberCoroutineScope()

    var showConfirmationDialog by remember { mutableStateOf(false) }
    var confirmationType by remember { mutableStateOf<String?>(null) }

    val highlightTransition = updateTransition(targetState = currentMedicationIndex, label = "highlightTransition")
    val highlightAlpha by highlightTransition.animateFloat(
        transitionSpec = { tween(300) },
        label = "highlightAlpha"
    ) { 1f }

    Column(modifier = Modifier.fillMaxWidth()) {
        medications.forEachIndexed { index, medication ->
            val isSelected = index == currentMedicationIndex

            Box(modifier = Modifier.fillMaxWidth()) {

                MedicationCard(
                    medication = medication,
                    isSelected = isSelected,
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

                    // Swipe hints - now with more subtle visual guidance
                    Row(
                        modifier = Modifier
                            .matchParentSize()
                            .padding(horizontal = 12.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Swipe to act",
                            tint = SecondaryDark.copy(alpha = 0.5f),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

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

        // Current medication action buttons - redesigned for better visual hierarchy
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5EFE6)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Taken button - now more visually distinct
                Button(
                    onClick = {
                        confirmationType = "taken"
                        showConfirmationDialog = true
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Taken",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Mark as Taken", color = Color.White)
                }

                // Missed button
                OutlinedButton(
                    onClick = {
                        confirmationType = "missed"
                        showConfirmationDialog = true
                    },
                    border = BorderStroke(1.dp, Color(0xFFE53935)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f).padding(start = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Missed",
                        tint = Color(0xFFE53935)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Mark as Missed", color = Color(0xFFE53935))
                }
            }
        }
    }


    if (showConfirmationDialog) {
        val isTaken = confirmationType == "taken"

        AlertDialog(
            onDismissRequest = { showConfirmationDialog = false },
            title = {
                Text(
                    text = if (isTaken) "Confirm Medication Taken" else "Missed Medication",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                Column {
                    Text(
                        text = currentMedication.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = PrimaryDark
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${currentMedication.dose} at ${currentMedication.time}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isTaken)
                            "Did you take this medication?"
                        else
                            "Are you sure you missed your medication? This may affect your treatment."
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (isTaken) {

                            val updatedMedication = currentMedication.copy(status = "taken")
                            onMedicationTaken(updatedMedication)
                        } else {

                            val updatedMedication = currentMedication.copy(status = "missed")
                            onMedicationMissed(updatedMedication)
                        }

                        showConfirmationDialog = false


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
                    Text(
                        if (isTaken) "Yes, Confirm" else "Yes, I Missed It",
                        color = Color.White
                    )
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
