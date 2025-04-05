package com.practice.medtechapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
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

@Composable
fun HomeScreen(
    userName: String,
    onNavigateToHistory: () -> Unit = {},
    onNavigateToSchedule: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    medications: List<Medication>
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    // Sample medications data
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
                containerColor = PrimaryDark, // Dark background for better contrast
                contentColor = BackgroundLight // Light content on dark background
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = {
                        Icon(
                            Icons.Filled.Home,
                            contentDescription = "Home",
                            tint = if (selectedTab == 0) PrimaryDark else PrimaryLight
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PrimaryLight, // Lighter color for selected
                        unselectedIconColor = Color(0xFFA68763), // Medium tone for unselected
                        indicatorColor = SecondaryDark // Darker indicator
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = {
                        selectedTab = 1
                        //onNavigateToSchedule()
                    },
                    icon = {
                        Icon(
                            Icons.Filled.DateRange,
                            contentDescription = "Schedule",
                            tint = if (selectedTab == 1) PrimaryDark else PrimaryLight
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PrimaryLight, // Lighter color for selected
                        unselectedIconColor = Color(0xFFA68763), // Medium tone for unselected
                        indicatorColor = SecondaryDark // Darker indicator
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = {
                        selectedTab = 2
                        //onNavigateToProfile()
                    },
                    icon = {
                        Icon(Icons.Filled.Person,
                            contentDescription = "Profile",
                            tint = if (selectedTab == 2) PrimaryDark else PrimaryLight
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PrimaryLight, // Lighter color for selected
                        unselectedIconColor = Color(0xFFA68763), // Medium tone for unselected
                        indicatorColor = SecondaryDark // Darker indicator
                    )
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .background(BackgroundLight)
        ) {
            // Responsive welcome text with dynamic sizing
            Text(
                text = "Welcome, $userName 👋",
                style = MaterialTheme.typography.headlineMedium,
                color = TextColor,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Section header with more prominent styling
            Text(
                text = "Today's Medications",
                style = MaterialTheme.typography.titleLarge,
                color = PrimaryDark,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // New integrated medication intake slider
            MedicationIntakeSlider(
                medications = medications,
                onMedicationTaken = { medication ->
                    // Update medication status
                    medicationStatusMap[medication.id] = "taken"

                    // Show feedback
                    feedbackMessage = "You've taken ${medication.name} successfully!"
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = "You've taken ${medication.name} successfully!",
                            duration = SnackbarDuration.Short
                        )
                    }
                },
                onMedicationMissed = { medication ->
                    // Update medication status
                    medicationStatusMap[medication.id] = "missed"

                    // Show warning feedback
                    feedbackMessage = "You've missed ${medication.name}. This may affect your treatment."
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
            val takenCount = medicationStatusMap.values.count { it == "taken" }
            val totalCount = medications.size

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
                        progress = { takenCount.toFloat() / totalCount.toFloat() },
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
        }
    }
}