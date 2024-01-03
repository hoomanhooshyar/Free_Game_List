package com.example.freegamelist.presentation.all_games.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun GameTag(
    tag:String,
    isSelected:Boolean,
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .background(if (isSelected) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.background, shape = RoundedCornerShape(100.dp))
            .border(
                width = 1.dp,
                shape = RoundedCornerShape(100.dp),
                color = MaterialTheme.colorScheme.primary
            )

            .padding(10.dp)
            .clickable {
                onClick()
            }
    ){
        Text(
            text = tag,
            color = if(isSelected) Color.White else Color.Black,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}