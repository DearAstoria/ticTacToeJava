package client.raw_data;

// a class used in evaluating the best move the computer AI can do
public class ScoredMove
{
    public int x, y; // the space on the board
    public int score; // the value of this space

    public ScoredMove() {}
    public ScoredMove(int score)
    {
        this.score = score;
    }
}