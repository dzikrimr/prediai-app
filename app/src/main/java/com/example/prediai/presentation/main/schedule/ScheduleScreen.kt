package com.example.prediai.presentation.main.schedule


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.prediai.presentation.main.schedule.comps.AddScheduleSheet
import com.example.prediai.presentation.main.schedule.comps.CalendarView
import com.example.prediai.presentation.main.schedule.comps.ScheduleItemCard
import com.example.prediai.presentation.theme.PrediAITheme
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    navController: NavController,
    viewModel: ScheduleViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState()

    if (uiState.isAddScheduleSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.hideAddScheduleSheet() },
            sheetState = sheetState
        ) {
            // --- PERBARUI PEMANGGILAN INI ---
            AddScheduleSheet(
                onDismiss = { viewModel.hideAddScheduleSheet() },

                // Tambahkan blok onSaveClick
                onSaveClick = { type, time, notes ->
                    viewModel.saveSchedule(
                        typeString = type,
                        notes = notes,
                        time = time
                    )
                }
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Jadwal", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More Options")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.showAddScheduleSheet() },
                containerColor = Color(0xFF00B4A3)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Schedule", tint = Color.White)
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                CalendarView(
                    selectedDate = uiState.selectedDate,
                    datesWithEvents = uiState.datesWithScheduledEvents,
                    onDateClick = { date -> viewModel.onDateSelected(date) },
                    onMonthChanged = { yearMonth -> viewModel.onMonthChanged(yearMonth) }
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Jadwal Anda", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                    val selectedDateText = remember(uiState.selectedDate) {
                        val formatter = DateTimeFormatter.ofPattern("dd MMM", Locale.getDefault())
                        uiState.selectedDate.format(formatter)
                    }
                    Text(selectedDateText, color = Color.Gray, fontSize = 14.sp)
                }
            }

            itemsIndexed(uiState.schedulesForSelectedDay) { index, scheduleItem ->
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    // Teruskan index ke ScheduleItemCard
                    ScheduleItemCard(
                        item = scheduleItem,
                        index = index
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ScheduleScreenPreview() {
    PrediAITheme {
        ScheduleScreen(navController = rememberNavController())
    }
}