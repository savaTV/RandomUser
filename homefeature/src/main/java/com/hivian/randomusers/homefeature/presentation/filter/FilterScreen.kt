package com.hivian.randomusers.homefeature.presentation.filter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hivian.randomusers.core.presentation.services.UserFilter
import com.hivian.randomusers.core.presentation.services.UserFilterPreferences
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get

// Define the specific Dark Blue color mentioned in the requirements
private val DarkBlue = Color(0xFF0D47A1) // A deep, professional blue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(
    onContinue: () -> Unit,
    onNavigateBack: () -> Unit = {}
) {
    val filterPreferences = get<UserFilterPreferences>()
    val scope = rememberCoroutineScope()

    val currentFilter = filterPreferences.currentFilter.collectAsState().value


    var selectedGender by remember {
        mutableStateOf(currentFilter.gender?.replaceFirstChar { it.uppercase() } ?: "Male")
    }
    var selectedNat by remember {
        mutableStateOf(currentFilter.nat?.uppercase() ?: "US")
    }

    var isGenerating by remember { mutableStateOf(false) }
    var genderExpanded by remember { mutableStateOf(false) }
    var natExpanded by remember { mutableStateOf(false) }

    val genders = listOf("Male", "Female")
    val nationalities = listOf(
        "AU", "BR", "CA", "CH", "DE", "DK", "ES", "FI", "FR", "GB",
        "IE", "IN", "IR", "MX", "NL", "NO", "NZ", "RS", "TR", "UA", "US"
    )

    // --- UI Implementation ---
    Scaffold(
        containerColor = Color.White,
        topBar = {
            // Requirement: Centered title "Generate user" with Back Arrow
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Generate user",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkBlue
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = DarkBlue
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Select Gender
            Text(
                text = "Select Gender:",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = DarkBlue,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            ExposedDropdownMenuBox(
                expanded = genderExpanded,
                onExpandedChange = { genderExpanded = it },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedGender,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = DarkBlue,
                        unfocusedBorderColor = DarkBlue,
                        focusedTextColor = DarkBlue,
                        unfocusedTextColor = DarkBlue
                    ),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderExpanded)
                    }
                )
                ExposedDropdownMenu(
                    expanded = genderExpanded,
                    onDismissRequest = { genderExpanded = false },
                    modifier = Modifier.background(Color.White)
                ) {
                    genders.forEach { gender ->
                        DropdownMenuItem(
                            text = { Text(gender, color = DarkBlue) },
                            onClick = {
                                selectedGender = gender
                                genderExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            //  Select Nationality
            Text(
                text = "Select Nationality:",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = DarkBlue,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            ExposedDropdownMenuBox(
                expanded = natExpanded,
                onExpandedChange = { natExpanded = it },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedNat,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = DarkBlue,
                        unfocusedBorderColor = DarkBlue,
                        focusedTextColor = DarkBlue,
                        unfocusedTextColor = DarkBlue
                    ),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = natExpanded)
                    }
                )
                ExposedDropdownMenu(
                    expanded = natExpanded,
                    onDismissRequest = { natExpanded = false },
                    modifier = Modifier.background(Color.White)
                ) {
                    nationalities.forEach { nat ->
                        DropdownMenuItem(
                            text = { Text(nat, color = DarkBlue) },
                            onClick = {
                                selectedNat = nat
                                natExpanded = false
                            }
                        )
                    }
                }
            }

            // Spacer to push the button to the bottom part of the screen
            Spacer(modifier = Modifier.weight(1f))

            //  Button: GENERATE
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    enabled = !isGenerating,
                    onClick = {
                        isGenerating = true
                        scope.launch {
                            val filter = UserFilter(
                                gender = selectedGender.lowercase(),
                                nat = selectedNat
                            )
                            filterPreferences.updateFilter(filter)
                            delay(150)
                            onContinue()
                            isGenerating = false
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.8f) // Requirement: 80% width
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp), // Requirement: Rounded corners ~16dp
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DarkBlue, // Requirement: Dark blue background
                        contentColor = Color.White
                    )
                ) {
                    if (isGenerating) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                    }
                    Text(
                        text = "GENERATE",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold // Requirement: Bold text
                    )
                }

                // Requirement: Sufficient vertical spacing at the bottom
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}