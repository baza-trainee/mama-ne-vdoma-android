package com.example.mama_ne_vdoma.utils

import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle

fun getTextWithUnderline(
    simpleText: String,
    underlinedText: String,
    isBold: Boolean = true
) = buildAnnotatedString {
    append(simpleText)
    val fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
    withStyle(
        style = SpanStyle(
            textDecoration = TextDecoration.Underline,
            fontWeight = fontWeight
        )
    ) {
        append(underlinedText)
    }
}