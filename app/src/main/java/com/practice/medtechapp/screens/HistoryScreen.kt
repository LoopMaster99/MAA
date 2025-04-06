package com.practice.medtechapp.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practice.medtechapp.ui.theme.BackgroundLight
import com.practice.medtechapp.ui.theme.PrimaryDark
import com.practice.medtechapp.ui.theme.SecondaryDark
import com.practice.medtechapp.ui.theme.TextColor
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class MedicationHistory(
    val id: String,
    val name: String,
    val dosage: String,
    val time: String,
    val date: LocalDate,
    val status: String,
    val takenTime: String? = null
)

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    onNavigateBack: () -> Unit
) {
    val today = LocalDate.now()
    val yesterday = today.minusDays(1)
    val twoDaysAgo = today.minusDays(2)

    // Sample medication history
    val medicationHistory = remember {
        listOf(
            MedicationHistory("1", "Aspirin", "100mg", "8:00 AM", today, "completed", "8:05 AM"),
            MedicationHistory("2", "Insulin", "10 units", "12:00 PM", today, "missed"),
            MedicationHistory("3", "Lisinopril", "5mg", "8:00 PM", today, "pending"),
            MedicationHistory("4", "Aspirin", "100mg", "8:00 AM", yesterday, "completed", "8:15 AM"),
            MedicationHistory("5", "Insulin", "10 units", "12:00 PM", yesterday, "completed", "12:10 PM"),
            MedicationHistory("6", "Lisinopril", "5mg", "8:00 PM", yesterday, "completed", "8:05 PM"),
            MedicationHistory("7", "Aspirin", "100mg", "8:00 AM", twoDaysAgo, "completed", "8:30 AM"),
            MedicationHistory("8", "Insulin", "10 units", "12:00 PM", twoDaysAgo, "missed"),
            MedicationHistory("9", "Lisinopril", "5mg", "8:00 PM", twoDaysAgo, "completed", "8:20 PM")
        )
    }

    // Group medication history by date
    val groupedHistory = medicationHistory.groupBy { it.date }

    // Calculate adherence rate
    val totalMedications = medicationHistory.count { it.status != "pending" }
    val takenMedications = medicationHistory.count { it.status == "completed" }
    val adherenceRate = if (totalMedications > 0) {
        (takenMedications.toFloat() / totalMedications) * 100
    } else {
        0f
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Medication History", color = BackgroundLight) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go back",
                            tint = BackgroundLight
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryDark
                )
            )
        },
        containerColor = BackgroundLight
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            AdherenceCard(adherenceRate)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Medication History",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryDark,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                groupedHistory.forEach { (date, medications) ->
                    item {
                        val dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d")
                        val dateString = when {
                            date.isEqual(today) -> "Today, ${date.format(dateFormatter)}"
                            date.isEqual(yesterday) -> "Yesterday, ${date.format(dateFormatter)}"
                            else -> date.format(dateFormatter)
                        }

                        Text(
                            dateString,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SecondaryDark,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    items(medications) { medication ->
                        HistoryMedicationCard(medication)
                    }
                }
            }
        }
    }
}

@Composable
fun AdherenceCard(adherenceRate: Float) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE6D9C2)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                "Medication Adherence",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryDark
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "%.1f%%".format(adherenceRate),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = when {
                        adherenceRate >= 80 -> Color(0xFF4CAF50) // Good adherence
                        adherenceRate >= 50 -> Color(0xFFFFC107) // Moderate adherence
                        else -> Color(0xFFF44336) // Poor adherence
                    }
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    val adherenceMessage = when {
                        adherenceRate >= 80 -> "Great adherence! Keep it up."
                        adherenceRate >= 50 -> "Moderate adherence. Room for improvement."
                        else -> "Poor adherence. Try to take your medications on time."
                    }

                    Text(
                        adherenceMessage,
                        color = TextColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { adherenceRate / 100 },
                modifier = Modifier.fillMaxWidth(),
                color = when {
                    adherenceRate >= 80 -> Color(0xFF4CAF50)
                    adherenceRate >= 50 -> Color(0xFFFFC107)
                    else -> Color(0xFFF44336)
                },
                trackColor = Color.LightGray.copy(alpha = 0.3f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryMedicationCard(medication: MedicationHistory) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Status icon with background circle
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFFE6D9C2), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                when (medication.status) {
                    "completed" -> {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = "Completed",
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    "missed" -> {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = "Missed",
                            tint = Color(0xFFF44336),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    else -> {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = "Pending",
                            tint = Color(0xFFFFC107),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    medication.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryDark
                )

                Text(
                    "${medication.dosage} - ${medication.time}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                if (medication.status == "completed" && medication.takenTime != null) {
                    Text(
                        "Taken at: ${medication.takenTime}",
                        fontSize = 14.sp,
                        color = Color(0xFF4CAF50)
                    )
                } else if (medication.status == "missed") {
                    Text(
                        "Missed",
                        fontSize = 14.sp,
                        color = Color(0xFFF44336)
                    )
                }
            }
        }
    }
}