import java.util.ArrayList;
import java.util.List;

// IMPORTANT: Il ne faut pas changer la signature des méthodes
// de cette classe, ni le nom de la classe.
// Vous pouvez par contre ajouter d'autres méthodes (ça devrait 
// être le cas)
class CPUPlayer
{

    // Contient le nombre de noeuds visités (le nombre
    // d'appel à la fonction MinMax ou Alpha Beta)
    // Normalement, la variable devrait être incrémentée
    // au début de votre MinMax ou Alpha Beta.
    private int numExploredNodes;
    private Mark mark;

    // Le constructeur reçoit en paramètre le
    // joueur MAX (X ou O)
    public CPUPlayer(Mark cpu){
        this.mark = cpu;
    }

    // Ne pas changer cette méthode
    public int  getNumOfExploredNodes(){
        return numExploredNodes;
    }

    // Retourne la liste des coups possibles.  Cette liste contient
    // plusieurs coups possibles si et seuleument si plusieurs coups
    // ont le même score.
    public ArrayList<Move> getNextMoveMinMax(Board board)
    {
        numExploredNodes = 0;
        return minmax(board, this.mark.equals(Mark.X), 6);
    }

    // Retourne la liste des coups possibles.  Cette liste contient
    // plusieurs coups possibles si et seuleument si plusieurs coups
    // ont le même score.


    public Mark getMark() {
        return mark;
    }

    private ArrayList<Move> minmax(Board board, boolean isMax, int depth) {
        ArrayList<Move> bestMoves = new ArrayList<Move>();
        numExploredNodes++;
        Mark[][] gameBoard = board.getBoard();
        
        Mark currentMark = isMax ? Mark.X : Mark.O;
        
        if (board.isFull() || depth == 0) {
            int score = board.evaluate(this.mark);
            ArrayList<Move> moveList = new ArrayList<Move>(List.of(new Move(score)));
            return moveList;
        }
    
        int bestScore = isMax ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[0].length; j++) {
                if (gameBoard[i][j] == Mark.EMPTY) {
                    board.play(new Move(i, j), currentMark);
                    int currentScore = minmax(board, !isMax, depth - 1).get(0).getScore();
                    board.play(new Move(i, j), Mark.EMPTY);
                    
                    if ((isMax && currentScore > bestScore) || (!isMax && currentScore < bestScore)) {
                        bestScore = currentScore;
                        bestMoves.clear();
                        bestMoves.add(new Move(i, j, bestScore));
                    } else if (currentScore == bestScore) {
                        bestMoves.add(new Move(i, j, bestScore));
                    }
                }
            }
        }
        return bestMoves;
    }

//    private ArrayList<Move> alphaBeta(BigBoard bigBoard, boolean isMax, int depth, int alpha, int beta) {
//        ArrayList<Move> bestMoves = new ArrayList<Move>();
//        numExploredNodes++;
//        Board[][] boards = bigBoard.getBoards();
//
//        Mark currentMark = isMax ? Mark.X : Mark.O;
//
//        if (bigBoard.isFull() || depth == 0) {
//            int score = bigBoard.evaluateBigBoard(boards, this.mark) ? (isMax ? Integer.MAX_VALUE : Integer.MIN_VALUE) : 0;
//            return new ArrayList<Move>(List.of(new Move(score)));
//        }
//
//        int bestScore = isMax ? Integer.MIN_VALUE : Integer.MAX_VALUE;
//        for (int i = 0; i < boards.length; i++) {
//            for (int j = 0; j < boards[0].length; j++) {
//                if (boards[i][j].evaluate(currentMark) == 0 && !boards[i][j].isFull()) {
//                    for (int k = 0; k < 3; k++) {
//                        for (int l = 0; l < 3; l++) {
//                            if (boards[i][j].getBoard()[k][l] == Mark.EMPTY) {
//                                boards[i][j].play(new Move(k, l), currentMark);
//                                int currentScore = alphaBeta(bigBoard, !isMax, depth - 1, alpha, beta).get(0).getScore();
//                                boards[i][j].play(new Move(k, l), Mark.EMPTY);
//
//                                if (isMax) {
//                                    if (currentScore > bestScore) {
//                                        bestScore = currentScore;
//                                        bestMoves.clear();
//                                        bestMoves.add(new Move(i * 3 + k, j * 3 + l, bestScore));
//                                    } else if (currentScore == bestScore) {
//                                        bestMoves.add(new Move(i * 3 + k, j * 3 + l, bestScore));
//                                    }
//                                    alpha = Math.max(alpha, bestScore);
//                                } else {
//                                    if (currentScore < bestScore) {
//                                        bestScore = currentScore;
//                                        bestMoves.clear();
//                                        bestMoves.add(new Move(i * 3 + k, j * 3 + l, bestScore));
//                                    } else if (currentScore == bestScore) {
//                                        bestMoves.add(new Move(i * 3 + k, j * 3 + l, bestScore));
//                                    }
//                                    beta = Math.min(beta, bestScore);
//                                }
//                                if (beta <= alpha) {
//                                    break;
//                                }
//                            }
//                        }
//                        if (beta <= alpha) {
//                            break;
//                        }
//                    }
//                }
//            }
//            if (beta <= alpha) {
//                break;
//            }
//        }
//        return bestMoves;
//    }

    public ArrayList<Move> getNextMoveAB(BigBoard bigBoard, String lastMove) {
        int depth = 7;

        if (bigBoard.getValidMoves(lastMove).size() > 30) depth = 4;  // Reduce depth early game
        if (bigBoard.getValidMoves(lastMove).size() < 10) depth = 8;  // Increase depth late game

        ArrayList<Move> bestMoves = alphaBeta(bigBoard, this.mark.equals(Mark.X), depth, Integer.MIN_VALUE, Integer.MAX_VALUE, lastMove);

        // 🚀 Only print final selected moves (not every recursive call)
        System.out.println("Final best moves: " + bestMoves);

        return bestMoves;

    }

    public ArrayList<Move> alphaBeta(BigBoard bigBoard, boolean isMax, int depth, int alpha, int beta, String lastMove) {
        ArrayList<Move> bestMoves = new ArrayList<>();
        numExploredNodes++;
        Board[][] boards = bigBoard.getBoards();
        Mark currentMark = isMax ? this.mark : (this.mark == Mark.X ? Mark.O : Mark.X);

        //terminal condition
        if (bigBoard.isFull() || depth == 0) {
            int score = bigBoard.evaluateBigBoard(this.mark);
            return new ArrayList<>(List.of(new Move(score)));
        }

        int bestScore = isMax ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        // Get valid moves based on the last move
        ArrayList<Move> validMoves = bigBoard.getValidMoves(lastMove);

        for (Move move : validMoves) {
            int boardRow = move.getRow() / 3;
            int boardCol = move.getCol() / 3;
            int localRow = move.getRow() % 3;
            int localCol = move.getCol() % 3;

            // Play the move
            boards[boardRow][boardCol].play(new Move(localRow, localCol), currentMark);

            // Get next move string representation for recursive call
            String nextMove = String.format("%c%d", 'A' + move.getCol(), 9 - move.getRow());

            // Recursive call
            ArrayList<Move> result = alphaBeta(bigBoard, !isMax, depth - 1, alpha, beta, nextMove);
            int currentScore = result.isEmpty() ? 0 : result.get(0).getScore();

            // Undo move
            boards[boardRow][boardCol].play(new Move(localRow, localCol), Mark.EMPTY);

            if (isMax) {
                if (currentScore > bestScore) {
                    bestScore = currentScore;
                    bestMoves.clear();
                    bestMoves.add(new Move(move.getRow(), move.getCol(), bestScore));
                } else if (currentScore == bestScore) {
                    bestMoves.add(new Move(move.getRow(), move.getCol(), bestScore));
                }
                alpha = Math.max(alpha, bestScore);
            } else {
                if (currentScore < bestScore) {
                    bestScore = currentScore;
                    bestMoves.clear();
                    bestMoves.add(new Move(move.getRow(), move.getCol(), bestScore));
                } else if (currentScore == bestScore) {
                    bestMoves.add(new Move(move.getRow(), move.getCol(), bestScore));
                }
                beta = Math.min(beta, bestScore);
            }

            if (beta <= alpha) {
                break;
            }
        }
//        if (bestMoves.isEmpty()) {
//            return new ArrayList<>(List.of(new Move(isMax ? Integer.MIN_VALUE : Integer.MAX_VALUE)));
//        }

        return bestMoves;
    }

}
