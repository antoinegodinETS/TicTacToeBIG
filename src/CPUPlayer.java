import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
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
    private Mark opponentMark;
    private int moveCount;

    // Le constructeur reçoit en paramètre le
    // joueur MAX (X ou O)
    public CPUPlayer(Mark cpu){
        this.mark = cpu;
        if (cpu == Mark.X) {
            this.opponentMark = Mark.O;
        } else {
            this.opponentMark = Mark.X;
        }
    }

    // Ne pas changer cette méthode
    public int  getNumOfExploredNodes(){
        return numExploredNodes;
    }

    public int getMoveCount() {
        return moveCount;
    }

    void incrementMoveCount() {
        moveCount = moveCount + 2;
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

    public Mark getOpponentMark() {
        return opponentMark;
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


    public ArrayList<Move> getNextMoveAB(BigBoard bigBoard, String lastMove) {
//        int depth = Math.min(4 + moveCount / 10, 10);
        int depth =0;

        if (moveCount < 15) depth = 6;  // Reduce depth early game
        if (moveCount >= 15 && moveCount <30) depth = 6; // Increase depth mid game
        if (moveCount >= 30) depth = 6; // Increase depth late game
        System.out.println("depth = " + depth);

        ArrayList<Move> bestMoves = alphaBeta(bigBoard, true, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, lastMove);

        // 🚀 Only print final selected moves (not every recursive call)
        System.out.println("Final best moves: " + bestMoves);

        return bestMoves;

    }

    public ArrayList<Move> alphaBeta(BigBoard previousBigBoard, boolean isMax, int depth, int alpha, int beta, String lastMove) {
        ArrayList<Move> bestMoves = new ArrayList<>();
        numExploredNodes++;
        BigBoard bigBoard = previousBigBoard.copy();
        Board[][] boards = bigBoard.getBoards();
        Mark currentMark = isMax ? this.getMark() : this.getOpponentMark();
        //terminal condition
        if (bigBoard.isFull() || depth == 0) {
            int score = bigBoard.evaluateBigBoard(this.getMark());
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
