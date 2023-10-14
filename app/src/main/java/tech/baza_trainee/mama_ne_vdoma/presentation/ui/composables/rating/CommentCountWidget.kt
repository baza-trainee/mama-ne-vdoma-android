package tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.rating

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.MainColor

@Composable
fun CommentCountWidget(modifier: Modifier = Modifier, count: Int) {
    Row(
        modifier = modifier.padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.ChatBubbleOutline,
            contentDescription = "Comment Icon",
            tint = MainColor
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = count.toString(), fontSize = 18.sp, color = Color.Black)
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CommentCountWidget(count = 10)
}
