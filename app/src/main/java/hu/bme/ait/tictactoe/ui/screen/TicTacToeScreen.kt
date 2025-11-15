package hu.bme.ait.tictactoe.ui.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Cyan
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.bme.ait.tictactoe.R
import hu.bme.ait.tictactoe.ui.theme.Purple80
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable

@Composable
fun TicTacToeScreen(
    modifier: Modifier = Modifier,
    ticTactToeViewModel: TicTactToeViewModel = viewModel()
) {
    val context = LocalContext.current
    val zoomState = rememberZoomState()

    val gradientColors = listOf(Purple80, Blue, Cyan)

    Column(
        modifier = modifier.padding(top = 150.dp)
            .fillMaxSize()
            .zoomable(zoomState),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {

        Text(
            context.getString(
                R.string.x_winning_times,
                ticTactToeViewModel.winners[0]
            )
        )

        Text(
            context.getString(
                R.string.o_winning_times,
                    ticTactToeViewModel.winners[1]
            )
        )


        Text(
            context.getString(
                R.string.next_player_text,
                ticTactToeViewModel.currentPlayer
            ),
            fontSize = 28.sp


        )

        TicTacToeBoard(ticTactToeViewModel.board) {
            ticTactToeViewModel.onCellClicked(it)
        }



        Button(onClick = {
            ticTactToeViewModel.setNewBoard(3)
        }) {
            Text(context.getString(R.string.btn_reset))
        }


        if (ticTactToeViewModel.totalCells == 9 && ticTactToeViewModel.winner == null) {
            Text(context.getString(R.string.text_if_draw)
            )
        }

        if (ticTactToeViewModel.winner != null){
            Text(
            context.getString(
                R.string.winner_text, ticTactToeViewModel.winner),
                style = TextStyle(
                    fontSize = 24.sp,
                    brush = Brush.linearGradient(
                        colors = gradientColors
                    )
                )
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TicTacToeBoard(
    board: Array<Array<Player?>>,
    onCellClicked: (BoardCell) -> Unit
) {


    var canvasSize by remember { mutableStateOf(Size.Zero) }

    Canvas(
        modifier = Modifier
            .fillMaxSize(0.5f)
            .aspectRatio(1.0f)
            .pointerInput(key1 = Unit) {
                detectTapGestures {
                    val cellX = (it.x / (canvasSize.width / 3)).toInt()
                    val cellY = (it.y / (canvasSize.height / 3)).toInt()

                    onCellClicked(
                        BoardCell(cellY, cellX)
                    )
                }
            }
    ) {
        canvasSize = this.size
        val canvasWidth = size.width.toInt()
        val canvasHeight = size.height.toInt()


        // Draw the grid
        val gridSize = size.minDimension
        val thirdSize = gridSize / 3



        for (i in 1..2) {
            drawLine(
                color = Color.Black,
                strokeWidth = 8f,
                pathEffect = PathEffect.cornerPathEffect(4f),
                start = androidx.compose.ui.geometry.Offset(thirdSize * i, 0f),
                end = androidx.compose.ui.geometry.Offset(thirdSize * i, gridSize)
            )
            drawLine(
                color = Color.Black,
                strokeWidth = 8f,

                start = androidx.compose.ui.geometry.Offset(0f, thirdSize * i),
                end = androidx.compose.ui.geometry.Offset(gridSize, thirdSize * i),
            )
        }

        // Draw players.. X and O
        for (row in 0..2) {
            for (col in 0..2) {
                val player = board[row][col]
                if (player != null) {
                    val centerX = col * thirdSize + thirdSize / 2
                    val centerY = row * thirdSize + thirdSize / 2
                    if (player == Player.X) {
                        drawLine(
                            color = Color.Red,
                            strokeWidth = 8f,
                            pathEffect = PathEffect.cornerPathEffect(4f),
                            start = androidx.compose.ui.geometry.Offset(
                                centerX - thirdSize / 4,
                                centerY - thirdSize / 4
                            ),
                            end = androidx.compose.ui.geometry.Offset(
                                centerX + thirdSize / 4,
                                centerY + thirdSize / 4
                            ),
                        )
                        drawLine(
                            color = Color.Red,
                            strokeWidth = 8f,
                            pathEffect = PathEffect.cornerPathEffect(4f),
                            start = androidx.compose.ui.geometry.Offset(
                                centerX + thirdSize / 4,
                                centerY - thirdSize / 4
                            ),
                            end = androidx.compose.ui.geometry.Offset(
                                centerX - thirdSize / 4,
                                centerY + thirdSize / 4
                            ),
                        )
                    } else {
                        drawCircle(
                            color = Color.Green,
                            style = Stroke(width = 8f),
                            center = androidx.compose.ui.geometry.Offset(centerX, centerY),
                            radius = thirdSize / 4,
                        )
                    }
                }
            }
        }
    }

}