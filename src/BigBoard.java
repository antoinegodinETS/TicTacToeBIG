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

    public boolean isFull() {
        for (Board[] row : boards) {
            for (Board board : row) {
                if (!board.isFull()) {
                    return false;
                }
            }
        }
        return true;
    }

    public Board[][] getBoards() {
        return this.boards;
    }

//    public int evaluateBigBoard(Mark mark) {
//        if (isWinningBigBoard(mark)) {
//            return 100;
//        } else {
//            switch (mark) {
//                case O:
//                    if (isWinningBigBoard(Mark.X)) {
//                        return -100;
//                    }
//                    break;
//                case X:
//                    if (isWinningBigBoard(Mark.O)) {
//                        return -100;
//                    }
//                    break;
//                case EMPTY:
//                    break;
//            }
//        }
//        //if neither are winning, then it's a draw
//        return 0;
//    }

//    public int evaluateBigBoard(Mark mark) {
//        // If there's a full win, return high value
//        if (isWinningBigBoard(mark)) {
//            return 100;
//        } else if (isWinningBigBoard(mark == Mark.X ? Mark.O : Mark.X)) {
//            return -100;
//        }
//
//        // Otherwise, calculate a heuristic score based on progress
//        int score = 0;
//
//        // Count wins in individual boards
//        for (int i = 0; i < 3; i++) {
//            for (int j = 0; j < 3; j++) {
//                if (boards[i][j].isWinning(mark)) {
//                    score += 10;
//                } else if (boards[i][j].isWinning(mark == Mark.X ? Mark.O : Mark.X)) {
//                    score -= 10;
//                } else {
//                    // Add smaller scores for advantageous positions within each board
//                    score += evaluateIndividualBoard(boards[i][j], mark);
//                }
//            }
//        }
//
//        // Check if we have 2 boards won in crucial winning patterns
//        score += evaluatePartialBigBoardWins(mark);
//
//        return score;
//    }
//
//    private int evaluateIndividualBoard(Board board, Mark mark) {
//        int score = 0;
//        Mark[][] tiles = board.getBoard();
//
//        // Check for two-in-a-row opportunities
//        // Rows
//        for (int i = 0; i < 3; i++) {
//            int markCount = 0;
//            int emptyCount = 0;
//            for (int j = 0; j < 3; j++) {
//                if (tiles[i][j] == mark) markCount++;
//                if (tiles[i][j] == Mark.EMPTY) emptyCount++;
//            }
//            if (markCount == 2 && emptyCount == 1) score += 2;
//        }
//
//        // Columns
//        for (int j = 0; j < 3; j++) {
//            int markCount = 0;
//            int emptyCount = 0;
//            for (int i = 0; i < 3; i++) {
//                if (tiles[i][j] == mark) markCount++;
//                if (tiles[i][j] == Mark.EMPTY) emptyCount++;
//            }
//            if (markCount == 2 && emptyCount == 1) score += 2;
//        }
//
//        // Diagonals
//        // Main diagonal
//        int markCount = 0;
//        int emptyCount = 0;
//        for (int i = 0; i < 3; i++) {
//            if (tiles[i][i] == mark) markCount++;
//            if (tiles[i][i] == Mark.EMPTY) emptyCount++;
//        }
//        if (markCount == 2 && emptyCount == 1) score += 2;
//
//        // Anti-diagonal
//        markCount = 0;
//        emptyCount = 0;
//        for (int i = 0; i < 3; i++) {
//            if (tiles[i][2-i] == mark) markCount++;
//            if (tiles[i][2-i] == Mark.EMPTY) emptyCount++;
//        }
//        if (markCount == 2 && emptyCount == 1) score += 2;
//
//        return score;
//    }
//
    private int evaluatePartialBigBoardWins(Mark mark) {
        int score = 0;
        // Check rows for 2 out of 3 wins
        for (int i = 0; i < 3; i++) {
            int winCount = 0;
            for (int j = 0; j < 3; j++) {
                if (boards[i][j].isWinning(mark)) winCount++;
            }
            if (winCount == 2) score += 5;
        }

        // Check columns for 2 out of 3 wins
        for (int j = 0; j < 3; j++) {
            int winCount = 0;
            for (int i = 0; i < 3; i++) {
                if (boards[i][j].isWinning(mark)) winCount++;
            }
            if (winCount == 2) score += 5;
        }

        // Check diagonals
        int mainDiagWins = 0;
        int antiDiagWins = 0;
        for (int i = 0; i < 3; i++) {
            if (boards[i][i].isWinning(mark)) mainDiagWins++;
            if (boards[i][2-i].isWinning(mark)) antiDiagWins++;
        }
        if (mainDiagWins == 2) score += 5;
        if (antiDiagWins == 2) score += 5;

        return score;
    }
//
    public boolean isWinningBigBoard(Mark mark) {
        return checkHorizontalBigBoard(mark) || checkVerticalBigBoard(mark) || checkDiagonalBigBoard(mark);
    }

    public boolean checkDiagonalBigBoard(Mark mark) {
        boolean diag1Win = true;
        boolean diag2Win = true;
        for (int i = 0; i < 3; i++) {
            if (!this.boards[i][i].isWinning(mark)) {
                diag1Win = false;
            }
            if (!this.boards[i][2 - i].isWinning(mark)) {
                diag2Win = false;
            }
        }
        return diag1Win || diag2Win;
    }


    public boolean checkHorizontalBigBoard(Mark mark) {
        for (int i = 0; i < 3; i++) {
            boolean rowWin = true;
            for (int j = 0; j < 3; j++) {
                if (!this.boards[i][j].isWinning(mark)) {
                    rowWin = false;
                    break;
                }
            }
            if (rowWin) {
                return true;
            }
        }
        return false;
    }

    public boolean checkVerticalBigBoard(Mark mark) {
        for (int i = 0; i < 3; i++) {
            boolean colWin = true;
            for (int j = 0; j < 3; j++) {
                if (!this.boards[j][i].isWinning(mark)) {
                    colWin = false;
                    break;
                }
            }
            if (colWin) {
                return true;
            }
        }
        return false;
    }

    public int evaluateBigBoard(Mark mark) {
        // Quick win/loss detection
        if (isWinningBigBoard(mark)) return 100;
        if (isWinningBigBoard(mark == Mark.X ? Mark.O : Mark.X)) return -100;

        // More sophisticated scoring
        int score = calculateBoardControlScore(mark);
        score += calculateStrategicPositionsScore(mark);
        score += calculatePotentialWinScore(mark);

        return score;
    }

    private int calculateBoardControlScore(Mark mark) {
        int score = 0;
        Mark opponent = (mark == Mark.X) ? Mark.O : Mark.X;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (boards[i][j].isWinning(mark)) {
                    score += 15;  // Slightly higher value for won boards
                } else if (boards[i][j].isWinning(opponent)) {
                    score -= 15;
                } else {
                    // More granular scoring for partial board control
                    score += evaluateIndividualBoard(boards[i][j], mark);
                }
            }
        }
        return score;
    }

    private int calculateStrategicPositionsScore(Mark mark) {
        return evaluatePartialBigBoardWins(mark) * 2;  // Multiply existing partial win logic
    }

    private int calculatePotentialWinScore(Mark mark) {
        int potentialScore = 0;

        // Evaluate board center and corner control across the big board
        if (isCenterControlled(mark)) potentialScore += 10;
        if (hasCornerAdvantage(mark)) potentialScore += 5;

        return potentialScore;
    }

    private boolean isCenterControlled(Mark mark) {
        return boards[1][1].hasMark(mark);
    }

    private boolean hasCornerAdvantage(Mark mark) {
        return (boards[0][0].hasMark(mark) ||
                boards[0][2].hasMark(mark) ||
                boards[2][0].hasMark(mark) ||
                boards[2][2].hasMark(mark));
    }

    // Enhanced individual board evaluation with positional weighting
    private int evaluateIndividualBoard(Board board, Mark mark) {
        int score = 0;
        Mark[][] tiles = board.getBoard();
        Mark opponent = (mark == Mark.X) ? Mark.O : Mark.X;

        // Center square is most valuable
        if (tiles[1][1] == mark) score += 3;
        if (tiles[1][1] == opponent) score -= 3;

        // Corner squares are next most valuable
        int[][] cornerPositions = {{0,0}, {0,2}, {2,0}, {2,2}};
        for (int[] pos : cornerPositions) {
            if (tiles[pos[0]][pos[1]] == mark) score += 2;
            if (tiles[pos[0]][pos[1]] == opponent) score -= 2;
        }

        // Two-in-a-row opportunities with more context
        score += calculateTwoInARowOpportunities(board, mark);

        return score;
    }

    private int calculateTwoInARowOpportunities(Board board, Mark mark) {
        int score = 0;
        Mark[][] tiles = board.getBoard();

        // Combine row, column, and diagonal checks
        score += calculateLineOpportunities(tiles, mark, true);  // Rows
        score += calculateLineOpportunities(tiles, mark, false);  // Columns
        score += calculateDiagonalOpportunities(tiles, mark);

        return score;
    }

    private int calculateLineOpportunities(Mark[][] tiles, Mark mark, boolean isRow) {
        int score = 0;
        for (int i = 0; i < 3; i++) {
            int markCount = 0;
            int emptyCount = 0;
            for (int j = 0; j < 3; j++) {
                Mark current = isRow ? tiles[i][j] : tiles[j][i];
                if (current == mark) markCount++;
                if (current == Mark.EMPTY) emptyCount++;
            }
            if (markCount == 2 && emptyCount == 1) score += 3;
        }
        return score;
    }

    private int calculateDiagonalOpportunities(Mark[][] tiles, Mark mark) {
        int score = 0;
        int[][] diagonals = {{0,0,1,1,2,2}, {0,2,1,1,2,0}};

        for (int[] diagonal : diagonals) {
            int markCount = 0;
            int emptyCount = 0;
            for (int i = 0; i < 3; i++) {
                Mark current = tiles[diagonal[i*2]][diagonal[i*2+1]];
                if (current == mark) markCount++;
                if (current == Mark.EMPTY) emptyCount++;
            }
            if (markCount == 2 && emptyCount == 1) score += 3;
        }
        return score;
    }

}