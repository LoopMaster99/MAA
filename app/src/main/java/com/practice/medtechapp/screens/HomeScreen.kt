package com.practice.medtechapp.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.practice.medtechapp.R
import com.practice.medtechapp.components.MedicationIntakeSlider
import com.practice.medtechapp.data.Medication
import com.practice.medtechapp.ui.theme.BackgroundLight
import com.practice.medtechapp.ui.theme.PrimaryDark
import com.practice.medtechapp.ui.theme.PrimaryLight
import com.practice.medtechapp.ui.theme.SecondaryDark
import com.practice.medtechapp.ui.theme.TextColor
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    userName: String,
    medications: List<Medication> = emptyList(),
    onNavigateToHistory: () -> Unit = {},
    onNavigateToSchedule: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    // Sample medications data (in a real app, this would come from your parameter)
    val medications = remember {
        listOf(
            Medication(
                "1", "Aspirin", "100mg", "8:00 AM"
            ),
            Medication(
                "2", "Insulin", "10 units", "9:00 AM"
            ),
            Medication(
                "3", "Blood Pressure", "5mg", "10:00 AM"
            ),
            Medication(
                "4", "Pain Killer", "500mg", "11:00 AM"
            )
        )
    }

    // State to track medication status for UI updates
    val medicationStatusMap = remember { mutableStateMapOf<String, String>() }

    // Message state for feedback to user
    var feedbackMessage by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = BackgroundLight,
        bottomBar = {
            NavigationBar(
                containerColor = PrimaryDark,
                contentColor = BackgroundLight
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = {
                        Icon(
                            Icons.Filled.Home,
                            contentDescription = "Home",
                            tint = if (selectedTab == 0) PrimaryLight else Color(0xFFA68763)
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PrimaryLight,
                        unselectedIconColor = Color(0xFFA68763),
                        indicatorColor = SecondaryDark
                    ),
                    label = { Text("Home", color = if (selectedTab == 0) PrimaryLight else Color(0xFFA68763)) }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = {
                        selectedTab = 1
                        onNavigateToSchedule()
                    },
                    icon = {
                        Icon(
                            Icons.Filled.DateRange,
                            contentDescription = "Schedule",
                            tint = if (selectedTab == 1) PrimaryLight else Color(0xFFA68763)
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PrimaryLight,
                        unselectedIconColor = Color(0xFFA68763),
                        indicatorColor = SecondaryDark
                    ),
                    label = { Text("Schedule", color = if (selectedTab == 1) PrimaryLight else Color(0xFFA68763)) }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = {
                        selectedTab = 2
                        onNavigateToProfile()
                    },
                    icon = {
                        Icon(
                            Icons.Filled.Person,
                            contentDescription = "Profile",
                            tint = if (selectedTab == 2) PrimaryLight else Color(0xFFA68763)
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PrimaryLight,
                        unselectedIconColor = Color(0xFFA68763),
                        indicatorColor = SecondaryDark
                    ),
                    label = { Text("Profile", color = if (selectedTab == 2) PrimaryLight else Color(0xFFA68763)) }
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToHistory,
                containerColor = SecondaryDark,
                contentColor = PrimaryLight,
                modifier = Modifier.size(50.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.history),
                    contentDescription = "History",
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
                .background(BackgroundLight)
        ) {
            // Welcome header
            Text(
                text = "Welcome, $userName 👋",
                style = MaterialTheme.typography.headlineMedium,
                color = TextColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Calendar/Timeline View
            CalendarTimelineView(
                selectedDate = selectedDate,
                onDateSelected = { newDate ->
                    selectedDate = newDate
                },
                medicationDays = (0..14).map { LocalDate.now().plusDays(it.toLong()) } // Days with medications
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Section header with date
            val formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d")
            val dateHeader = if (selectedDate == LocalDate.now()) {
                "Today's Medications"
            } else {
                "Medications for ${selectedDate.format(formatter)}"
            }

            Text(
                text = dateHeader,
                style = MaterialTheme.typography.titleLarge,
                color = PrimaryDark,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Enhanced medication intake slider
            MedicationIntakeSlider(
                medications = medications.filter { /* Filter for selected date */ true },
                onMedicationTaken = { medication ->
                    medicationStatusMap[medication.id] = "taken"
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = "You've taken ${medication.name} successfully!",
                            duration = SnackbarDuration.Short
                        )
                    }
                },
                onMedicationMissed = { medication ->
                    medicationStatusMap[medication.id] = "missed"
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = "You've missed ${medication.name}. This may affect your treatment.",
                            duration = SnackbarDuration.Long
                        )
                    }
                }
            )

            // Progress summary
            Spacer(modifier = Modifier.height(24.dp))
            val todayMeds = medications.filter { /* Filter for today */ true }
            val takenCount = medicationStatusMap.values.count { it == "taken" }
            val totalCount = todayMeds.size

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFE6D9C2)
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Today's Progress",
                        style = MaterialTheme.typography.titleMedium,
                        color = PrimaryDark
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { takenCount.toFloat() / if (totalCount > 0) totalCount.toFloat() else 1f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp),
                        color = Color(0xFF4CAF50),
                        trackColor = Color.LightGray
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "$takenCount/$totalCount medications taken today",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextColor
                    )
                }
            }

            // Upcoming medications section
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Upcoming Medications",
                style = MaterialTheme.typography.titleLarge,
                color = PrimaryDark,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Display next few upcoming medications
            UpcomingMedicationsSection(
                medications = medications.take(2),
                onMedicationTaken = { medication ->
                    medicationStatusMap[medication.id] = "taken"
                }
            )

            // Add some bottom padding
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarTimelineView(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    medicationDays: List<LocalDate>
) {
    // Create a list of the next 7 days
    val dates = remember {
        val today = LocalDate.now()
        (0..6).map { today.plusDays(it.toLong()) }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF5EFE6)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = "April 2025",
                style = MaterialTheme.typography.titleMedium,
                color = PrimaryDark
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                items(dates) { date ->
                    val isSelected = date == selectedDate
                    val hasMedications = medicationDays.contains(date)
                    val isToday = date == LocalDate.now()

                    DateItem(
                        date = date,
                        isSelected = isSelected,
                        isToday = isToday,
                        hasMedications = hasMedications,
                        onClick = { onDateSelected(date) }
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateItem(
    date: LocalDate,
    isSelected: Boolean,
    isToday: Boolean,
    hasMedications: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        isSelected -> SecondaryDark
        else -> Color.White
    }

    val textColor = when {
        isSelected -> Color.White
        isToday -> PrimaryDark
        else -> TextColor
    }

    val weekdayColor = when {
        isSelected -> Color.White
        isToday -> PrimaryDark
        else -> Color.Gray
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(48.dp)
            .clickable { onClick() }
            .background(backgroundColor, shape = CircleShape)
            .padding(vertical = 8.dp)
    ) {
        // Day of week (Mon, Tue, etc)
        Text(
            text = date.dayOfWeek.toString().take(3),
            style = MaterialTheme.typography.bodySmall,
            color = weekdayColor
        )

        // Day number
        Text(
            text = date.dayOfMonth.toString(),
            style = MaterialTheme.typography.titleMedium,
            color = textColor
        )

        // Dot indicator for medications
        if (hasMedications) {
            Spacer(modifier = Modifier.height(2.dp))
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(
                        color = if (isSelected) Color.White else PrimaryDark,
                        shape = CircleShape
                    )
            )
        }
    }
}

@Composable
fun UpcomingMedicationsSection(
    medications: List<Medication>,
    onMedicationTaken: (Medication) -> Unit
) {
    medications.forEach { medication ->
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(PrimaryLight, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = medication.name,
                            style = MaterialTheme.typography.titleMedium,
                            color = TextColor
                        )
                        Text(
                            text = "${medication.dose} at ${medication.time}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                }

                Button(
                    onClick = { onMedicationTaken(medication) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryDark
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text("Take")
                }
            }
        }
    }
}