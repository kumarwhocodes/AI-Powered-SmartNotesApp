package dev.kumar.assignment.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PrimaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shapes: RoundedCornerShape = RoundedCornerShape(percent = 25),
    color: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = Color.White,
        disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
        disabledContentColor = Color.White.copy(alpha = 0.8f)
    ),
    contentPadding: PaddingValues = PaddingValues(25.dp, 0.dp),
    content: @Composable (RowScope.() -> Unit)
) {

    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shapes,
        colors = color,
        contentPadding = contentPadding,
        content = content
    )
}


@Composable
fun SecondaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shapes: RoundedCornerShape = RoundedCornerShape(percent = 25),
    color: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.onPrimary,
        contentColor = MaterialTheme.colorScheme.onBackground,
        disabledContainerColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
        disabledContentColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
    ),
    contentPadding: PaddingValues = PaddingValues(25.dp, 0.dp),
    content: @Composable (RowScope.() -> Unit)
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shapes,
        colors = color,
        contentPadding = contentPadding,
        content = content
    )
}

@Composable
fun FAB(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(percent = 100),
    elevation: FloatingActionButtonElevation = FloatingActionButtonDefaults.elevation(),
    contentDescription: String = "",
    icon: Painter,
    containerColor: Color = MaterialTheme.colorScheme.primary,
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier.size(20.dp),
        shape = shape,
        containerColor = containerColor,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        elevation = elevation
    ) {
        Image(painter = icon, contentDescription = contentDescription)

    }
}

@Composable
fun LoginButton(
    value: String, onClick: () -> Unit,
    isEnabled: Boolean
) {
    PrimaryButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(),
        enabled = isEnabled
    ) {
        Text(
            text = value,
            fontSize = 15.sp
        )
    }
}

@Composable
fun SmallCircularImageButton(
    onClick: () -> Unit,
    image: Painter,
    desc: String
) {
    Box(
        modifier = Modifier
            .size(80.dp)
            .padding(10.dp)
            .border(
                width = 2.dp,
                color = Color.LightGray,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier
                .size(50.dp)
                .clip(shape = CircleShape)
                .clickable(onClick = onClick),
            painter = image,
            contentDescription = desc
        )

    }

}
