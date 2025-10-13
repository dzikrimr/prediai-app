package com.example.prediai.presentation.quistionere.comps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun QuestionItem(
    question: String,
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = question,
            fontSize = 14.sp,
            color = Color(0xFF2D3142),
            textAlign = TextAlign.Center,
            lineHeight = 20.sp,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Grid layout untuk pilihan
        when (options.size) {
            2 -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    options.forEach { option ->
                        OptionButton(
                            text = option,
                            isSelected = selectedOption == option,
                            onClick = { onOptionSelected(option) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            3 -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    options.forEach { option ->
                        OptionButton(
                            text = option,
                            isSelected = selectedOption == option,
                            onClick = { onOptionSelected(option) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            4 -> {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OptionButton(
                            text = options[0],
                            isSelected = selectedOption == options[0],
                            onClick = { onOptionSelected(options[0]) },
                            modifier = Modifier.weight(1f)
                        )
                        OptionButton(
                            text = options[1],
                            isSelected = selectedOption == options[1],
                            onClick = { onOptionSelected(options[1]) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OptionButton(
                            text = options[2],
                            isSelected = selectedOption == options[2],
                            onClick = { onOptionSelected(options[2]) },
                            modifier = Modifier.weight(1f)
                        )
                        OptionButton(
                            text = options[3],
                            isSelected = selectedOption == options[3],
                            onClick = { onOptionSelected(options[3]) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}