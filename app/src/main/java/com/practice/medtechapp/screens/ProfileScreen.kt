package com.practice.medtechapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToMedications: () -> Unit
) {
    val userDetails = remember {
        UserProfile(
            name = "Krishna M",
            email = "krishna@example.com",
            phone = "+1 (555) 123-4567",
            emergencyContact = "Rahul M: +1 (555) 765-4321",
            doctorInfo = "Dr. Sarah Johnson: +1 (555) 987-6543"
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
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
                    IconButton(onClick = onNavigateToMedications) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.DateRange, contentDescription = "Schedule")
                            Text("Schedule", fontSize = 12.sp)
                        }
                    }
                    IconButton(onClick = {}) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = "Profile",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                "Profile",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                ProfileHeader(userDetails.name)
                Spacer(modifier = Modifier.height(24.dp))
                PersonalInformationSection(userDetails)
                Spacer(modifier = Modifier.height(16.dp))
                Divider()
                Spacer(modifier = Modifier.height(16.dp))
                MedicalInformationSection()
                Spacer(modifier = Modifier.height(16.dp))
                Divider()
                Spacer(modifier = Modifier.height(16.dp))
                AppSettingsSection()
                Spacer(modifier = Modifier.height(16.dp))
                Divider()
                Spacer(modifier = Modifier.height(16.dp))
                LogoutButton()
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

data class UserProfile(
    val name: String,
    val email: String,
    val phone: String,
    val emergencyContact: String,
    val doctorInfo: String
)

@Composable
fun ProfileHeader(name: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Person,
                contentDescription = "Profile Picture",
                modifier = Modifier.size(60.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            name,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        OutlinedButton(
            onClick = { /* Edit profile action */ },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Icon(Icons.Default.Edit, contentDescription = "Edit")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Edit Profile")
        }
    }
}

@Composable
fun PersonalInformationSection(userProfile: UserProfile) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            "Personal Information",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        ProfileInfoItem(
            icon = Icons.Default.Email,
            title = "Email",
            value = userProfile.email
        )

        ProfileInfoItem(
            icon = Icons.Default.Phone,
            title = "Phone",
            value = userProfile.phone
        )

        ProfileInfoItem(
            icon = Icons.Default.Call,
            title = "Emergency Contact",
            value = userProfile.emergencyContact
        )

        ProfileInfoItem(
            icon = Icons.Default.Build,
            title = "Doctor",
            value = userProfile.doctorInfo
        )
    }
}

@Composable
fun MedicalInformationSection() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            "Medical Information",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        SettingsItem(
            icon = Icons.Default.Person,
            title = "My Medications",
            onClick = { /* Navigate to medications list */ }
        )

        SettingsItem(
            icon = Icons.Default.Favorite,
            title = "Health Conditions",
            onClick = { /* Navigate to health conditions */ }
        )

        SettingsItem(
            icon = Icons.Default.AddCircle,
            title = "Allergies",
            onClick = { /* Navigate to allergies */ }
        )
    }
}

@Composable
fun AppSettingsSection() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            "App Settings",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        SettingsItem(
            icon = Icons.Default.Notifications,
            title = "Reminders & Notifications",
            onClick = { /* Navigate to notifications settings */ }
        )

        SettingsItem(
            icon = Icons.Default.Lock,
            title = "Privacy & Security",
            onClick = { /* Navigate to privacy settings */ }
        )

        SettingsItem(
            icon = Icons.Default.Info,
            title = "About & Help",
            onClick = { /* Navigate to about page */ }
        )
    }
}

@Composable
fun ProfileInfoItem(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
        ) {
            Text(
                title,
                fontSize = 14.sp,
                color = Color.Gray
            )

            Text(
                value,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun SettingsItem(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier.run {
            fillMaxWidth()
                .padding(vertical = 12.dp)
                .clickable(onClick = onClick)
        },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )

        Text(
            title,
            fontSize = 16.sp,
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
        )

        Icon(
            Icons.Default.KeyboardArrowRight,
            contentDescription = "Navigate",
            tint = Color.Gray
        )
    }
}

@Composable
fun LogoutButton() {
    Button(
        onClick = { /* Logout action */ },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.error
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(Icons.Default.Clear, contentDescription = "Logout")
        Spacer(modifier = Modifier.width(8.dp))
        Text("Logout")
    }
}