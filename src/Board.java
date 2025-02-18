import java.util.Arrays;

// IMPORTANT: Il ne faut pas changer la signature des méthodes
// de cette classe, ni le nom de la classe.
// Vous pouvez par contre ajouter d'autres méthodes (ça devrait 
// être le cas)
class Board
{
    private Mark[][] board;

   

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
                        return -100;
                    }
                    break;
                case X:
                    if (isWinning(Mark.O)) {
                        return -100;
                    }
                    break;
                case EMPTY:
                    break;
            }
        }
        // If neither are winning, then it's a draw
        return 0;
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
    public boolean checkDiagonal(Mark mark, int index) {
        if (index == 0) {
            for (int i = index; i < this.board.length; i++) {
                if (this.board[i][i] != mark) {
                    return false;
                }
            }
        } else {
            for (int i = index; i >= 0; i--) {
                if (this.board[this.board.length - i - 1][i] != mark) {
                    return false;
                }
            }
        }
        return true;
    }

    // Check if mark is winning with any of the winning conditions
    public boolean isWinning(Mark mark) {
        return checkHorizontal(mark) || checkVertical(mark) || checkDiagonal(mark, 0) || checkDiagonal(mark, this.board.length - 1);
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
}
