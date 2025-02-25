

class Move
{
    private int row;
    private int col;
    private int score;

    public Move(){
        row = -1;
        col = -1;
    }

    public Move(int r, int c){
        row = r;
        col = c;
    }

    @Override
    public String toString() {
        return "Move [row=" + row + ", col=" + col + "]";
    }

    public Move(int r, int c, int score){
        row = r;
        col = c;
        this.score = score;
    }

    public Move(int score){
        row = -1;
        col = -1;
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public int getRow(){
        return row;
    }

    public int getCol(){
        return col;
    }

    public int getBigRow(){
        return row / 3;
    }

    public int getBigCol(){
        return col / 3;
    }

    public void setRow(int r){
        row = r;
    }

    public void setCol(int c){
        col = c;
    }
}
