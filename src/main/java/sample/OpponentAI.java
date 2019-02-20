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
        if(difficulty == "easy")
            randomMove(thisTurn, controller);
        //else bestMove
        controller.changeTurn();

    }

    private int randomMove(GameState thisTurn, GameController controller)
    {
        Random generator = new Random();
        int x, y;

        do
        {   x = generator.nextInt(3); // generate a random number from [0,3)
            y = generator.nextInt(3);

        }
        while(thisTurn.occupied(x,y));

        thisTurn.set(controller.pIcon[controller.currentMover], x, y);
        controller.atLocation(x,y).getChildren().add(new Text(Character.toString(controller.pIcon[controller.currentMover])));
        controller.atLocation(x,y).setId("Permanent");

        return 0;
    }

    private int calculatedMove()
    {
        return 0;
    }
}