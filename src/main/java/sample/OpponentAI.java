package sample;

import java.util.Random;

public class OpponentAI
{
    String difficulty; // "easy" or "hard"

    public OpponentAI(String difficulty)
    {
        this.difficulty = difficulty;
    }

    public int nextMove(GameState thisTurn)
    {
        return difficulty == "easy" ? randomMove(thisTurn) : -1;
    }

    private int randomMove(GameState thisTurn)
    {
        boolean freeSpaceFound = false;
        Random generator = new Random();
        int x, y;

        while(!freeSpaceFound)
        {
            x = generator.nextInt(3); // generate a random number from [0,3)
            y = generator.nextInt(3);
            if(thisTurn.get(x, y) == 0)
            {
                freeSpaceFound = true;
                thisTurn.set( thisTurn.getTurn() ? 1 : 2, x, y);
                thisTurn.changeTurn();
            }
        }

        return 0;
    }

    private int calculatedMove()
    {
        return 0;
    }
}