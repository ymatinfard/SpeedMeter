package com.matin.core.designsystem.theme.component

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade

@Composable
fun CircularImage(imageUrl: String) {
    AsyncImage(
        modifier = Modifier
            .size(60.dp)
            .clip(CircleShape),
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl).crossfade(true).build(),
        contentScale = ContentScale.Crop,
        contentDescription = null
    )
}