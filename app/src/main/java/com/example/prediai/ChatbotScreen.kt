package com.example.prediai

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class Message(
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    val isFromUser: Boolean,
    val timestamp: String = getCurrentTime(),
    val messageType: MessageType = MessageType.TEXT
)

enum class MessageType {
    TEXT, QUICK_REPLY, TYPING
}

data class QuickReply(
    val text: String,
    val icon: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatbotScreen(navController: NavController) {
    // Initialize Gemini AI using the correct model name for v1 API
    val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = ApiKeys.GEMINI_API_KEY
    )

    // Initialize chat with system instruction
    val chat = remember {
        generativeModel.startChat(
            history = listOf(
                content(role = "user") {
                    text("Kamu adalah AI Assistant PrediAI yang ahli dalam bidang diabetes, nutrisi, dan kesehatan. Berikan jawaban yang akurat, informatif, dan mudah dipahami dalam bahasa Indonesia. Fokus pada informasi medis yang dapat dipercaya dan selalu sarankan untuk konsultasi dengan dokter untuk diagnosis yang akurat. Jawab dengan singkat namun informatif, maksimal 3-4 kalimat per respons.")
                },
                content(role = "model") {
                    text("Halo! Saya AI Assistant PrediAI. Saya siap membantu Anda dengan pertanyaan seputar diabetes, nutrisi, dan kesehatan. Ada yang ingin Anda tanyakan?")
                }
            )
        )
    }

    var messages by remember {
        mutableStateOf(
            listOf(
                Message(
                    text = "Halo! Saya AI Assistant PrediAI. Saya siap membantu Anda dengan pertanyaan seputar diabetes, nutrisi, dan kesehatan. Ada yang ingin Anda tanyakan?",
                    isFromUser = false,
                    timestamp = getCurrentTime()
                )
            )
        )
    }
    var messageText by remember { mutableStateOf("") }
    var isTyping by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    // Prevent multiple rapid sends
    var lastSendTime by remember { mutableStateOf(0L) }

    val quickReplies = listOf(
        QuickReply("Apa saja gejala awal diabetes?", Icons.Filled.Help),
        QuickReply("Bagaimana cara mengontrol gula darah?", Icons.Filled.TrendingUp)
    )

    // Function to send message with Gemini AI
    fun sendMessage(text: String) {
        val currentTime = System.currentTimeMillis()
        if (text.isNotBlank() && !isTyping && (currentTime - lastSendTime) > 1000) { // 1 second cooldown
            lastSendTime = currentTime
            val userMessage = Message(text = text, isFromUser = true)
            messages = messages + userMessage
            messageText = ""
            isTyping = true
            errorMessage = null

            // Scroll to bottom safely
            coroutineScope.launch {
                try {
                    kotlinx.coroutines.delay(50) // Small delay to ensure UI update
                    listState.animateScrollToItem(messages.size)
                } catch (e: Exception) {
                    // Ignore scroll errors - they're not critical
                }
            }

            // Send to Gemini AI with proper error handling
            coroutineScope.launch {
                try {
                    val response = chat.sendMessage(text)

                    // Ensure we're still in the right state
                    if (isTyping) {
                        val responseText = response.text
                        if (!responseText.isNullOrBlank()) {
                            val botResponse = Message(
                                text = responseText,
                                isFromUser = false
                            )
                            // Update state atomically
                            messages = messages + botResponse
                            isTyping = false

                            // Safe scroll with delay
                            kotlinx.coroutines.delay(100)
                            try {
                                listState.animateScrollToItem(messages.size)
                            } catch (e: Exception) {
                                // Fallback to immediate scroll if animation fails
                                listState.scrollToItem(messages.size)
                            }
                        } else {
                            val fallbackResponse = Message(
                                text = "Maaf, saya tidak dapat memberikan respons saat ini. Silakan coba pertanyaan lain.",
                                isFromUser = false
                            )
                            messages = messages + fallbackResponse
                            isTyping = false
                        }
                    }
                } catch (e: Exception) {
                    // Only show error if we're still in typing state
                    if (isTyping) {
                        isTyping = false

                        // Filter out UI state errors
                        val shouldShowError = when {
                            e.message?.contains("Current mutation had a higher priority") == true -> false
                            e.message?.contains("CancellationException") == true -> false
                            e.message?.contains("API") == true -> true
                            e.message?.contains("quota") == true -> true
                            e.message?.contains("model") == true -> true
                            e.message?.contains("network") == true -> true
                            else -> false // Don't show unknown errors
                        }

                        if (shouldShowError) {
                            val errorDetail = when {
                                e.message?.contains("API") == true -> "Terjadi masalah dengan koneksi API"
                                e.message?.contains("model") == true -> "Model AI tidak tersedia"
                                e.message?.contains("quota") == true -> "Kuota API habis"
                                e.message?.contains("network") == true -> "Masalah jaringan"
                                else -> "Gangguan koneksi"
                            }
                            errorMessage = errorDetail
                        }

                        // Always provide fallback response for user
                        val fallbackResponse = Message(
                            text = "Maaf, terjadi gangguan sementara. Silakan coba lagi.",
                            isFromUser = false
                        )
                        messages = messages + fallbackResponse
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            ChatTopBar(navController = navController)
        },
        bottomBar = {
            Column {
                // Show error message if any
                errorMessage?.let { error ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Error,
                                contentDescription = "Error",
                                tint = MaterialTheme.colorScheme.onErrorContainer,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = error,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            IconButton(
                                onClick = { errorMessage = null },
                                modifier = Modifier.size(20.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "Close",
                                    tint = MaterialTheme.colorScheme.onErrorContainer,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }

                if (messages.size == 1) { // Show quick replies only initially
                    QuickRepliesSection(quickReplies = quickReplies) { reply ->
                        sendMessage(reply.text)
                    }
                }
                ChatInputSection(
                    messageText = messageText,
                    onMessageChange = { messageText = it },
                    onSendMessage = { sendMessage(messageText) },
                    keyboardController = keyboardController,
                    isLoading = isTyping
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FA))
                .padding(paddingValues)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                // Online status indicator
                item {
                    OnlineStatusIndicator()
                }

                // Messages
                items(messages) { message ->
                    MessageBubble(message = message)
                }

                // Typing indicator
                if (isTyping) {
                    item {
                        TypingIndicator()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopBar(navController: NavController) {
    CenterAlignedTopAppBar(
        title = {
            Column {
                Text(
                    text = "AI Assistant",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = "Online • Siap membantu",
                    color = Color(0xFF00B4A3),
                    fontSize = 12.sp
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        actions = {
            IconButton(onClick = { /* Handle more options */ }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "More",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}

@Composable
fun OnlineStatusIndicator() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .background(Color(0xFF00B4A3), RoundedCornerShape(16.dp))
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.LocalHospital,
                    contentDescription = "Health",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Konsultasi Diabetes",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        Box(
            modifier = Modifier
                .background(Color(0xFF4CAF50), RoundedCornerShape(16.dp))
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Restaurant,
                    contentDescription = "Nutrition",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Tips Nutrisi",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        Box(
            modifier = Modifier
                .background(Color(0xFF2196F3), RoundedCornerShape(16.dp))
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.FitnessCenter,
                    contentDescription = "Exercise",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun MessageBubble(message: Message) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isFromUser) Arrangement.End else Arrangement.Start
    ) {
        if (!message.isFromUser) {
            // AI Avatar
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(Color(0xFF00B4A3), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.SmartToy,
                    contentDescription = "AI",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            modifier = Modifier.widthIn(max = 280.dp),
            horizontalAlignment = if (message.isFromUser) Alignment.End else Alignment.Start
        ) {
            Card(
                shape = RoundedCornerShape(
                    topStart = if (message.isFromUser) 16.dp else 4.dp,
                    topEnd = if (message.isFromUser) 4.dp else 16.dp,
                    bottomStart = 16.dp,
                    bottomEnd = 16.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = if (message.isFromUser) Color(0xFF00B4A3)
                    else MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Text(
                    text = message.text,
                    modifier = Modifier.padding(12.dp),
                    color = if (message.isFromUser) Color.White
                    else MaterialTheme.colorScheme.onSurface,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }
            Text(
                text = message.timestamp,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(top = 4.dp, start = 4.dp, end = 4.dp)
            )
        }

        if (message.isFromUser) {
            Spacer(modifier = Modifier.width(8.dp))
            // User Avatar
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "U",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun TypingIndicator() {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(Color(0xFF00B4A3), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.SmartToy,
                contentDescription = "AI",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(3) { index ->
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(Color(0xFF00B4A3), CircleShape)
                    )
                    if (index < 2) Spacer(modifier = Modifier.width(4.dp))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "AI sedang mengetik...",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun QuickRepliesSection(quickReplies: List<QuickReply>, onQuickReplyClick: (QuickReply) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // FAQ Header
        Text(
            text = "Pertanyaan yang sering ditanyakan",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            textAlign = TextAlign.Center
        )

        // Quick Replies
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(quickReplies) { quickReply ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onQuickReplyClick(quickReply) },
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = quickReply.icon,
                            contentDescription = quickReply.text,
                            tint = Color(0xFF00B4A3),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = quickReply.text,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 14.sp,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = Icons.Filled.ChevronRight,
                            contentDescription = "Send",
                            tint = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatInputSection(
    messageText: String,
    onMessageChange: (String) -> Unit,
    onSendMessage: () -> Unit,
    keyboardController: androidx.compose.ui.platform.SoftwareKeyboardController?,
    isLoading: Boolean = false
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            OutlinedTextField(
                value = messageText,
                onValueChange = onMessageChange,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp),
                placeholder = {
                    Text(
                        text = if (isLoading) "AI sedang memproses..." else "Ketik pesan Anda...",
                        color = MaterialTheme.colorScheme.outline,
                        fontSize = 14.sp
                    )
                },
                enabled = !isLoading,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Send,
                    keyboardType = KeyboardType.Text
                ),
                keyboardActions = KeyboardActions(
                    onSend = {
                        if (!isLoading) {
                            onSendMessage()
                            keyboardController?.hide()
                        }
                    }
                ),
                shape = RoundedCornerShape(20.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF00B4A3),
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    containerColor = Color.Transparent
                ),
                maxLines = 4
            )
            IconButton(
                onClick = {
                    if (!isLoading) {
                        onSendMessage()
                    }
                },
                enabled = messageText.isNotBlank() && !isLoading
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            if (messageText.isNotBlank() && !isLoading) Color(0xFF00B4A3)
                            else MaterialTheme.colorScheme.surfaceVariant,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Filled.Send,
                            contentDescription = "Send",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

// Helper functions
fun getCurrentTime(): String {
    val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    return formatter.format(Date())
}