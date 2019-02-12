package cs4b.proj1;

import javafx.scene.image.ImageView;

import java.io.*;


public class Game implements Serializable{
    boolean isNewGame = true;
    //Player [] p;         // players[0] is always Player.X,  players[1] is Player.Y
    Player  x_Player = null,
            o_Player = null,
            winner = null,
            current = null;

    transient Board boardUi;
    char [][]board = {{0,0,0},{0,0,0},{0,0,0}};
    boolean gameOver = false;
    static final File savedGame = new File("savedGame.bin");
    Game(){}
    Game(String humanPlayer, Icon playersIcon, Icon firstMover){
        //p  = new Player[2];
        if(playersIcon.icon() == Icon.Xchar) {
            x_Player = new Human(humanPlayer, playersIcon, this);
            o_Player = new Cpu(new O(), this);

        }
        else
        {   x_Player = new Cpu(new X(), this);
            o_Player = new Human(humanPlayer, playersIcon, this);
        }
        x_Player.setOpponent(o_Player);
        o_Player.setOpponent(x_Player);
        if(firstMover.icon() == Icon.Xchar)
            current = x_Player;
        else
            current = o_Player;
    }

    Game(String xPlayer, String yPlayer, Icon firstMover){

        //p = new Player[2];
        x_Player = new Human(xPlayer, new X(), this);
        o_Player = new Human(yPlayer, new O(), this);
        x_Player.setOpponent(o_Player);
        o_Player.setOpponent(x_Player);
        if(firstMover.icon() == Icon.Xchar)
            current = x_Player;
        else
            current = o_Player;
    }

    public void play(){
        boardUi = new Board(this);
        boardUi.show();
        x_Player.setGame(boardUi);
        o_Player.setGame(boardUi);
        current.move();

        }



    boolean occupied(int i, int j){
        return board[i][j] != '\0';
    }

    boolean gameIsOver(){
        if(updateWinner() != null) {  // if there is a winner, return true
            System.out.println("Winner is " + winner);
            savedGame.delete();
            return true;
        }

        for(int i=0; i<3; ++i)        // else check for tie
            for(int j=0;j<3;++j)
                if(!occupied(i,j))
                    return gameOver = false;

        System.out.println("no winner");
        savedGame.delete();
        return gameOver = true;
    }

    public static Game restore() throws Exception{
        FileInputStream fileInput = new FileInputStream(savedGame);
        ObjectInputStream objInput = new ObjectInputStream((fileInput));
        return (Game)objInput.readObject();


    }

    public static void save(Game game) throws Exception{
        //if(savedGame.)
        game.isNewGame = false;
        FileOutputStream fileOutput = new FileOutputStream(savedGame, false);
        ObjectOutputStream objOutput = new ObjectOutputStream((fileOutput));
        objOutput.writeObject(game);
    }

    @Override
    public String toString() // returns a string of the bord to print in the consol for debuging purposes
    {
        StringBuilder str = null;
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                str.append(board[y][x]).append(", ");
            }
            str.append("\n");
        }
        return str.toString();
    }

    public Player updateWinner() {
        for (int x = 0; x < 3; x++) // check all horizontal groups
        {
            // if there is a horizontal match AND that match is not empty spaces (empty space = 0)
            if (board[0][x] != 0 && board[0][x] == board[1][x] && board[1][x] == board[2][x]) {
                return winner = playerOf(board[0][x]); // then return the winning token
            }
        }

        for (int y = 0; y < 3; y++) // check all vertical groups
        {
            // if there is a vertical match AND that match is not empty spaces
            if (board[y][0] != 0 && board[y][0] == board[y][1] && board[y][1] == board[y][2]) {
                return winner = playerOf(board[y][0]); // then return the winning token
            }
        }

        if (board[0][0] != 0 && board[0][0] == board[1][1] && board[1][1] == board[2][2]) // check both diagonal groups
        {
            return winner = playerOf(board[0][0]);
        }
        else if (board[2][0] != 0 && board[2][0] == board[1][1] && board[1][1] == board[0][2]) {
            return winner = playerOf(board[2][2]);
        }

        return null;
    }

    private Player playerOf(char c){
        if (c == x_Player.getIcon())
            return winner = x_Player;
        else if (c == o_Player.getIcon())
            return winner = o_Player;
        return null;
    }
}


    /*public boolean set(int player,   // the player's symbol
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
}*/