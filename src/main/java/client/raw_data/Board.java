package client.raw_data;

public class Board
{
    char [][] space;

    public Board()
    {
        space = new char[3][3];
        clear();
    }

    public void set(char c, int x, int y)
    {
        space[x][y] = c;
    }

    public char get(int x, int y) { return space[x][y]; }

    public void clear()
    {
        for(int x = 0; x < 3; x++) {
            for(int y = 0; y < 3; y++) {
                space[x][y] = ' ';
            }
        }
    }

    @Override
    public String toString()
    {
        StringBuilder board = new StringBuilder();
        for(int y = 0; y < 3; y ++) {
            for(int x = 0; x < 3; x++) {
                if(space[y][x] == ' ') {
                    board.append('-'); // if the space is empty use '-' to visibly display the whitespace character
                } else {
                    board.append(space[y][x]);
                }
            }
            board.append("\n");
        }
        return board.toString();
    }
}
