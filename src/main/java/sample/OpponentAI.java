package sample;

import java.util.Random;
import javafx.scene.text.Text;

public class OpponentAI extends Player
{

    String difficulty; // "easy" or "hard"

    public OpponentAI(String difficulty)
    {
        this.difficulty = difficulty;
        name = "CPU";
    }

    public void nextMove(GameState thisTurn, GameController controller)
    {

        if(thisTurn.isOver()) return;
        if(difficulty.equals("easy"))
            randomMove(thisTurn, controller);
        else System.out.println("shit");
        controller.changeTurn();

    }

    private int randomMove(GameState gs, GameController gc)
    {
        Random generator = new Random();
        int x, y;

        do
        {   x = generator.nextInt(3); // generate a random number from [0,3)
            y = generator.nextInt(3);

        }
        while(gs.occupied(x,y));

        gs.set(gc.pIcon[gc.currentMover], x, y);  // game state

        // UI
        Text t = new Text(Character.toString(gc.pIcon[gc.currentMover]));
        t.setStyle("-fx-font: 64 System;");
        gc.atLocation(x,y).getChildren().add(t);
        gc.atLocation(x,y).setId("Permanent");

        return 0;
    }

    private int calculatedMove()
    {
        return 0;
    }
}