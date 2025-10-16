package com.example.prediai.presentation.main.chatbot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prediai.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// Model data sederhana untuk UI
data class ChatFilter(val label: String, val icon: Int)
data class ChatMessage(val text: String, val isFromUser: Boolean, val timestamp: String)
data class QuickReply(val text: String, val icon: Int)

// State untuk seluruh UI
data class ChatbotUiState(
    val filters: List<ChatFilter> = listOf(
        ChatFilter("Konsultasi Diabetes", R.drawable.ic_health_consultation),
        ChatFilter("Tips Nutrisi", R.drawable.ic_nutrition_tips),
        ChatFilter("Aktivitas Fisik", R.drawable.ic_physical_activity)
    ),
    val messages: List<ChatMessage> = listOf(
        ChatMessage(
            text = "Halo! Saya AI Assistant PrediAI. Saya siap membantu Anda dengan pertanyaan seputar diabetes, nutrisi, dan kesehatan. Ada yang ingin Anda tanyakan?",
            isFromUser = false,
            timestamp = "09:15"
        )
    ),
    val quickReplies: List<QuickReply> = listOf(
        QuickReply("Apa saja gejala awal diabetes?", R.drawable.ic_help_question),
        QuickReply("Makanan apa yang sebaiknya dihindari?", R.drawable.ic_food_avoid),
        QuickReply("Bagaimana cara mengontrol gula darah?", R.drawable.ic_blood_sugar_control)
    ),
    val inputText: String = ""
)

@HiltViewModel
class ChatbotViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(ChatbotUiState())
    val uiState = _uiState.asStateFlow()

    fun onInputTextChanged(newText: String) {
        _uiState.update { it.copy(inputText = newText) }
    }

    fun sendMessage(text: String) {
        // TODO: Implement send message logic
    }
}