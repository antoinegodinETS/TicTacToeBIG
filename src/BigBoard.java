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

    public void play(Move m, Mark mark){
        Move boardMove = new Move(m.getRow() % 3, m.getCol() % 3);
        System.out.println(m.getBigRow());
        System.out.println(m.getBigCol());
        System.out.println(boardMove.getRow());
        System.out.println(boardMove.getCol());
        this.boards[m.getBigRow()][m.getBigCol()].play(boardMove, mark);
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

}