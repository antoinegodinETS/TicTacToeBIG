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
    public ArrayList<Move> getNextMoveAB(Board board){
        numExploredNodes = 0;
        return alphaBeta(board, this.mark.equals(Mark.X), 6, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

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

    private ArrayList<Move> alphaBeta(Board board, boolean isMax, int depth, int alpha, int beta) {
        ArrayList<Move> bestMoves = new ArrayList<Move>();
        numExploredNodes++;
        Mark[][] gameBoard = board.getBoard();
        
        Mark currentMark = isMax ? Mark.X : Mark.O;
        
        if (board.isFull() || depth == 0) {
            int score = board.evaluate(this.mark);
            return new ArrayList<Move>(List.of(new Move(score)));
        }
    
        int bestScore = isMax ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[0].length; j++) {
                if (gameBoard[i][j] == Mark.EMPTY) {
                    board.play(new Move(i, j), currentMark);
                    int currentScore = alphaBeta(board, !isMax, depth - 1, alpha, beta).get(0).getScore();
                    board.play(new Move(i, j), Mark.EMPTY);
                    
                    if (isMax) {
                        if (currentScore > bestScore) {
                            bestScore = currentScore;
                            bestMoves.clear();
                            bestMoves.add(new Move(i, j, bestScore));
                        } else if (currentScore == bestScore) {
                            bestMoves.add(new Move(i, j, bestScore));
                        }
                        alpha = Math.max(alpha, bestScore);
                    } else {
                        if (currentScore < bestScore) {
                            bestScore = currentScore;
                            bestMoves.clear();
                            bestMoves.add(new Move(i, j, bestScore));
                        } else if (currentScore == bestScore) {
                            bestMoves.add(new Move(i, j, bestScore));
                        }
                        beta = Math.min(beta, bestScore);
                    }
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
            if (beta <= alpha) {
                break;
            }
        }
        return bestMoves;
    }

}
