package com.matin.core.designsystem.theme.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.matin.core.common.SortOption


@Composable
fun SortTab(
    modifier: Modifier = Modifier,
    tabsList: List<SortOption>,
    initialSelectedOption: SortOption = SortOption.EXPLOSIVENESS,
    onClick: (SortOption) -> Unit
) {
    var selectedOption by remember {
        mutableStateOf(initialSelectedOption)
    }
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.inverseOnSurface, RoundedCornerShape(70.dp))
            .border(
                border = BorderStroke(
                    2.dp,
                    MaterialTheme.colorScheme.background
                ), RoundedCornerShape(70.dp)
            )
            .height(IntrinsicSize.Min),
        contentAlignment = Alignment.Center
    ) {

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            tabsList.forEachIndexed { index, sortOption ->
                TabItem(
                    isSelected = selectedOption.name == sortOption.name,
                    text = sortOption.name,
                    Modifier.weight(0.5f)
                ) {
                    selectedOption = sortOption
                    onClick.invoke(selectedOption)
                }
            }
        }
    }
}

@Composable
fun TabItem(isSelected: Boolean, text: String, modifier: Modifier, onClick: () -> Unit) {
    val tabTextColor: Color by animateColorAsState(
        targetValue = if (isSelected) {
            MaterialTheme.colorScheme.onPrimary
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        },
        animationSpec = tween(easing = LinearEasing), label = ""
    )

    val background: Color by animateColorAsState(
        targetValue = if (isSelected)
            MaterialTheme.colorScheme.secondary
        else
            MaterialTheme.colorScheme.inverseOnSurface,
        animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing), label = ""
    )
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = modifier
            .fillMaxWidth(1f)
            .fillMaxHeight(1f)
            .background(background, RoundedCornerShape(70.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onClick.invoke()
            }
            .padding(vertical = 10.dp)
            .clip(RoundedCornerShape(70.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center,
            color = tabTextColor
        )
    }
}