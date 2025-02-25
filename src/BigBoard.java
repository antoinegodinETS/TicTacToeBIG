import java.util.ArrayList;

class BigBoard {
    private Board[][] boards;

    public BigBoard() {
        this.boards = new Board[3][3];
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                this.boards[r][c] = new Board();
            }
        }
    }

    public Board[][] getBoard() {
        return this.boards;
    }

    public void play(String position, Mark mark){
        int localCol = position.charAt(0) - 'A'; 
        int localRow = 9 - Character.getNumericValue(position.charAt(1));

        int globalCol = localCol % 3;
        int globalRow = localRow % 3;

        Board currentBoard = this.boards[localRow/3][localCol/3];

        currentBoard.play(new Move(globalRow, globalCol), mark);
    }

    @Override
    public String toString() {
        String boardVisual = " |-----------------| ";
        for (Board[] row: this.boards) {
            boardVisual += "\n |";
            for(int i = 0; i < 3; i++) {
                boardVisual += " ";
                for (Board board: row) {
                    Mark[][] tiles = board.getBoard();
                    Mark[] tile = tiles[i];
                    for (Mark t: tile) {
                        if (t == Mark.EMPTY) {
                            boardVisual += " ";
                            continue;
                        }
                        boardVisual += t;
                    }
                    boardVisual += " | ";
                }
                boardVisual += "\n |";
            }
            boardVisual += "-----------------| ";

        }

        return boardVisual;
    }

    public ArrayList<Move> getValidMoves(String position) {
        ArrayList<Move> validMoves = new ArrayList<Move>();
        int localCol = position.charAt(0) - 'A'; 
        int localRow = 9 - Character.getNumericValue(position.charAt(1));

        int globalCol = localCol % 3;
        int globalRow = localRow % 3;

        Board nextBoard = this.boards[globalRow][globalCol];

        if (nextBoard.evaluate(Mark.X) == 0 && !nextBoard.isFull()) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (this.boards[globalRow][globalCol].getBoard()[i][j].equals(Mark.EMPTY)) {
                        validMoves.add(new Move(globalRow * 3 + i, globalCol * 3 + j));
                    }
                }
            }
        } else {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (this.boards[i][j].evaluate(Mark.X) == 0 && !this.boards[i][j].isFull()) {
                        for (int k = 0; k < 3; k++) {
                            for (int l = 0; l < 3; l++) {
                                if (this.boards[i][j].getBoard()[k][l].equals(Mark.EMPTY)) {
                                    validMoves.add(new Move(i * 3 + k, j * 3 + l));
                                }
                            }
                        }
                    }
                }
            }
        }
        
        
        return validMoves;
    }

}