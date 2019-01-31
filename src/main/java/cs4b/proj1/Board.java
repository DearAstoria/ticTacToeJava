package cs4b.proj1;

public class Board
{
    private int[3][3] board;

    boolean set(int player,   // the player's symbol
                int x, int y) // position where to place player's symbol
    {
        if(board[x][y] == 0) // if this board space is empty
        {
            board[x][y] = player; // set board
            return true; // return that the player has used his turn
        }
        else // else the player entered an illigal move and his turn is not over
        {
            return false;
        }

    }

    public Board(int a, int b, int c,
                 int d, int e, int f,
                 int g, int h, int i)
    {
        board[0][0] = a;
        board[0][1] = b;
        board[0][2] = c;

        board[1][0] = d;
        board[1][1] = e;
        board[1][2] = f;

        board[2][0] = g;
        board[2][1] = h;
        board[2][2] = i;
    }

    public Board(String filepath)
    {
        // initailize board from a saved game
    }

    public Board()
    {
        for(int x = 0; i < 3; x++)
        {
            for(int y = 0; i < 3; y++)
            {
                board[x][y] = 0;
            }
        }
    }
}
