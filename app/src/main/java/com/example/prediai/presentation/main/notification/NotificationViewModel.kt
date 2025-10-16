package com.example.prediai.presentation.main.notification

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

// Model data for a single notification item
data class NotificationItemData(
    val title: String,
    val dateTime: String
)

// State for the entire UI
data class NotificationUiState(
    val notifications: List<NotificationItemData> = listOf(
        NotificationItemData("Waktunya minum obat", "17 Agustus • 4:00 PM"),
        NotificationItemData("Ayo pergi ke dokter untuk kontrol gula darah", "17 Agustus • 4:00 PM"),
        NotificationItemData("Waktunya minum obat", "17 Agustus • 4:00 PM"),
        NotificationItemData("Ayo pergi ke dokter untuk kontrol gula darah", "17 Agustus • 4:00 PM"),
        NotificationItemData("Waktunya minum obat", "17 Agustus • 4:00 PM"),
        NotificationItemData("Ayo pergi ke dokter untuk kontrol gula darah", "17 Agustus • 4:00 PM")
    )
)

@HiltViewModel
class NotificationViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(NotificationUiState())
    val uiState = _uiState.asStateFlow()
}