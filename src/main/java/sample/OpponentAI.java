package sample;

import java.util.Random;
import javafx.scene.text.Text;
import java.io.*;

public class OpponentAI extends Player implements Serializable{

    String difficulty; // "easy" or "hard"

    public OpponentAI(String difficulty)
    {
        this.difficulty = difficulty;
        name = "CPU";
    }

    public void nextMove(GameState thisTurn)
    {

        if(thisTurn.isOver()) return;
        if(difficulty.equals("easy"))
            randomMove(thisTurn);
        else System.out.println("shit");
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
        /* Algorythm
        .
        .
        .
        .
        .
        .
        */
        update(x,y, gs);
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