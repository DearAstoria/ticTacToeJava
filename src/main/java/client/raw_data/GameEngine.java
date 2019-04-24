package client.raw_data;

import java.util.Random;
import java.util.Vector;

public class GameEngine
{
    // verify if a a space can be taken at (xcoord, ycoord), and take the space for the current player's turn if it is available.  return weather or not a space has been taken or not

    public  static boolean takeSpace(GameState state, Move move){ return takeSpace(state, move.getRow(), move.getCol());}

    public static boolean takeSpace(GameState state, int xcoord, int ycoord) {
        assert state.getBoardSpaces().get(xcoord, ycoord) == ' ';

        if(state.getBoardSpaces().get(xcoord, ycoord) == ' ') {
            state.getBoardSpaces().set(state.getCurrentTurn(), xcoord, ycoord); // if this board space is empty then take the space
            return true; //ret = true;
        } ///** DELETED
         else {
            return false; // else return that a space has not been taken
        }//*/
        //state.nextTurn();/** added */
        //return ret;
    }

    // take a random space for the current player's turn
    public static void takeRandomSpace(GameState state) {
        Random rand = new Random();
        int x, y;

        do {
            x = rand.nextInt(3); // generate a random number from [0,3)
            y = rand.nextInt(3);
        }
        while(state.getBoardSpaces().get(x, y) != ' '); // continue checking random spaces until an empty space is found

        takeSpace(state, x, y);
    }

    // calculate and take the best space for the current player's turn
    public static void takeBestSpace(GameState state) {
        ScoredMove bestMove = minmax(state);
        takeSpace(state, bestMove.x, bestMove.y);
    }

    private static ScoredMove minmax(GameState state)
    {
        // check if the minmax method has reached it's recursive endstate
        char winner = checkGameState(state);
        if( winner == 'X') { // x wins
            return new ScoredMove(10);
        } else if ( winner == 'O') { // o wins
            return new ScoredMove(-10);
        } else if ( winner == 'T') { // tie game
            return new ScoredMove(0);
        }

        Vector<ScoredMove> moves; // initilaize a list of all potential moves that can be made this turn
        moves = new Vector<ScoredMove>();

        // find and store all potential moves in moves vector
        for (int y = 0; y < 3; y++) {
            for ( int x = 0; x < 3; x++) {
                if(state.getBoardSpaces().get(x, y) == ' ') { // if this board space is empty then add it to the potential moves list
                    state.getBoardSpaces().set(state.getCurrentTurn(), x, y); // temporaraly set this space for evaluation
                    state.nextTurn();                                         // temporarly change the current turn so that it is the next player's turn

                    ScoredMove move = new ScoredMove(); // initialize a record of this potential move
                    move.x = x;
                    move.y = y;

                    // RECURSIVE CALL HERE: set the score of this potential move to the score of the best move the next player can make on his turn
                    move.score = minmax(state).score;

                    moves.add(move); // add this potential move to the list of all potential moves

                    state.getBoardSpaces().set(' ', x, y); // restore this space back to it's empty state
                    state.nextTurn();                         // restore the turn to the current player
                }
            }
        }

        // pick the best move out of all the available moves found
        int bestMove = 0;
        if(state.getCurrentTurn() == 'X') { // if calculating best move for X, look through all moves for the highest scoring move
            int bestScore = -1000000;
            for (int i = 0; i < moves.size(); i++) {
                if(moves.get(i).score > bestScore) {
                    bestMove = i;
                    bestScore = moves.get(i).score;
                }
            }
        } else { // else if calculating best move for O, look through all moves for the lowest scoring move
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

    // verify if the game is won by X, O, tied, or not over
    public static char checkGameState(GameState state) {
        for(int x = 0; x < 3; x++) { // check all horizontal groups
            if(state.boardSpaces.get(0, x) != ' ' && matching(state.boardSpaces.get(0, x), state.boardSpaces.get(1, x), state.boardSpaces.get(2, x))) { // if there is a horizontal match AND that match is not empty spaces (empty space = 0)
                return state.boardSpaces.get(0, x); // then return the winning player
            }
        }

        for(int y = 0; y < 3; y++) { // check all vertical groups
            if(state.boardSpaces.get(y, 0) != ' ' && matching(state.boardSpaces.get(y, 0), state.boardSpaces.get(y, 1), state.boardSpaces.get(y, 2))) { // if there is a vertical match AND that match is not empty spaces
                return state.boardSpaces.get(y, 0); // then return the winning player
            }
        }

        if(state.boardSpaces.get(0, 0) != ' ' && matching(state.boardSpaces.get(0, 0), state.boardSpaces.get(1, 1), state.boardSpaces.get(2, 2))) { // check both diagonal groups
            return state.boardSpaces.get(0, 0);
        }
        else if(state.boardSpaces.get(2, 0) != ' ' && matching(state.boardSpaces.get(2, 0), state.boardSpaces.get(1, 1), state.boardSpaces.get(0,2))) {
            return state.boardSpaces.get(2, 0);
        }
        else { // if no winner is found, then determine if there is still a playable/empty space
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 3; x++) {
                    if (state.boardSpaces.get(y, x) == ' ') { // if an empty space is found return empty token
                        return state.boardSpaces.get(y, x);   // returning the empty token to show that the game is not over
                    }
                }
            }
        }

        return 'T'; // if all other cases fail (no winner, and no empty spaces), then this game must be a draw
    }
    public static char checkGameStatus_or_lastMover(GameState state){

         char status = checkGameState(state);
         //System.out.println("\n\n" + status + "\n\n");
         if(status == ' ')
             status = Character.toLowerCase(state.currentTurn);
         return status;
    }


    // return true or false whether there is a winner for this game state
    public static boolean winFound(GameState state)
    {
        char winner = checkGameState(state);
        if(winner == 'X' || winner == 'O') {
            return true;
        } else {
            return false;
        }
    }

    private static boolean matching(int a, int b, int c) {
        return a == b && b == c;
    }
}
