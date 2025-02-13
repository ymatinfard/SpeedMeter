import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.matin.core.common.TimeFormatter
import com.matin.core.designsystem.theme.component.BackButton
import com.matin.core.designsystem.theme.component.PlayerInfo
import com.matin.core.designsystem.theme.component.SpeedMeterTopBar
import com.matin.feature.stopwatch.StopWatchSharedViewModel
import com.matin.feature.stopwatch.model.UiLeaderBoardPlayer
import com.matin.speedmeter.feature.stopwatch.R
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
    val minLapTime = player.laps.minOf { it.lapTime }
    val chartData = player.laps.map { (it.lapTime.toDouble() - minLapTime.toDouble()) / 1000 }
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            SpeedMeterTopBar(
                title = stringResource(R.string.feature_stopwatch_player_metric_chart),
                navigationButton = { BackButton(action = onBack) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(padding),
        ) {
            PlayerInfo(player.fullName, player.distance, player.imageUrl)
            Spacer(modifier = Modifier.height(22.dp))
            LapTimeSummary(
                bestLapTime = player.laps.minOf { it.lapTime },
                worstLapTime = player.laps.maxOf { it.lapTime },
                averageLapTime = player.laps.map { it.lapTime }.average().toLong()
            )
            Chart(chartData)
        }
    }
}

@Composable
fun Chart(data: List<Double>) {
    val gradientFillColor = MaterialTheme.colorScheme.tertiary
    val chartLabel = stringResource(R.string.feature_stopwatch_time_difference_between_best_lap_s)
    LineChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(horizontal = 22.dp, vertical = 22.dp),
        data = remember {
            listOf(
                Line(
                    label = chartLabel,
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

@Composable
fun LapTimeSummary(
    bestLapTime: Long,
    worstLapTime: Long,
    averageLapTime: Long,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.feature_stopwatch_lap_time_summary),
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        TimeCard(
            title = stringResource(R.string.feature_stopwatch_best_lap),
            time = bestLapTime,
            backgroundColor = Color.Green.copy(alpha = .3f)
        )
        TimeCard(
            title = stringResource(R.string.feature_stopwatch_worst_lap),
            time = worstLapTime,
            backgroundColor = Color.Red.copy(alpha = .3f)
        )
        TimeCard(
            title = stringResource(R.string.feature_stopwatch_average_lap),
            time = averageLapTime,
            backgroundColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

@Composable
fun TimeCard(
    title: String,
    time: Long,
    backgroundColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp),
        shape = RoundedCornerShape(8.dp),
        colors = cardColors(containerColor = backgroundColor)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = TimeFormatter.formatTime(time),
                    style = MaterialTheme.typography.labelMedium,
                )
            }
        }
    }
}

