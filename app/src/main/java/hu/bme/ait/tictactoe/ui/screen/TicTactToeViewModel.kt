package hu.bme.ait.tictactoe.ui.screen


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

enum class Player { X, O }

data class BoardCell(val row: Int, val col: Int)

class TicTactToeViewModel : ViewModel() {

    var board by mutableStateOf(
        Array(3) { Array(3) { null as Player? } })
        private set

    var currentPlayer by mutableStateOf(Player.X)
        private set

    var winner by mutableStateOf<Player?>(null)
        private set

    var totalCells = 0

    var xListOfCells by mutableStateOf(
        Array(9){false})
        private set

    var oListOfCells by mutableStateOf(
        Array(9){false})
        private set

    var winners by mutableStateOf(
        Array(2){0})
        private set



    fun setNewBoard(size: Int) {
        board = Array(size) { Array(size) { null as Player? } }
        winner = null
        totalCells = 0
        xListOfCells = Array(9){false}
        oListOfCells = Array(9){false}
    }



    fun onCellClicked(cell: BoardCell) {
        var num = 3*cell.row + 1*cell.col

        if (board[cell.row][cell.col] != null || winner != null) return

        totalCells = totalCells + 1

        board[cell.row][cell.col] = currentPlayer


        if (currentPlayer == Player.X){
            xListOfCells[num] = true
        }

        if (currentPlayer == Player.O){
            oListOfCells[num] = true
        }

        winCheck(currentPlayer)

        if (winner == Player.X) winners[0] = winners[0] + 1
        if (winner == Player.O) winners[1] = winners[1] + 1

        currentPlayer =
            if (currentPlayer == Player.X) Player.O else Player.X


    }

    fun winCheck(player: Player){
        var cells: Array<Boolean>
        if (currentPlayer == Player.X) cells = xListOfCells else cells = oListOfCells

        if (cells[0] && cells[4] && cells[8]){winner = player}
        if (cells[2] && cells[4] && cells[6]){winner = player}
        if (cells[0] && cells[3] && cells[6]){winner = player}

        if (cells[1] && cells[4] && cells[7]){winner = player}
        if (cells[2] && cells[5] && cells[8]){winner = player}
        if (cells[0] && cells[1] && cells[2]){winner = player}

        if (cells[3] && cells[4] && cells[5]){winner = player}
        if (cells[6] && cells[7] && cells[8]){winner = player}

    }

}