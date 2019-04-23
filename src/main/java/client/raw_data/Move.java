package client.raw_data;

public class Move {
    private int row;
    private int col;
    public int getRow(){return row;}
    public int getCol(){return col;}
    Move(int r, int c) { row = r; col = c; }
}
