package com.example.crisisready.ui.adminHome

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.crisisready.ui.doDonts.Disaster
import com.example.crisisready.ui.doDonts.naturalDisasterList
import com.example.crisisready.ui.homeScreen.HomeScreenTopBar
import com.example.crisisready.ui.viewmodel.UserViewModel


@Composable
fun AdminHomeScreen(modifier: Modifier = Modifier, userViewModel: UserViewModel = hiltViewModel()) {
    Scaffold(
        topBar = { HomeScreenTopBar() }
    ) {
        AdminHomeScreenContent(modifier = Modifier.padding(it))
    }
}

@Composable
fun AdminHomeScreenContent(modifier: Modifier = Modifier) {
    val viewModel: adminHomeScreenViewModel = hiltViewModel()
    val disasterList = viewModel.disasterList.collectAsState()
    val selectedDisaster = viewModel.selectedDisaster.collectAsState()
    val selectedLocation = viewModel.selectedLocation.collectAsState()
    val availableLocation = listOf(
        "Bangalore",
        "JP Nagar",
        "Jayanagar",
        "WhiteField",
        "Rajindranagar",
    )
    val availableDisaster = listOf(
        "Earthquake",
        "Flood",
        "Cyclone",
        "Landslide",
        "Tsunami",
        "Heat Wave",
        "Fire",
        "Drought",
        "Thunderstorm",
        "Forest Fire"
    )
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()

    ) {
        MyDropDownMenu(
            availableOptions = availableLocation,
            onSelectedChange = { viewModel.updateSelectedLocation(it) },
            selectWhat = "Location"
        )
        Spacer(modifier = Modifier.height(16.dp))
        MyDropDownMenu(
            availableOptions = availableDisaster,
            onSelectedChange = { viewModel.updateSelectedDisaster(it) },
            selectWhat = "Disaster"
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (selectedDisaster.value.isNotEmpty() && selectedLocation.value.isNotEmpty()) {
                    viewModel.sendNotification()
                    viewModel.addDisaster(
                        disaster(
                            disasterName = selectedDisaster.value,
                            disasterLocation = selectedLocation.value
                        )
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = "Alert")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Alerts List",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(16.dp))
        disasterList.value.forEach {
            AlertListItem(
                location = it.disasterLocation,
                disaster = it.disasterName,
                onRemove = { viewModel.removeDisaster(it) },
                item = it
            )
        }

    }
}

@Composable
fun AlertListItem(
    modifier: Modifier = Modifier,
    location: String,
    disaster: String,
    onRemove: (disaster) -> Unit = {},
    onMap: () -> Unit = {},
    item: disaster
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = location, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(text = disaster, style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(5.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { onRemove(item) }, modifier = Modifier.weight(1f)) {
                    Text(
                        text = "REVERT",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Red
                    )
                }
                Spacer(modifier = Modifier.weight(0.5f))
                Button(onClick = { onMap() }, modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Map",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDropDownMenu(
    modifier: Modifier = Modifier,
    availableOptions: List<String>,
    onSelectedChange: (String) -> Unit = {},
    selectWhat: String,
) {
    val interactionSource = remember { MutableInteractionSource() }
    var selectedAvailability by rememberSaveable {
        mutableStateOf(availableOptions[0])
    }
    onSelectedChange(selectedAvailability)
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }
    Text(
        text = "Select $selectWhat",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold
    )
    Spacer(modifier = Modifier.height(8.dp))
    Column(modifier = modifier.fillMaxWidth()) {


        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                BasicTextField(value = selectedAvailability,
                    onValueChange = { },
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .menuAnchor(),
                    visualTransformation = VisualTransformation.None,
                    interactionSource = interactionSource,
                    readOnly = true,
                    enabled = true,
                    singleLine = true,
                    decorationBox = @Composable { innerTextField ->
                        OutlinedTextFieldDefaults.DecorationBox(value = selectedAvailability,
                            innerTextField = innerTextField,
                            singleLine = true,
                            interactionSource = interactionSource,
                            visualTransformation = VisualTransformation.None,
                            enabled = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            contentPadding = PaddingValues(start = 16.dp),
                            container = {
                                OutlinedTextFieldDefaults.ContainerBox(
                                    enabled = true,
                                    isError = false,
                                    interactionSource = interactionSource,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(15, 46, 66, 255),
                                        unfocusedBorderColor = Color(15, 46, 66, 255),
                                        focusedTextColor = Color(15, 46, 66, 255),
                                        unfocusedTextColor = Color(15, 46, 66, 255),
                                    ),
                                    shape = MaterialTheme.shapes.medium,
                                )
                            })
                    })
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    availableOptions.forEach { selectionOption ->
                        DropdownMenuItem(text = { Text(text = selectionOption) }, onClick = {
                            selectedAvailability = selectionOption
                            onSelectedChange(selectionOption)
                            expanded = false
                        })
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun AdminHomePreview(modifier: Modifier = Modifier) {
    AdminHomeScreen()
}