package com.practice.medtechapp.screens

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.practice.medtechapp.data.Medication
import com.practice.medtechapp.ui.theme.BackgroundLight
import com.practice.medtechapp.ui.theme.PrimaryDark
import com.practice.medtechapp.ui.theme.PrimaryLight
import com.practice.medtechapp.ui.theme.SecondaryDark
import com.practice.medtechapp.ui.theme.TextColor
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("NewApi")
@Composable
fun ScheduleScreen(
    medications: List<Medication> = emptyList(),
    onNavigateBack: () -> Unit = {},
    onUpdateMedication: (Medication) -> Unit = {}
) {
    var calendarViewType by remember { mutableStateOf("day") }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // For medication editing
    var showEditDialog by remember { mutableStateOf(false) }
    var selectedMedication by remember { mutableStateOf<Medication?>(null) }

    // For reminder settings
    var showReminderDialog by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = BackgroundLight,
        topBar = {
            TopAppBar(
                title = { Text("Medication Schedule", color = PrimaryLight) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = PrimaryLight)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryDark
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .background(BackgroundLight)
        ) {
            // Feature 1: Calendar view type selector
            CalendarViewTypeSelector(
                currentViewType = calendarViewType,
                onViewTypeChanged = { calendarViewType = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Feature 2: Calendar visualization based on selected view type
            when (calendarViewType) {
                "day" -> DayView(
                    selectedDate = selectedDate,
                    medications = medications.filter {
                        // Filter medications for selected date
                        true // Replace with actual date filtering
                    },
                    onMedicationClick = { medication ->
                        selectedMedication = medication
                        showEditDialog = true
                    }
                )
                "week" -> WeekView(
                    selectedDate = selectedDate,
                    onDateSelected = { selectedDate = it },
                    medications = medications,
                    onMedicationClick = { medication ->
                        selectedMedication = medication
                        showEditDialog = true
                    }
                )
                "month" -> MonthView(
                    selectedDate = selectedDate,
                    onDateSelected = { selectedDate = it },
                    medications = medications
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Upcoming medication reminders section
            UpcomingMedicationReminders(
                medications = medications.take(3),
                onReminderSettingsClick = {
                    showReminderDialog = true
                }
            )
        }

        // Feature 3: Edit medication dialog (for rescheduling)
        if (showEditDialog && selectedMedication != null) {
            EditMedicationDialog(
                medication = selectedMedication!!,
                onDismiss = { showEditDialog = false },
                onSave = { updatedMedication ->
                    onUpdateMedication(updatedMedication)
                    showEditDialog = false
                    scope.launch {
                        snackbarHostState.showSnackbar("Medication updated successfully")
                    }
                }
            )
        }

        // Feature 4: Reminder settings dialog
        if (showReminderDialog) {
            ReminderSettingsDialog(
                onDismiss = { showReminderDialog = false },
                onSave = { reminderSettings ->
                    // Handle saving reminder settings
                    scope.launch {
                        snackbarHostState.showSnackbar("Reminder settings updated")
                    }
                    showReminderDialog = false
                }
            )
        }
    }
}

// Feature 1: View type selector (Day, Week, Month)
@Composable
fun CalendarViewTypeSelector(
    currentViewType: String,
    onViewTypeChanged: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        SelectableButton(
            text = "Day",
            isSelected = currentViewType == "day",
            onClick = { onViewTypeChanged("day") }
        )
        SelectableButton(
            text = "Week",
            isSelected = currentViewType == "week",
            onClick = { onViewTypeChanged("week") }
        )
        SelectableButton(
            text = "Month",
            isSelected = currentViewType == "month",
            onClick = { onViewTypeChanged("month") }
        )
    }
}

@Composable
fun SelectableButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) SecondaryDark else PrimaryLight,
            contentColor = if (isSelected) PrimaryLight else TextColor
        ),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.width(100.dp)
    ) {
        Text(text)
    }
}

// Feature 2: Day view implementation
@SuppressLint("NewApi")
@Composable
fun DayView(
    selectedDate: LocalDate,
    medications: List<Medication>,
    onMedicationClick: (Medication) -> Unit
) {
    val dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d")

    Column(modifier = Modifier.fillMaxWidth()) {
        // Date header with navigation
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* Navigate to previous day */ }) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Previous Day", tint = PrimaryDark)
            }

            Text(
                text = selectedDate.format(dateFormatter),
                style = MaterialTheme.typography.titleMedium,
                color = PrimaryDark
            )

            IconButton(onClick = { /* Navigate to next day */ }) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Next Day", tint = PrimaryDark)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Time slots with medications
        LazyColumn {
            // Morning medications
            item {
                TimeSlotSection(
                    title = "Morning (6 AM - 12 PM)",
                    medications = medications.filter { it.time.contains("AM") },
                    onMedicationClick = onMedicationClick
                )
            }

            // Afternoon medications
            item {
                TimeSlotSection(
                    title = "Afternoon (12 PM - 6 PM)",
                    medications = medications.filter {
                        it.time.contains("PM") && !it.time.contains("9:00") && !it.time.contains("10:00")
                    },
                    onMedicationClick = onMedicationClick
                )
            }

            // Evening medications
            item {
                TimeSlotSection(
                    title = "Evening (6 PM - 12 AM)",
                    medications = medications.filter {
                        it.time.contains("PM") && (it.time.contains("9:00") || it.time.contains("10:00"))
                    },
                    onMedicationClick = onMedicationClick
                )
            }
        }
    }
}

@Composable
fun TimeSlotSection(
    title: String,
    medications: List<Medication>,
    onMedicationClick: (Medication) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE6D9C2)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = PrimaryDark
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (medications.isEmpty()) {
                Text(
                    text = "No medications scheduled",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextColor.copy(alpha = 0.7f)
                )
            } else {
                medications.forEach { medication ->
                    MedicationTimeSlotItem(
                        medication = medication,
                        onClick = { onMedicationClick(medication) }
                    )
                }
            }
        }
    }
}

@Composable
fun MedicationTimeSlotItem(
    medication: Medication,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(PrimaryDark, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = medication.time,
                style = MaterialTheme.typography.labelSmall,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = medication.name,
                style = MaterialTheme.typography.titleSmall,
                color = TextColor
            )
            Text(
                text = medication.dose,
                style = MaterialTheme.typography.bodySmall,
                color = TextColor.copy(alpha = 0.7f)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = onClick) {
            Icon(
                Icons.Filled.Edit,
                contentDescription = "Edit medication",
                tint = SecondaryDark
            )
        }
    }
}

// Feature 2: Week view implementation (simplified)
@SuppressLint("NewApi")
@Composable
fun WeekView(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    medications: List<Medication>,
    onMedicationClick: (Medication) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Week header with dates
        val startOfWeek = selectedDate.with(DayOfWeek.MONDAY)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            (0..6).forEach { dayOffset ->
                val date = startOfWeek.plusDays(dayOffset.toLong())
                val isSelected = date == selectedDate
                val dayHasMedications = medications.any { /* check if date has medications */ true }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onDateSelected(date) }
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = date.dayOfWeek.toString().substring(0, 3),
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isSelected) SecondaryDark else TextColor
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(
                                if (isSelected) SecondaryDark else Color.Transparent,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = date.dayOfMonth.toString(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (isSelected) Color.White else TextColor
                        )
                    }

                    if (dayHasMedications) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(PrimaryDark, shape = CircleShape)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Show medications for selected day
        DayView(
            selectedDate = selectedDate,
            medications = medications.filter { /* Filter for selected date */ true },
            onMedicationClick = onMedicationClick
        )
    }
}

// Feature 2: Month view (simplified)
@SuppressLint("NewApi")
@Composable
fun MonthView(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    medications: List<Medication>
) {
    val yearMonth = YearMonth.from(selectedDate)
    val firstDayOfMonth = yearMonth.atDay(1)
    val lastDayOfMonth = yearMonth.atEndOfMonth()
    val firstDayOfWeek = firstDayOfMonth.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

    Column(modifier = Modifier.fillMaxWidth()) {
        // Month header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* Previous month */ }) {
                Icon(Icons.Filled.KeyboardArrowLeft, contentDescription = "Previous Month", tint = PrimaryDark)
            }

            Text(
                text = yearMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                style = MaterialTheme.typography.titleMedium,
                color = PrimaryDark
            )

            IconButton(onClick = { /* Next month */ }) {
                Icon(Icons.Filled.KeyboardArrowRight, contentDescription = "Next Month", tint = PrimaryDark)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Weekday headers
        Row(modifier = Modifier.fillMaxWidth()) {
            for (dayOfWeek in DayOfWeek.entries) {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = dayOfWeek.toString().substring(0, 1),
                        style = MaterialTheme.typography.bodySmall,
                        color = TextColor
                    )
                }
            }
        }

        // Calendar grid (simplified)
        var currentDate = firstDayOfWeek
        for (week in 0 until 6) {
            Row(modifier = Modifier.fillMaxWidth()) {
                for (day in 0 until 7) {
                    val isCurrentMonth = currentDate.month == selectedDate.month
                    val isSelected = currentDate == selectedDate
                    val dateHasMedications = medications.any { /* check if date has medications */ true }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(2.dp)
                            .background(
                                when {
                                    isSelected -> SecondaryDark
                                    isCurrentMonth -> PrimaryLight.copy(alpha = 0.3f)
                                    else -> Color.Transparent
                                },
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable(enabled = isCurrentMonth) {
                                if (isCurrentMonth) onDateSelected(currentDate)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = currentDate.dayOfMonth.toString(),
                                style = MaterialTheme.typography.bodySmall,
                                color = when {
                                    isSelected -> Color.White
                                    isCurrentMonth -> TextColor
                                    else -> TextColor.copy(alpha = 0.3f)
                                }
                            )

                            if (dateHasMedications && isCurrentMonth) {
                                Box(
                                    modifier = Modifier
                                        .size(4.dp)
                                        .background(
                                            if (isSelected) PrimaryLight else PrimaryDark,
                                            shape = CircleShape
                                        )
                                )
                            }
                        }
                    }

                    currentDate = currentDate.plusDays(1)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display a summary of medications for selected date
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE6D9C2)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Medications for ${selectedDate.format(DateTimeFormatter.ofPattern("MMM d"))}",
                    style = MaterialTheme.typography.titleMedium,
                    color = PrimaryDark
                )

                Spacer(modifier = Modifier.height(8.dp))

                val medsForSelectedDate = medications.filter { /* Filter for selected date */ true }
                if (medsForSelectedDate.isEmpty()) {
                    Text(
                        text = "No medications scheduled for this day",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextColor.copy(alpha = 0.7f)
                    )
                } else {
                    medsForSelectedDate.forEach { medication ->
                        Text(
                            text = "• ${medication.time} - ${medication.name} (${medication.dose})",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextColor,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

// Feature 3: Edit/Reschedule medication dialog
@Composable
fun EditMedicationDialog(
    medication: Medication,
    onDismiss: () -> Unit,
    onSave: (Medication) -> Unit
) {
    var name by remember { mutableStateOf(medication.name) }
    var dosage by remember { mutableStateOf(medication.dose) }
    var time by remember { mutableStateOf(medication.time) }
    var showTimePicker by remember { mutableStateOf(false) }

    // For Feature 5: Frequency settings
    var frequency by remember { mutableStateOf("daily") }
    var selectedDays by remember { mutableStateOf(setOf<DayOfWeek>()) }
    var showFrequencyDialog by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = BackgroundLight
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Edit Medication",
                    style = MaterialTheme.typography.titleLarge,
                    color = PrimaryDark
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Medication name
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Medication Name") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryDark,
                        unfocusedBorderColor = PrimaryDark.copy(alpha = 0.5f),
                        focusedLabelColor = PrimaryDark
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Dosage
                OutlinedTextField(
                    value = dosage,
                    onValueChange = { dosage = it },
                    label = { Text("Dosage") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryDark,
                        unfocusedBorderColor = PrimaryDark.copy(alpha = 0.5f),
                        focusedLabelColor = PrimaryDark
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Time picker
                OutlinedTextField(
                    value = time,
                    onValueChange = { },
                    label = { Text("Time") },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { showTimePicker = true }) {
                            Icon(Icons.Filled.Schedule, contentDescription = "Select time")
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryDark,
                        unfocusedBorderColor = PrimaryDark.copy(alpha = 0.5f),
                        focusedLabelColor = PrimaryDark
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Feature 5: Frequency settings
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showFrequencyDialog = true },
                    colors = CardDefaults.outlinedCardColors(
                        containerColor = Color(0xFFE6D9C2)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Frequency",
                                style = MaterialTheme.typography.titleSmall,
                                color = PrimaryDark
                            )

                            Text(
                                text = when (frequency) {
                                    "daily" -> "Every day"
                                    "weekly" -> "Weekly on specific days"
                                    "custom" -> "Custom schedule"
                                    else -> "As needed"
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextColor
                            )
                        }

                        Icon(Icons.Filled.KeyboardArrowRight, contentDescription = "Edit frequency", tint = PrimaryDark)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = PrimaryDark
                        )
                    ) {
                        Text("Cancel")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            val updatedMedication = Medication(
                                id = medication.id,
                                name = name,
                                dose = dosage,
                                time = time
                                // Add frequency and other properties as needed
                            )
                            onSave(updatedMedication)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SecondaryDark,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Save Changes")
                    }
                }
            }
        }
    }

    // Time picker dialog
    if (showTimePicker) {
        // In a real app, use a proper time picker dialog
        Dialog(onDismissRequest = { showTimePicker = false }) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = BackgroundLight
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Select Time",
                        style = MaterialTheme.typography.titleMedium,
                        color = PrimaryDark
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Simple time options for this example
                    Column {
                        listOf("8:00 AM", "9:00 AM", "12:00 PM", "3:00 PM", "6:00 PM", "9:00 PM").forEach { timeOption ->
                            Text(
                                text = timeOption,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        time = timeOption
                                        showTimePicker = false
                                    }
                                    .padding(vertical = 12.dp, horizontal = 16.dp),
                                color = if (time == timeOption) SecondaryDark else TextColor
                            )
                        }
                    }

                    Button(
                        onClick = { showTimePicker = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryDark
                        ),
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text("Close")
                    }
                }
            }
        }
    }

    // Frequency selector dialog
    if (showFrequencyDialog) {
        Dialog(onDismissRequest = { showFrequencyDialog = false }) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = BackgroundLight
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "Medication Frequency",
                        style = MaterialTheme.typography.titleMedium,
                        color = PrimaryDark
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Frequency options
                    FrequencyOption(
                        title = "Daily",
                        description = "Take every day",
                        isSelected = frequency == "daily",
                        onClick = { frequency = "daily" }
                    )

                    FrequencyOption(
                        title = "Weekly",
                        description = "Take on specific days of the week",
                        isSelected = frequency == "weekly",
                        onClick = { frequency = "weekly" }
                    )

                    FrequencyOption(
                        title = "As needed",
                        description = "Take only when necessary",
                        isSelected = frequency == "as_needed",
                        onClick = { frequency = "as_needed" }
                    )

                    FrequencyOption(
                        title = "Custom",
                        description = "Set a custom schedule",
                        isSelected = frequency == "custom",
                        onClick = { frequency = "custom" }
                    )

                    // Show day selector if weekly is selected
                    if (frequency == "weekly") {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Select days:",
                            style = MaterialTheme.typography.titleSmall,
                            color = PrimaryDark
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            DayOfWeek.entries.forEach { day ->
                                val isSelected = selectedDays.contains(day)

                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(
                                            if (isSelected) SecondaryDark else PrimaryLight.copy(alpha = 0.3f),
                                            shape = CircleShape
                                        )
                                        .clickable {
                                            selectedDays = if (isSelected) {
                                                selectedDays - day
                                            } else {
                                                selectedDays + day
                                            }
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = day.toString().substring(0, 1),
                                        color = if (isSelected) Color.White else TextColor
                                    )
                                }
                            }
                        }
                    }

                    // Custom frequency options would go here if selected

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { showFrequencyDialog = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SecondaryDark
                        ),
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Done")
                    }
                }
            }
        }
    }
}

@Composable
fun FrequencyOption(
    title: String,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = SecondaryDark,
                unselectedColor = PrimaryDark
            )
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = TextColor
            )

            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = TextColor.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun ReminderSettingsDialog(
    onDismiss: () -> Unit,
    onSave: (ReminderSettings) -> Unit
) {
    var notificationsEnabled by remember { mutableStateOf(true) }
    var remindBeforeMins by remember { mutableStateOf(15) }
    var reminderSound by remember { mutableStateOf("default") }
    var vibrateEnabled by remember { mutableStateOf(true) }

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = BackgroundLight
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Reminder Settings",
                    style = MaterialTheme.typography.titleLarge,
                    color = PrimaryDark
                )

                Spacer(modifier = Modifier.height(16.dp))


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Enable Notifications",
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextColor
                    )
                    Switch(
                        checked = notificationsEnabled,
                        onCheckedChange = { notificationsEnabled = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = PrimaryLight,
                            checkedTrackColor = PrimaryDark,
                            uncheckedThumbColor = Color.Gray,
                            uncheckedTrackColor = Color.LightGray
                        )
                    )
                }

                AnimatedVisibility(visible = notificationsEnabled) {
                    Column {
                        Spacer(modifier = Modifier.height(16.dp))

                        // Remind before minutes
                        Text(
                            text = "Remind ${remindBeforeMins} minutes before",
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextColor
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Slider(
                            value = remindBeforeMins.toFloat(),
                            onValueChange = { remindBeforeMins = it.toInt() },
                            valueRange = 5f..60f,
                            steps = 11,
                            colors = SliderDefaults.colors(
                                thumbColor = PrimaryLight,
                                activeTrackColor = PrimaryDark
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Reminder sound dropdown
                        Text(
                            text = "Notification Sound",
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextColor
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        var soundMenuExpanded by remember { mutableStateOf(false) }
                        val sounds = listOf("Default", "Bell", "Chime", "Silent")

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = 1.dp,
                                    color = PrimaryDark,
                                    shape = RoundedCornerShape(8.dp)
                                )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { soundMenuExpanded = true }
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = sounds.find { it.lowercase() == reminderSound } ?: "Default",
                                    color = TextColor
                                )
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Select sound",
                                    tint = PrimaryDark
                                )
                            }

                            DropdownMenu(
                                expanded = soundMenuExpanded,
                                onDismissRequest = { soundMenuExpanded = false },
                                modifier = Modifier.background(BackgroundLight)
                            ) {
                                sounds.forEach { sound ->
                                    DropdownMenuItem(
                                        text = { Text(sound) },
                                        onClick = {
                                            reminderSound = sound.lowercase()
                                            soundMenuExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Vibrate on notification
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Vibrate",
                                style = MaterialTheme.typography.bodyLarge,
                                color = TextColor
                            )
                            Switch(
                                checked = vibrateEnabled,
                                onCheckedChange = { vibrateEnabled = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = PrimaryLight,
                                    checkedTrackColor = PrimaryDark,
                                    uncheckedThumbColor = Color.Gray,
                                    uncheckedTrackColor = Color.LightGray
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = PrimaryDark
                        )
                    ) {
                        Text("Cancel")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            onSave(
                                ReminderSettings(
                                    enabled = notificationsEnabled,
                                    minutesBefore = remindBeforeMins,
                                    sound = reminderSound,
                                    vibrate = vibrateEnabled
                                )
                            )
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SecondaryDark,
                            contentColor = PrimaryLight
                        )
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}


data class ReminderSettings(
    val enabled: Boolean = true,
    val minutesBefore: Int = 15,
    val sound: String = "default",
    val vibrate: Boolean = true
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MedicationFrequencyDialog(
    onDismiss: () -> Unit,
    onSave: (MedicationFrequency) -> Unit,
    initialFrequency: MedicationFrequency = MedicationFrequency()
) {
    var frequencyType by remember { mutableStateOf(initialFrequency.type) }
    var timesPerDay by remember { mutableStateOf(initialFrequency.timesPerDay) }
    var selectedDays by remember { mutableStateOf(initialFrequency.daysOfWeek) }
    var interval by remember { mutableStateOf(initialFrequency.intervalDays) }

    val dayOptions = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = BackgroundLight
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Medication Frequency",
                    style = MaterialTheme.typography.titleLarge,
                    color = PrimaryDark
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Frequency type selector
                Column {
                    Text(
                        text = "How often?",
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextColor
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    FrequencyOption(
                        title = "Daily",
                        selected = frequencyType == FrequencyType.DAILY,
                        onClick = { frequencyType = FrequencyType.DAILY }
                    )

                    FrequencyOption(
                        title = "Specific Days",
                        selected = frequencyType == FrequencyType.SPECIFIC_DAYS,
                        onClick = { frequencyType = FrequencyType.SPECIFIC_DAYS }
                    )

                    FrequencyOption(
                        title = "Every X Days",
                        selected = frequencyType == FrequencyType.INTERVAL,
                        onClick = { frequencyType = FrequencyType.INTERVAL }
                    )

                    FrequencyOption(
                        title = "As Needed",
                        selected = frequencyType == FrequencyType.AS_NEEDED,
                        onClick = { frequencyType = FrequencyType.AS_NEEDED }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Additional options based on frequency type
                AnimatedVisibility(visible = frequencyType == FrequencyType.DAILY) {
                    Column {
                        Text(
                            text = "Times per day: $timesPerDay",
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextColor
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Slider(
                            value = timesPerDay.toFloat(),
                            onValueChange = { timesPerDay = it.toInt() },
                            valueRange = 1f..4f,
                            steps = 3,
                            colors = SliderDefaults.colors(
                                thumbColor = PrimaryLight,
                                activeTrackColor = PrimaryDark
                            )
                        )
                    }
                }

                AnimatedVisibility(visible = frequencyType == FrequencyType.SPECIFIC_DAYS) {
                    Column {
                        Text(
                            text = "Select days:",
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextColor
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            maxItemsInEachRow = 4
                        ) {
                            dayOptions.forEachIndexed { index, day ->
                                FilterChip(
                                    selected = selectedDays.contains(index),
                                    onClick = {
                                        selectedDays = if (selectedDays.contains(index)) {
                                            selectedDays - index
                                        } else {
                                            selectedDays + index
                                        }
                                    },
                                    label = { Text(day) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        containerColor = Color.Transparent,
                                        selectedContainerColor = PrimaryDark.copy(alpha = 0.2f),
                                        labelColor = TextColor,
                                        selectedLabelColor = PrimaryDark
                                    ),
                                    border = FilterChipDefaults.filterChipBorder(
                                        enabled = true,
                                        selected = selectedDays.contains(index),
                                        borderColor = PrimaryDark.copy(alpha = 0.5f),
                                        selectedBorderColor = PrimaryDark
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Times per day on selected days: $timesPerDay",
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextColor
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Slider(
                            value = timesPerDay.toFloat(),
                            onValueChange = { timesPerDay = it.toInt() },
                            valueRange = 1f..4f,
                            steps = 3,
                            colors = SliderDefaults.colors(
                                thumbColor = PrimaryLight,
                                activeTrackColor = PrimaryDark
                            )
                        )
                    }
                }

                AnimatedVisibility(visible = frequencyType == FrequencyType.INTERVAL) {
                    Column {
                        Text(
                            text = "Every $interval days",
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextColor
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Slider(
                            value = interval.toFloat(),
                            onValueChange = { interval = it.toInt() },
                            valueRange = 2f..30f,
                            steps = 28,
                            colors = SliderDefaults.colors(
                                thumbColor = PrimaryLight,
                                activeTrackColor = PrimaryDark
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = PrimaryDark
                        )
                    ) {
                        Text("Cancel")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            onSave(
                                MedicationFrequency(
                                    type = frequencyType,
                                    timesPerDay = timesPerDay,
                                    daysOfWeek = selectedDays,
                                    intervalDays = interval
                                )
                            )
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SecondaryDark,
                            contentColor = PrimaryLight
                        )
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}

@Composable
private fun FrequencyOption(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.RadioButton
            )
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
            colors = RadioButtonDefaults.colors(
                selectedColor = PrimaryDark,
                unselectedColor = TextColor.copy(alpha = 0.6f)
            )
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = TextColor
        )
    }
}

// Data classes for medication frequency
enum class FrequencyType {
    DAILY, SPECIFIC_DAYS, INTERVAL, AS_NEEDED
}

data class MedicationFrequency(
    val type: FrequencyType = FrequencyType.DAILY,
    val timesPerDay: Int = 1,
    val daysOfWeek: Set<Int> = setOf(0, 1, 2, 3, 4, 5, 6), // 0 = Sunday, 1 = Monday, etc.
    val intervalDays: Int = 2 // For "Every X days" frequency
)

@Composable
fun UpcomingMedicationReminders(
    medications: List<Medication>,
    onReminderSettingsClick: (Medication) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE6D9C2)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            if (medications.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No upcoming medications",
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextColor.copy(alpha = 0.7f)
                    )
                }
            } else {
                medications.forEach { medication ->
                    MedicationReminderItem(
                        medication = medication,
                        onSettingsClick = { onReminderSettingsClick(medication) }
                    )

                    if (medication != medications.last()) {
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = PrimaryDark.copy(alpha = 0.2f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MedicationReminderItem(
    medication: Medication,
    onSettingsClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Time indicator
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    color = PrimaryDark,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = medication.time.split(" ")[0],
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.width(16.dp))


        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = medication.name,
                style = MaterialTheme.typography.titleMedium,
                color = PrimaryDark,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = medication.dose,
                style = MaterialTheme.typography.bodyMedium,
                color = TextColor.copy(alpha = 0.8f)
            )


            Text(
                text = "Due at ${medication.time}",
                style = MaterialTheme.typography.bodySmall,
                color = TextColor.copy(alpha = 0.6f)
            )
        }

        // Settings button
        IconButton(
            onClick = onSettingsClick,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Reminder settings for ${medication.name}",
                tint = SecondaryDark
            )
        }
    }
}


@Composable
fun MedicationRemindersSection(
    medications: List<Medication>
) {
    var showReminderDialog by remember { mutableStateOf(false) }
    var selectedMedication by remember { mutableStateOf<Medication?>(null) }
    var showFrequencyDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Upcoming Medication Reminders",
            style = MaterialTheme.typography.titleLarge,
            color = PrimaryDark,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        UpcomingMedicationReminders(
            medications = medications,
            onReminderSettingsClick = { medication ->
                selectedMedication = medication
                showReminderDialog = true
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { showFrequencyDialog = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = SecondaryDark,
                contentColor = PrimaryLight
            ),
            modifier = Modifier.align(Alignment.End)
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit frequency",
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Edit Medication Schedule")
        }
    }

    // Show reminder settings dialog when requested
    if (showReminderDialog && selectedMedication != null) {
        ReminderSettingsDialog(
            onDismiss = { showReminderDialog = false },
            onSave = { reminderSettings ->

                showReminderDialog = false
            }
        )
    }


    if (showFrequencyDialog) {
        MedicationFrequencyDialog(
            onDismiss = { showFrequencyDialog = false },
            onSave = { frequency ->

                showFrequencyDialog = false
            }
        )
    }
}
