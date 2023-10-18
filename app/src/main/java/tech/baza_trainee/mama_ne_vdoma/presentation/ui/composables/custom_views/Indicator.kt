package tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Indicator(
    isSelected: Boolean,
    selectedColor: Color,
    backgroundColor: Color,
    defaultRadius: Dp,
    selectedLength: Dp,
    modifier: Modifier = Modifier
) {
    val width by animateDpAsState(
        targetValue = if (isSelected) selectedLength else defaultRadius,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = ""
    )
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .height(defaultRadius)
            .width(width)
            .background(
                color = if (isSelected) selectedColor else backgroundColor,
                shape = CircleShape
            )
            .border(
                width = if (isSelected) 0.dp else 1.dp,
                color = selectedColor,
                shape = CircleShape
            )
    )
}