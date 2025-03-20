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

    public void play(String position, Mark mark) {
        int localCol = position.charAt(0) - 'A';
        int localRow = 9 - Character.getNumericValue(position.charAt(1));

        int globalCol = localCol % 3;
        int globalRow = localRow % 3;

        Board currentBoard = this.boards[localRow / 3][localCol / 3];

        currentBoard.play(new Move(globalRow, globalCol), mark);
        this.boards[localRow/3][localCol/3].isWinning(mark);
    }

    @Override
    public String toString() {
        String boardVisual = " |-----------------| ";
        for (Board[] row : this.boards) {
            boardVisual += "\n |";
            for (int i = 0; i < 3; i++) {
                boardVisual += " ";
                for (Board board : row) {
                    Mark[][] tiles = board.getBoard();
                    Mark[] tile = tiles[i];
                    for (Mark t : tile) {
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

//    public ArrayList<Move> getValidMoves(String position) {
//        ArrayList<Move> validMoves = new ArrayList<Move>();
//        int localCol = position.charAt(0) - 'A';
//        int localRow = 9 - Character.getNumericValue(position.charAt(1));
//
//        int globalCol = localCol % 3;
//        int globalRow = localRow % 3;
//
//        Board nextBoard = this.boards[globalRow][globalCol];
////        nextBoard.evaluate(Mark.X);
//
//        if (nextBoard.getWinner() == null && !nextBoard.isFull()) {
//            for (int i = 0; i < 3; i++) {
//                for (int j = 0; j < 3; j++) {
//                    if (this.boards[globalRow][globalCol].getBoard()[i][j].equals(Mark.EMPTY)) {
//                        validMoves.add(new Move(globalRow * 3 + i, globalCol * 3 + j));
//                    }
//                }
//            }
//        } else {
//            for (int i = 0; i < 3; i++) {
//                for (int j = 0; j < 3; j++) {
//                    this.boards[i][j].evaluate(Mark.X);
//                    if (this.boards[i][j].getWinner() == null && !this.boards[i][j].isFull()) {
//                        for (int k = 0; k < 3; k++) {
//                            for (int l = 0; l < 3; l++) {
//                                if (this.boards[i][j].getBoard()[k][l].equals(Mark.EMPTY)) {
//                                    validMoves.add(new Move(i * 3 + k, j * 3 + l));
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        return validMoves;
//    }

//-------------------new-------------------
    public ArrayList<Move> getValidMoves(String position) {
        ArrayList<Move> validMoves = new ArrayList<>();

        int localCol = position.charAt(0) - 'A';
        int localRow = 9 - Character.getNumericValue(position.charAt(1));

        int globalCol = localCol % 3;
        int globalRow = localRow % 3;

        Board nextBoard = this.boards[globalRow][globalCol];
        boolean nextBoardPlayable = nextBoard.getWinner() == null && !nextBoard.isFull();

        if (nextBoardPlayable) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (nextBoard.getBoard()[i][j].equals(Mark.EMPTY)) {
                        validMoves.add(new Move(globalRow * 3 + i, globalCol * 3 + j));
                    }
                }
            }
        } else {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    Board currentBoard = this.boards[i][j];
                    if (currentBoard.getWinner() == null && !currentBoard.isFull()) {
                        for (int k = 0; k < 3; k++) {
                            for (int l = 0; l < 3; l++) {
                                if (currentBoard.getBoard()[k][l].equals(Mark.EMPTY)) {
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

    // public int evaluateBigBoard(Mark mark) {
    // if (isWinningBigBoard(mark)) {
    // return 100;
    // } else {
    // switch (mark) {
    // case O:
    // if (isWinningBigBoard(Mark.X)) {
    // return -100;
    // }
    // break;
    // case X:
    // if (isWinningBigBoard(Mark.O)) {
    // return -100;
    // }
    // break;
    // case EMPTY:
    // break;
    // }
    // }
    // //if neither are winning, then it's a draw
    // return 0;
    // }

    // public int evaluateBigBoard(Mark mark) {
    // // If there's a full win, return high value
    // if (isWinningBigBoard(mark)) {
    // return 100;
    // } else if (isWinningBigBoard(mark == Mark.X ? Mark.O : Mark.X)) {
    // return -100;
    // }
    //
    // // Otherwise, calculate a heuristic score based on progress
    // int score = 0;
    //
    // // Count wins in individual boards
    // for (int i = 0; i < 3; i++) {
    // for (int j = 0; j < 3; j++) {
    // if (boards[i][j].isWinning(mark)) {
    // score += 10;
    // } else if (boards[i][j].isWinning(mark == Mark.X ? Mark.O : Mark.X)) {
    // score -= 10;
    // } else {
    // // Add smaller scores for advantageous positions within each board
    // score += evaluateIndividualBoard(boards[i][j], mark);
    // }
    // }
    // }
    //
    // // Check if we have 2 boards won in crucial winning patterns
    // score += evaluatePartialBigBoardWins(mark);
    //
    // return score;
    // }
    //
    // private int evaluateIndividualBoard(Board board, Mark mark) {
    // int score = 0;
    // Mark[][] tiles = board.getBoard();
    //
    // // Check for two-in-a-row opportunities
    // // Rows
    // for (int i = 0; i < 3; i++) {
    // int markCount = 0;
    // int emptyCount = 0;
    // for (int j = 0; j < 3; j++) {
    // if (tiles[i][j] == mark) markCount++;
    // if (tiles[i][j] == Mark.EMPTY) emptyCount++;
    // }
    // if (markCount == 2 && emptyCount == 1) score += 2;
    // }
    //
    // // Columns
    // for (int j = 0; j < 3; j++) {
    // int markCount = 0;
    // int emptyCount = 0;
    // for (int i = 0; i < 3; i++) {
    // if (tiles[i][j] == mark) markCount++;
    // if (tiles[i][j] == Mark.EMPTY) emptyCount++;
    // }
    // if (markCount == 2 && emptyCount == 1) score += 2;
    // }
    //
    // // Diagonals
    // // Main diagonal
    // int markCount = 0;
    // int emptyCount = 0;
    // for (int i = 0; i < 3; i++) {
    // if (tiles[i][i] == mark) markCount++;
    // if (tiles[i][i] == Mark.EMPTY) emptyCount++;
    // }
    // if (markCount == 2 && emptyCount == 1) score += 2;
    //
    // // Anti-diagonal
    // markCount = 0;
    // emptyCount = 0;
    // for (int i = 0; i < 3; i++) {
    // if (tiles[i][2-i] == mark) markCount++;
    // if (tiles[i][2-i] == Mark.EMPTY) emptyCount++;
    // }
    // if (markCount == 2 && emptyCount == 1) score += 2;
    //
    // return score;
    // }
    //


    private int evaluatePartialBigBoardWins(Mark mark) {
        int score = 0;
        // Check rows for 2 out of 3 wins
        for (int i = 0; i < 3; i++) {
            int winCount = 0;
            for (int j = 0; j < 3; j++) {
                if (boards[i][j].isWinning(mark))
                    winCount++;
            }
            if (winCount == 2)
                score += 5;
        }

        // Check columns for 2 out of 3 wins
        for (int j = 0; j < 3; j++) {
            int winCount = 0;
            for (int i = 0; i < 3; i++) {
                if (boards[i][j].isWinning(mark))
                    winCount++;
            }
            if (winCount == 2)
                score += 5;
        }

        // Check diagonals
        int mainDiagWins = 0;
        int antiDiagWins = 0;
        for (int i = 0; i < 3; i++) {
            if (boards[i][i].isWinning(mark))
                mainDiagWins++;
            if (boards[i][2 - i].isWinning(mark))
                antiDiagWins++;
        }
        if (mainDiagWins == 2)
            score += 5;
        if (antiDiagWins == 2)
            score += 5;

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
        if (mark == Mark.X) {
            if (isWinningBigBoard(Mark.O))
                return -100;
        } else {
            if (isWinningBigBoard(Mark.X))
                return -100;
        }

        if (isWinningBigBoard(mark))
            return 100;

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
                    score += 15; // Slightly higher value for won boards
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
        return evaluatePartialBigBoardWins(mark) * 2; // Multiply existing partial win logic
    }

//    private int calculatePotentialWinScore(Mark mark) {
//        int potentialScore = 0;
//
//        // Evaluate board center and corner control across the big board
//        if (isCenterControlled(mark))
//            potentialScore += 10;
//        if (hasCornerAdvantage(mark))
//            potentialScore += 5;
//
//        return potentialScore;
//    }

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
        if (tiles[1][1] == mark)
            score += 3;
        if (tiles[1][1] == opponent)
            score -= 3;

        // Corner squares are next most valuable
        int[][] cornerPositions = { { 0, 0 }, { 0, 2 }, { 2, 0 }, { 2, 2 } };
        for (int[] pos : cornerPositions) {
            if (tiles[pos[0]][pos[1]] == mark)
                score += 2;
            if (tiles[pos[0]][pos[1]] == opponent)
                score -= 2;
        }

        // Two-in-a-row opportunities with more context
        score += calculateTwoInARowOpportunities(board, mark);

        return score;
    }

    private int calculateTwoInARowOpportunities(Board board, Mark mark) {
        int score = 0;
        Mark[][] tiles = board.getBoard();

        // Combine row, column, and diagonal checks
        score += calculateLineOpportunities(tiles, mark, true); // Rows
        score += calculateLineOpportunities(tiles, mark, false); // Columns
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
                if (current == mark)
                    markCount++;
                if (current == Mark.EMPTY)
                    emptyCount++;
            }
            if (markCount == 2 && emptyCount == 1)
                score += 3;
        }
        return score;
    }

    private int calculateDiagonalOpportunities(Mark[][] tiles, Mark mark) {
        int score = 0;
        int[][] diagonals = { { 0, 0, 1, 1, 2, 2 }, { 0, 2, 1, 1, 2, 0 } };

        for (int[] diagonal : diagonals) {
            int markCount = 0;
            int emptyCount = 0;
            for (int i = 0; i < 3; i++) {
                Mark current = tiles[diagonal[i * 2]][diagonal[i * 2 + 1]];
                if (current == mark)
                    markCount++;
                if (current == Mark.EMPTY)
                    emptyCount++;
            }
            if (markCount == 2 && emptyCount == 1)
                score += 3;
        }
        return score;
    }


//    --------------new--------------

    private int calculatePotentialWinScore(Mark mark) {
        int potentialScore = 0;
        Mark opponent = (mark == Mark.X) ? Mark.O : Mark.X;

        // Center control on the big board
        potentialScore += isCenterControlled(mark) ? 15 : 0;
        potentialScore -= isCenterControlled(opponent) ? 15 : 0;

        // Corner control on the big board
        potentialScore += hasCornerAdvantage(mark) ? 8 : 0;
        potentialScore -= hasCornerAdvantage(opponent) ? 8 : 0;

        // Combined Threat & Chain Reaction Scoring in a Single Pass
        potentialScore += calculateThreatAndChainScore(mark, opponent);

        return potentialScore;
    }

    private int calculateThreatAndChainScore(Mark mark, Mark opponent) {
        int score = 0;

        for (int i = 0; i < 3; i++) {
            int rowWinThreats = 0, colWinThreats = 0;
            int opponentRowThreats = 0, opponentColThreats = 0;

            for (int j = 0; j < 3; j++) {
                if (!boards[i][j].isFull()) {
                    if (hasTwoInARowWithinBoard(boards[i][j], mark)) score += 5; // Threat detection
                    if (hasTwoInARowWithinBoard(boards[i][j], opponent)) score -= 5;
                }

                rowWinThreats += boards[i][j].isWinning(mark) ? 1 : 0;
                colWinThreats += boards[j][i].isWinning(mark) ? 1 : 0;
                opponentRowThreats += boards[i][j].isWinning(opponent) ? 1 : 0;
                opponentColThreats += boards[j][i].isWinning(opponent) ? 1 : 0;
            }

            if (rowWinThreats == 2) score += 7;  // Strong row threat
            if (colWinThreats == 2) score += 7;  // Strong column threat
            if (opponentRowThreats == 2) score -= 7;
            if (opponentColThreats == 2) score -= 7;
        }

        // Diagonal chain potential
        int mainDiagThreats = 0, antiDiagThreats = 0;
        int opponentMainDiagThreats = 0, opponentAntiDiagThreats = 0;

        for (int i = 0; i < 3; i++) {
            mainDiagThreats += boards[i][i].isWinning(mark) ? 1 : 0;
            antiDiagThreats += boards[i][2 - i].isWinning(mark) ? 1 : 0;
            opponentMainDiagThreats += boards[i][i].isWinning(opponent) ? 1 : 0;
            opponentAntiDiagThreats += boards[i][2 - i].isWinning(opponent) ? 1 : 0;
        }

        if (mainDiagThreats == 2) score += 7;
        if (antiDiagThreats == 2) score += 7;
        if (opponentMainDiagThreats == 2) score -= 7;
        if (opponentAntiDiagThreats == 2) score -= 7;

        return score;
    }

    private boolean hasTwoInARowWithinBoard(Board board, Mark mark) {
        Mark[][] tiles = board.getBoard();

        // Rows and Columns
        for (int i = 0; i < 3; i++) {
            if ((tiles[i][0] == mark && tiles[i][1] == mark && tiles[i][2] == Mark.EMPTY) ||
                    (tiles[i][0] == mark && tiles[i][2] == mark && tiles[i][1] == Mark.EMPTY) ||
                    (tiles[i][1] == mark && tiles[i][2] == mark && tiles[i][0] == Mark.EMPTY)) {
                return true;
            }

            if ((tiles[0][i] == mark && tiles[1][i] == mark && tiles[2][i] == Mark.EMPTY) ||
                    (tiles[0][i] == mark && tiles[2][i] == mark && tiles[1][i] == Mark.EMPTY) ||
                    (tiles[1][i] == mark && tiles[2][i] == mark && tiles[0][i] == Mark.EMPTY)) {
                return true;
            }
        }

        // Diagonals
        if ((tiles[0][0] == mark && tiles[1][1] == mark && tiles[2][2] == Mark.EMPTY) ||
                (tiles[0][0] == mark && tiles[2][2] == mark && tiles[1][1] == Mark.EMPTY) ||
                (tiles[1][1] == mark && tiles[2][2] == mark && tiles[0][0] == Mark.EMPTY)) {
            return true;
        }

        if ((tiles[0][2] == mark && tiles[1][1] == mark && tiles[2][0] == Mark.EMPTY) ||
                (tiles[0][2] == mark && tiles[2][0] == mark && tiles[1][1] == Mark.EMPTY) ||
                (tiles[1][1] == mark && tiles[2][0] == mark && tiles[0][2] == Mark.EMPTY)) {
            return true;
        }

        return false;
    }


    public BigBoard copy() {
        BigBoard newBigBoard = new BigBoard();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                newBigBoard.boards[i][j] = this.boards[i][j].copy();
            }
        }
        return newBigBoard;
    }



}