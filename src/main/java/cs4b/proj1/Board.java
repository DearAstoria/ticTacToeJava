package cs4b.proj1;

public class Board implements java.io.Serializable
{
    private int[3][3] board;

    @Override
    public String toString() // returns a string of the bord to print in the consol for debuging purposes
    {
        StringBuilder str;
        for(int y = 0; y < 3; y++)
        {
            for(int x = 0; x < 3; x++)
            {
                str.append(board[y][x]).append(", ");
            }
            str.append("\n");
        }
        return str.toString();
    }

    public int checkForWin()
    {
        for(int x = 0; x < 3; x++) // check all horizontal groups
        {
            // if there is a horizontal match AND that match is not empty spaces (empty space = 0)
            if(board[0][x] != 0 && board[0][x] == board[1][x] == board[2][x])
            {
                return board[0][x]; // then return the winning token
            }
        }

        for(int y = 0; y < 3; y++) // check all vertical groups
        {
            // if there is a vertical match AND that match is not empty spaces
            if(board[y][0] != 0 && board[y][0] == board[y][1] == board[y][2])
            {
                return board[y][0]; // then return the winning token
            }
        }

        if(board[0][0] != 0 && board[0][0] == board[1][1] == board[2][2]) // check both diagonal groups
        {
            return board[0][0];
        }
        else if(board[2][2] != 0 && board[2][2] == board[1][1] == board[0][0])
        {
            return board[2][2];
        }
        else // if no winner is found, then determine if there is still a playable/empty space
        {
            for (int y = 0; y < 3; y++)
            {
                for (int x = 0; x < 3; x++)
                {
                    if (board[y][x] == 0) // if an empty space is found return empty token
                    {
                        return board[y][x]; // returning the empty token to show that the game is not over
                    }
                }
            }
        }

        return -1; // if all other cases fail (no winner, and no empty spaces), then this game must be a draw
    }

    public boolean set(int player,   // the player's symbol
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
