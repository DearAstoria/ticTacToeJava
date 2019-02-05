package cs4b.proj1;

import javafx.scene.image.ImageView;

public class Game {
    Player [] p;         // players[0] is always Player.X,  players[1] is Player.Y
    int current = 0;
    Board boardUi;
    char [][]board = {{0,0,0},{0,0,0},{0,0,0}};
    boolean gameOver = false;
    Game(){}
    Game(String humanPlayer, Icon icon, Icon firstMover){
        p  = new Player[2];
        if(icon.icon() == Icon.Xchar) {
            p[0] = new Human(humanPlayer, icon, this);
            p[1] = new Cpu(new O(), this);

        }
        else
        {   p[0] = new Cpu(new X(), this);
            p[1] = new Human(humanPlayer, icon, this);
        }
        p[0].setOpponent(p[1]);
        p[1].setOpponent(p[0]);
        if(firstMover.icon() == Icon.Ochar)
            current = 1;
    }

    Game(String xPlayer, String yPlayer, Icon firstMover){

        p = new Player[2];
        p[0] = new Human(xPlayer, new X(), this);
        p[1] = new Human(yPlayer, new O(), this);
        p[0].setOpponent(p[1]);
        p[1].setOpponent(p[0]);
        if(firstMover.icon() == Icon.Ochar)
            current = 1;
    }

    public void play(){
        boardUi = new Board(this);
        boardUi.play(p, current);
        //p[current].setMyTurn();
        p[0].setGame(boardUi);
        p[1].setGame(boardUi);
        p[current].move();
        }



    boolean occupied(int i, int j){
        return board[i][j] != '\0';
    }

    boolean gameIsOver(){     // incomplete - only returns true if it's a tie
        for(int i=0; i<3; ++i)
            for(int j=0;j<3;++j)
                if(!occupied(i,j))
                    return gameOver = false;
                return gameOver = true;


    }

}
