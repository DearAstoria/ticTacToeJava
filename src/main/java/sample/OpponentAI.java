package sample;

import java.util.Random;
import javafx.scene.text.Text;
import java.io.*;
import java.util.Vector;

public class OpponentAI extends Player implements Serializable{

    String difficulty;
    GameState gamestate;
    char seed;

    public OpponentAI(String difficulty)//, char seed, GameState gs)
    {
        this.difficulty = difficulty;
        name = "CPU";
        //this.seed = seed;
        //gamestate = gs;

    }

    public void nextMove(GameState thisTurn)
    {

        if(thisTurn.isOver()) return;
        if(difficulty.equals("easy"))
            randomMove(thisTurn);
        else
            bestMove(thisTurn);
            System.out.println("shit");

        thisTurn.changeTurn();
    }

    private int randomMove(GameState gs)
    {
        Random generator = new Random();
        int x, y;

        do
        {   x = generator.nextInt(3); // generate a random number from [0,3)
            y = generator.nextInt(3);

        }
        while(gs.occupied(x,y));

        update(x, y, gs);


        return 0;
    }

    private void bestMove(GameState gs)  // (variable "game" in GameController,  "this" in GameController)
    {
        int x = 0, y =0;

        ScoredMove bm = minmax(gs);

        System.out.println(gs.pIcon[gs.currentMover] + ": (" + bm.x + ',' + bm.y + ')');

        update(bm.x,bm.y, gs);
    }

    private ScoredMove minmax(GameState gs)
    {
        // check if the minmax method has reached it's recursive endstate
        char winner = gs.checkForWin();
        if( winner == 'x') { // x wins
            return new ScoredMove(10);
        } else if ( winner == 'o') { // o wins
            return new ScoredMove(-10);
        } else if ( winner == 'T') { // tie game
            return new ScoredMove(0);
        }

        Vector<ScoredMove> moves; // a list of all potential moves that can be made this turn
        moves = new Vector<ScoredMove>();

        // find and store all potential moves in moves vector
        for (int y = 0; y < 3; y++) {
            for ( int x = 0; x < 3; x++) {
                if(gs.boardSpaces[x][y] == 0) { // if this board space is empty then add it to the potential moves list

                    gs.boardSpaces[x][y] = gs.pIcon[gs.currentMover];// temporarly set this space for evaluation
                    gs.tempChangeTurn(); // temporarly change the current turn so that it is the next player's turn

                    ScoredMove move = new ScoredMove(); // initialize a record of this potential move
                    move.x = x;
                    move.y = y;

                    // RECURSIVE CALL HERE: set the score of this potential move to the score of the best move the next player can make on his turn
                    move.score = minmax(gs).score;

                    moves.add(move); // add this potential move to the list of all potential moves

                    gs.boardSpaces[x][y] = 0; // restore this space back to it's empty state
                    gs.tempChangeTurn(); // restore the turn to the current player
                }
            }
        }

        // pick the best move out of all the available moves found
        int bestMove = 0;
        if(gs.pIcon[gs.currentMover] == 'x') { //gs.p[gs.currentMover] instanceof OpponentAI) {
            int bestScore = -1000000;
            for (int i = 0; i < moves.size(); i++) {
                if(moves.get(i).score > bestScore) {
                    bestMove = i;
                    bestScore = moves.get(i).score;
                }
            }
        } else {
            int bestScore = 1000000;
            for (int i = 0; i < moves.size(); i++) {
                if(moves.get(i).score < bestScore) {
                    bestMove = i;
                    bestScore = moves.get(i).score;
                }
            }
        }

        return moves.get(bestMove);
    }

    void update(int x, int y,GameState gs){
        gs.set(gs.pIcon[gs.currentMover], x, y);  // game state

        // UI
        Text t = new Text(Character.toString(gs.pIcon[gs.currentMover]));
        t.setStyle("-fx-font: 64 System;");
        gs.atLocation(x,y).getChildren().add(t);
        gs.atLocation(x,y).setId("Permanent");
    }






}