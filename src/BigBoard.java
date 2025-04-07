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
        this.boards[localRow / 3][localCol / 3].isWinning(mark);
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
        if (this.isWinningBigBoard(Mark.X) || this.isWinningBigBoard(Mark.O)) {
            return true;
        }
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
                return -1000;
        } else {
            if (isWinningBigBoard(Mark.X))
                return -1000;
        }

        if (isWinningBigBoard(mark))
            return 1000;

        // More sophisticated scoring
        int score = calculateBoardControlScore(mark);
        //score += calculateStrategicPositionsScore(mark);
        score += calculatePotentialWinScore(mark);

        return score;
    }


// Evaluation : Board control

    private int calculateBoardControlScore(Mark mark) {
        int score = 0;
        Mark opponent = (mark == Mark.X) ? Mark.O : Mark.X;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (boards[i][j].getWinner() == mark) {
                    score += 15; // Slightly higher value for won boards
                } else if (boards[i][j].getWinner() == opponent) {
                    score -= 15;
                } else {
                    // More granular scoring for partial board control
                    score += evaluateIndividualBoard(boards[i][j], mark);
                }
            }
        }
        return score;
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


// Evaluation : Strategic positions

    private int calculateStrategicPositionsScore(Mark mark) {
        return evaluatePartialBigBoardWins(mark) * 2; // Multiply existing partial win logic
    }

    private int evaluatePartialBigBoardWins(Mark mark) {
        int score = 0;
        // Check rows for 2 out of 3 wins
        for (int i = 0; i < 3; i++) {
            int winCount = 0;
            for (int j = 0; j < 3; j++) {
                if (boards[i][j].getWinner() == mark)
                    winCount++;
            }
            if (winCount == 2)
                score += 5;
        }

        // Check columns for 2 out of 3 wins
        for (int j = 0; j < 3; j++) {
            int winCount = 0;
            for (int i = 0; i < 3; i++) {
                if (boards[i][j].getWinner() == mark)
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



    private int calculatePotentialWinScore(Mark mark) {
        int potentialScore = 0;
        Mark opponent = (mark == Mark.X) ? Mark.O : Mark.X;

        // Center control on the big board
        potentialScore += isCenterControlled(mark);

        // Corner control on the big board
        potentialScore += hasCornerAdvantage(mark);

        // Combined Threat & Chain Reaction Scoring in a Single Pass
        potentialScore += calculateThreatAndChainScore(mark, opponent);

        return potentialScore;
    }

    public int isCenterControlled(Mark mark) {
        Mark opponent = (mark == Mark.X) ? Mark.O : Mark.X;
        if (boards[1][1].hasMark(mark) && boards[1][1].hasMark(opponent) && this.boards[1][1].getWinner() == null) return 0;
        if (boards[1][1].hasMark(opponent) && this.boards[1][1].getWinner() == null) return -15;
        if (boards[1][1].hasMark(mark) && this.boards[1][1].getWinner() == null) return 15;
        if (boards[1][1].getWinner() == mark) return 30;
        if (boards[1][1].getWinner() == opponent) return -30;
        return 0;
    }

    public int hasCornerAdvantage(Mark mark) {
        Mark opponent = (mark == Mark.X) ? Mark.O : Mark.X;

        // Check if corner are owned
        if ((boards[0][0].getWinner() == mark ||
                boards[0][2].getWinner() == mark ||
                boards[2][0].getWinner() == mark ||
                boards[2][2].getWinner() == mark) 
                &&
                (boards[0][0].getWinner() == opponent ||
                boards[0][2].getWinner() == opponent ||
                boards[2][0].getWinner() == opponent ||
                boards[2][2].getWinner() == opponent))
                {
            return 0;
        }
        if (boards[0][0].getWinner() == mark ||
                boards[0][2].getWinner() == mark ||
                boards[2][0].getWinner() == mark ||
                boards[2][2].getWinner() == mark){
            return 16;
        }
        if (boards[0][0].getWinner() == opponent ||
                boards[0][2].getWinner() == opponent ||
                boards[2][0].getWinner() == opponent ||
                boards[2][2].getWinner() == opponent){
            return -16;
        }
        if ((boards[0][0].hasMark(mark) && this.boards[0][0].getWinner() == null ||
                boards[0][2].hasMark(mark) && this.boards[0][2].getWinner() == null ||
                boards[2][0].hasMark(mark) && this.boards[2][0].getWinner() == null ||
                boards[2][2].hasMark(mark) && this.boards[2][2].getWinner() == null)
                &&
                (boards[0][0].hasMark(opponent) && this.boards[0][0].getWinner() == null ||
                boards[0][2].hasMark(opponent) && this.boards[0][2].getWinner() == null ||
                boards[2][0].hasMark(opponent) && this.boards[2][0].getWinner() == null ||
                boards[2][2].hasMark(opponent) && this.boards[2][2].getWinner() == null)){
            return 0;
        }

        if (boards[0][0].hasMark(mark) && this.boards[0][0].getWinner() == null ||
                boards[0][2].hasMark(mark) && this.boards[0][2].getWinner() == null ||
                boards[2][0].hasMark(mark) && this.boards[2][0].getWinner() == null ||
                boards[2][2].hasMark(mark) && this.boards[2][2].getWinner() == null){
            return 8;
                }
        if (boards[0][0].hasMark(opponent) && this.boards[0][0].getWinner() == null ||
                boards[0][2].hasMark(opponent) && this.boards[0][2].getWinner() == null ||
                boards[2][0].hasMark(opponent) && this.boards[2][0].getWinner() == null ||
                boards[2][2].hasMark(opponent) && this.boards[2][2].getWinner() == null){
            return -8;
        }
         else {
            return 0;
        }
    }


    private int calculateThreatAndChainScore(Mark mark, Mark opponent) {
        int score = 0;

        for (int i = 0; i < 3; i++) {
            int rowWinThreats = 0, colWinThreats = 0;
            int opponentRowThreats = 0, opponentColThreats = 0;

            for (int j = 0; j < 3; j++) {
                Board board = boards[i][j];
                Board opponentBoard = boards[j][i];

                if (!boards[i][j].isFull()) {
                    score += getThreatScore(boards[i][j], mark); // Threat detection
                    score -= getThreatScore(boards[i][j], opponent);
                }


                rowWinThreats += boards[i][j].getWinner() == mark ? 1 : 0;
                colWinThreats += boards[j][i].getWinner() == mark ? 1 : 0;

                opponentRowThreats += boards[i][j].getWinner() == opponent ? 1 : 0;
                opponentColThreats += boards[j][i].getWinner() == opponent ? 1 : 0;

                if (boards[i][j].getWinner() == opponent) {
                    rowWinThreats = -1; // row cannot be completed
                }
                if (boards[j][i].getWinner() == opponent) {
                    colWinThreats = -1; // col cannot be completed
                }

            }

            if (rowWinThreats == 2) score += 15;  // Strong row threat
            if (colWinThreats == 2) score += 15;  // Strong column threat
            if (opponentRowThreats == 2) score -= 15;
            if (opponentColThreats == 2) score -= 15;
        }

        // Diagonal chain potential
        int mainDiagThreats = 0, antiDiagThreats = 0;
        int opponentMainDiagThreats = 0, opponentAntiDiagThreats = 0;

        for (int i = 0; i < 3; i++) {
            mainDiagThreats += boards[i][i].isWinning(mark) ? 1 : 0;
            antiDiagThreats += boards[i][2 - i].isWinning(mark) ? 1 : 0;
            opponentMainDiagThreats += boards[i][i].isWinning(opponent) ? 1 : 0;
            opponentAntiDiagThreats += boards[i][2 - i].isWinning(opponent) ? 1 : 0;

            if (boards[i][i].getWinner() == opponent) {
                mainDiagThreats = -1; // diag cannot be completed
            }
            if (boards[i][2 - i].getWinner() == opponent) {
                antiDiagThreats = -1; // diag cannot be completed
            }
        }

        if (mainDiagThreats == 2) score += 16;
        if (antiDiagThreats == 2) score += 16;
        if (opponentMainDiagThreats == 2) score -= 16;
        if (opponentAntiDiagThreats == 2) score -= 16;

        return score;
    }

    private int getThreatScore(Board board, Mark mark) {
        Mark[][] tiles = board.getBoard();
        int[] rowCount = new int[3];
        int[] colCount = new int[3];
        int mainDiag = 0, antiDiag = 0;
        int emptySpots = 0;
        int threatsCount = 0; // Count independent threats

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tiles[i][j] == mark) {
                    rowCount[i]++;
                    colCount[j]++;
                    if (i == j)
                        mainDiag++;
                    if (i + j == 2)
                        antiDiag++;
                } else if (tiles[i][j] == Mark.EMPTY) {
                    emptySpots++;
                }
            }
        }

        // Count two-in-a-row threats
        if (rowCount[0] == 2)
            threatsCount++;
        if (rowCount[1] == 2)
            threatsCount++;
        if (rowCount[2] == 2)
            threatsCount++;

        if (colCount[0] == 2)
            threatsCount++;
        if (colCount[1] == 2)
            threatsCount++;
        if (colCount[2] == 2)
            threatsCount++;

        if (mainDiag == 2)
            threatsCount++;
        if (antiDiag == 2)
            threatsCount++;

        if (threatsCount >= 2 && emptySpots > 1)
            return 20; // More than one threat detected
        if (threatsCount == 1)
            return 5; // Normal two-in-a-row singular threat

        return 0;
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