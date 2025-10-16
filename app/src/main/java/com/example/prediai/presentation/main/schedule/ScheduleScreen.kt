package com.example.prediai.presentation.main.schedule

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    navController: NavController,
    viewModel: ScheduleViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    if (uiState.isAddScheduleSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.hideAddScheduleSheet() },
            sheetState = sheetState
        ) {
            AddScheduleSheet()
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
                    currentMonth = uiState.currentMonth,
                    onDateClick = { date -> viewModel.onDateSelected(date) },
                    onPrevMonthClick = { /* TODO */ },
                    onNextMonthClick = { /* TODO */ }
                )
            }

            item {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Jadwal Hari Ini", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("15 Jan", color = Color.Gray, fontSize = 14.sp)
                }
            }

            items(uiState.schedulesForSelectedDay) { scheduleItem ->
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    ScheduleItemCard(item = scheduleItem)
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