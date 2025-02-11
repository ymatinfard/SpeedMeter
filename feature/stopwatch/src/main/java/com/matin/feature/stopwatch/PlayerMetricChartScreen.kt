import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.matin.core.designsystem.theme.component.PlayerInfo
import com.matin.core.designsystem.theme.component.SpeedMeterTopBar
import com.matin.feature.stopwatch.StopWatchSharedViewModel
import com.matin.feature.stopwatch.model.UiLeaderBoardPlayer
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.Line

@Composable
fun PlayerMetricChartScreen(id: Int, viewModel: StopWatchSharedViewModel, onBack: () -> Unit) {
    val state = viewModel.leaderBoardUiState.collectAsStateWithLifecycle()
    val player = state.value.players.first { it.id == id }
    PlayerMetricChartContent(player, onBack)
}

@Composable
fun PlayerMetricChartContent(player: UiLeaderBoardPlayer, onBack: () -> Unit) {
    val chartData = player.laps.map { it.lapTime.toDouble() }

    Scaffold(
        topBar = { SpeedMeterTopBar(title = "Player Metric Chart", onBack = onBack) }
    ) { padding ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            PlayerInfo(player.fullName, player.distance, player.imageUrl)
            Spacer(modifier = Modifier.height(22.dp))
            Chart(chartData)
        }
    }
}

@Composable
fun Chart(data: List<Double>) {
    val gradientFillColor = MaterialTheme.colorScheme.tertiary

    LineChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(horizontal = 22.dp, vertical = 22.dp),
        data = remember {
            listOf(
                Line(
                    label = "Lap time",
                    values = data,
                    color = SolidColor(Color(0xFF23af92)),
                    firstGradientFillColor = gradientFillColor,
                    secondGradientFillColor = Color.Transparent,
                    strokeAnimationSpec = tween(2000, easing = EaseInOutCubic),
                    gradientAnimationDelay = 1000,
                    drawStyle = DrawStyle.Stroke(width = 2.dp),
                )
            )
        },
        animationMode = AnimationMode.Together(delayBuilder = {
            it * 500L
        }),
    )
}
