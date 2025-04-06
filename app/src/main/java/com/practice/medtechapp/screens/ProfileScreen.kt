package com.practice.medtechapp.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonPin
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.practice.medtechapp.ui.theme.BackgroundLight
import com.practice.medtechapp.ui.theme.PrimaryDark
import com.practice.medtechapp.ui.theme.PrimaryLight
import com.practice.medtechapp.ui.theme.SecondaryDark
import com.practice.medtechapp.ui.theme.TextColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToSchedule: () -> Unit
) {
    val userDetails = remember {
        UserProfile(
            name = "Krishna M",
            id = "469213",
            email = "krishna@example.com",
            phone = "+1 (555) 123-4567",
            emergencyContact = "Rahul M: +1 (555) 765-4321",
            doctorInfo = "Dr. Sarah Johnson: +1 (555) 987-6543"
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile", color = BackgroundLight) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryDark
                )
            )
        },
        containerColor = BackgroundLight,
        bottomBar = {
            NavigationBar(
                containerColor = PrimaryDark,
                contentColor = BackgroundLight
            ) {
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToHome,
                    icon = {
                        Icon(
                            Icons.Default.Home,
                            contentDescription = "Home",
                            tint = Color(0xFFA68763)
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PrimaryLight,
                        unselectedIconColor = Color(0xFFA68763),
                        indicatorColor = SecondaryDark
                    ),
                    label = { Text("Home", color = Color(0xFFA68763)) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToSchedule,
                    icon = {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = "Schedule",
                            tint = Color(0xFFA68763)
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PrimaryLight,
                        unselectedIconColor = Color(0xFFA68763),
                        indicatorColor = SecondaryDark
                    ),
                    label = { Text("Schedule", color = Color(0xFFA68763)) }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = PrimaryLight
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PrimaryLight,
                        unselectedIconColor = Color(0xFFA68763),
                        indicatorColor = SecondaryDark
                    ),
                    label = { Text("Profile", color = PrimaryLight) }
                )
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
                ProfileHeader(userDetails.name, userDetails.id)
                Spacer(modifier = Modifier.height(24.dp))
                PersonalInformationSection(userDetails)
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = Color(0xFFE6D9C2))
                Spacer(modifier = Modifier.height(16.dp))
                MedicalInformationSection()
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = Color(0xFFE6D9C2))
                Spacer(modifier = Modifier.height(16.dp))
                AppSettingsSection()
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = Color(0xFFE6D9C2))
                Spacer(modifier = Modifier.height(16.dp))
                LogoutButton(
                    navController = NavController(context = LocalContext.current)
                )
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

data class UserProfile(
    val name: String,
    val id: String,
    val email: String,
    val phone: String,
    val emergencyContact: String,
    val doctorInfo: String
)

@Composable
fun ProfileHeader(name: String, id: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(PrimaryLight.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Person,
                contentDescription = "Profile Picture",
                modifier = Modifier.size(60.dp),
                tint = PrimaryDark
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            name,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TextColor
        )

        Text(id,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = TextColor
        )

        OutlinedButton(
            onClick = { /* Edit profile action */ },
            modifier = Modifier.padding(top = 8.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = PrimaryDark
            ),
            border = BorderStroke(1.dp, PrimaryDark)
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
            color = PrimaryDark,
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
            icon = Icons.Default.Emergency,
            title = "Emergency Contact",
            value = userProfile.emergencyContact
        )

        ProfileInfoItem(
            icon = Icons.Default.PersonPin,
            title = "Doctor",
            value = userProfile.doctorInfo
        )
    }
}

@Composable
fun ProfileInfoItem(icon: ImageVector, title: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            modifier = Modifier.size(40.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE6D9C2)
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = title,
                    tint = PrimaryDark,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f)
        ) {
            Text(
                title,
                fontSize = 14.sp,
                color = Color.Gray
            )
            Text(
                value,
                fontSize = 16.sp,
                color = TextColor
            )
        }
    }
}

@Composable
fun MedicalInformationSection() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            "Medical Information",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryDark,
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
            color = PrimaryDark,
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
fun SettingsItem(icon: ImageVector, title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            modifier = Modifier.size(40.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE6D9C2)
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = title,
                    tint = PrimaryDark,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Text(
            title,
            fontSize = 16.sp,
            color = TextColor,
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
        )

        Icon(
            Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Navigate",
            tint = Color.Gray,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun LogoutButton(navController: NavController) {
    Button(
        onClick = { navController.navigate("signIn") },
        colors = ButtonDefaults.buttonColors(
            containerColor = SecondaryDark,
            contentColor = Color.White
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            Icons.AutoMirrored.Filled.ExitToApp,
            contentDescription = "Logout"
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text("Logout")
    }
}