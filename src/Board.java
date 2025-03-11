import java.util.Arrays;

// IMPORTANT: Il ne faut pas changer la signature des méthodes
// de cette classe, ni le nom de la classe.
// Vous pouvez par contre ajouter d'autres méthodes (ça devrait 
// être le cas)
class Board
{
    private Mark[][] board;
    private Mark winner = null;

    // Ne pas changer la signature de cette méthode
    public Board() {
        this.board = new Mark[3][3];
        for (Mark[] row: this.board) {
            Arrays.fill(row, Mark.EMPTY);
        }
    }

    public Board(Mark[][] initialBoard) {
        board = new Mark[3][3];
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                board[r][c] = initialBoard[r][c];
            }
        }
    }

    public boolean hasMark(Mark mark) {
        for (Mark[] row : board) {
            for (Mark tile : row) {
                if (tile == mark) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public void setWinner(Mark winner) {
        this.winner = winner;
    }

    public Mark[][] getBoard() {
        return board;
    }

    // Place la pièce 'mark' sur le plateau, à la
    // position spécifiée dans Move
    //
    // Ne pas changer la signature de cette méthode
    public void play(Move m, Mark mark){
        this.board[m.getRow()][m.getCol()] = mark;
    }

    @Override
    public String toString() {
        String boardVisual = "";
        for (Mark[] row: this.board) {
            boardVisual += "\n | ";
            for (Mark tile: row) {
                if (tile == Mark.EMPTY) {
                    boardVisual += "  | ";
                    continue;
                }
                boardVisual += tile + " | ";
            }
        }

        return boardVisual;
    }

    // retourne  100 pour une victoire
    //          -100 pour une défaite
    //           0   pour un match nul
    // Ne pas changer la signature de cette méthode
    public int evaluate(Mark mark) {
        // Check if the current mark is winning
        if (isWinning(mark)) {
            return 100;
        } else {
            // Else, check if other mark is winning
            switch (mark) {
                case O:
                    if (isWinning(Mark.X)) {
                        setWinner(Mark.X);
                        return -100;
                    }
                    break;
                case X:
                    if (isWinning(Mark.O)) {
                        setWinner(mark);
                        return -100;
                    }
                    break;
                case EMPTY:
                    break;
            }
        }
        // If neither are winning, then it's a draw
        setWinner(null);
        return 0;
    }

    public Mark getWinner() {
        return winner;
    }

    // Check if mark is winning on a horizontal line
    public boolean checkHorizontal(Mark mark) {
        rowIteration: for (Mark[] row: this.board) {
            for (Mark col: row) {
                if (col != mark) {
                    // For each row, if current column is not equal to mark, skip to check next row
                    continue rowIteration;
                }
            }
            return true;
        }
        return false;
    }

    // Check if mark is winning on a vertical line
    public boolean checkVertical(Mark mark) {
        columnIteration: for (int i = 0; i < this.board[0].length; i++) {
            for (int j = 0; j < this.board.length; j++) {
                if (this.board[j][i] != mark) {
                    // For each column, if current row is not equal to mark, skip to check next column
                    continue columnIteration;
                }
            }
            return true;
        }
        return false;
    }

    // Check if mark is winning on either diagonals
    public boolean checkDiagonal(Mark mark) {
        return board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[2][2] == mark ||
               board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[2][0] == mark;
    }

    // Check if mark is winning with any of the winning conditions
    public boolean isWinning(Mark mark) {
        boolean result = checkHorizontal(mark) || checkVertical(mark) || checkDiagonal(mark);
        if (result) {
            setWinner(mark);
        }
        return result;
    }



    // Check if board is filled with marks
    public boolean isFull() {
        for (Mark[] row: board) {
            for (Mark col: row) {
                if (col == Mark.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }
    
    // Create a copy of the board
    public Board copy(){
        Board newBoard = new Board();
        for (int i=0; i<3; i++){
            for (int j=0; j<3; j++){
                newBoard.board[i][j] = board[i][j];
            }
        }
        newBoard.setWinner(this.getWinner());
        return newBoard;
    }
}
