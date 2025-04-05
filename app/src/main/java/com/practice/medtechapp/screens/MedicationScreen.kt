package com.practice.medtechapp.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practice.medtechapp.components.MedicationCard
import com.practice.medtechapp.data.Medication
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationsScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val today = LocalDate.now()
    val tomorrow = today.plusDays(1)

    // Sample medication schedule data for upcoming days
    val medicationSchedule = remember {
        listOf(
            Medication("1", "Aspirin", "100mg", "8:00 AM", "pending"),
            Medication("2", "Insulin", "10 units", "12:00 PM", "pending"),
            Medication("3", "Lisinopril", "5mg", "8:00 PM", "pending"),
            Medication("4", "Aspirin", "100mg", "8:00 AM", "pending"),
            Medication("5", "Insulin", "10 units", "12:00 PM", "pending"),
            Medication("6", "Lisinopril", "5mg", "8:00 PM", "pending")
        )
    }

    // Group medications by date
    val groupedMedications = medicationSchedule.groupBy { LocalDate.parse(it.time, DateTimeFormatter.ofPattern("h:mm a")) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Medication Schedule") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        bottomBar = {
            BottomAppBar {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    IconButton(onClick = onNavigateToHome) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Home, contentDescription = "Home")
                            Text("Home", fontSize = 12.sp)
                        }
                    }
                    IconButton(onClick = {}) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.DateRange,
                                contentDescription = "Schedule",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                "Schedule",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    IconButton(onClick = onNavigateToProfile) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Person, contentDescription = "Profile")
                            Text("Profile", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                "Your Upcoming Medications",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                groupedMedications.forEach { (date, medications) ->
                    item {
                        val dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d")
                        val dateString = if (date.isEqual(today)) {
                            "Today, ${date.format(dateFormatter)}"
                        } else if (date.isEqual(tomorrow)) {
                            "Tomorrow, ${date.format(dateFormatter)}"
                        } else {
                            date.format(dateFormatter)
                        }

                        Text(
                            dateString,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    items(medications) { medication ->
                        MedicationCard(medication, isSelected = false)
                    }
                }
            }
        }
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun MedicationCard(medication: MedicationSchedule) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 4.dp),
//        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
//    ) {
//        Row(
//            modifier = Modifier
//                .padding(16.dp)
//                .fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Icon(
//                imageVector = Icons.Default.Medication,
//                contentDescription = null,
//                tint = MaterialTheme.colorScheme.primary,
//                modifier = Modifier.size(40.dp)
//            )
//
//            Spacer(modifier = Modifier.width(16.dp))
//
//            Column(modifier = Modifier.weight(1f)) {
//                Text(
//                    medication.name,
//                    fontSize = 18.sp,
//                    fontWeight = FontWeight.Bold
//                )
//                Text(
//                    "${medication.dosage} - ${medication.time}",
//                    fontSize = 14.sp,
//                    color = Color.Gray
//                )
//            }
//
//            when (medication.status) {
//                "completed" -> {
//                    Icon(
//                        Icons.Default.CheckCircle,
//                        contentDescription = "Completed",
//                        tint = Color(0xFF4CAF50)
//                    )
//                }
//                "missed" -> {
//                    Icon(
//                        Icons.Default.Cancel,
//                        contentDescription = "Missed",
//                        tint = Color(0xFFF44336)
//                    )
//                }
//                else -> {
//                    OutlinedButton(onClick = { /* Mark as taken */ }) {
//                        Text("Take Now")
//                    }
//                }
//            }
//        }
//    }
//}