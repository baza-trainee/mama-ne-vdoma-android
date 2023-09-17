package com.example.mama_ne_vdoma.utils

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp


@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    text: CharSequence,
    fontSize: TextUnit = 16.sp,
    shape: Shape = ButtonDefaults.shape,
    containerColor: Color,
    contentColor: Color,
    action: () -> Unit
) {
    Button(
        onClick = action,
        modifier = modifier,
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        shape = shape
    ) {
        if (text is AnnotatedString)
            Text(text = text, fontSize = fontSize)
        else
            Text(text = text.toString(), fontSize = fontSize)
    }
}