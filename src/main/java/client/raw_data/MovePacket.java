package client.raw_data;

public class MovePacket {

    public Move move;
    public char status;

    /** status can take on any of these constant values */
    // game not over
    public static final char FROM_X ='x';   // incoming move was made by X player
    public static final char FROM_O = 'o';  // incoming move was made by O player

    // game is over
    public static final char X_WINS = 'X'; // X wins
    public static final char O_WINS = 'O'; // O wins
    public static final char DRAW = 'T';   // DRAW
    /*************************************/

    public MovePacket(Move move, char status ){
        this.move = move;
        this.status = status;
    }
    @Override public String toString(){
        return move.toString() + " " + "status: " + status;
    }

}
