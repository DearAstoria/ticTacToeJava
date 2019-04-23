package client.raw_data;

public class MovePacket {

    public Move move;
    public  final char status;
    static final char X_TURN ='x';
    static final char O_TURN = 'o';
    static final char X_WINS = 'X';
    static final char O_WINS = 'O';
    static final char DRAW = 'T';

    public MovePacket(Move move, char status ){
        this.move = move;
        this.status = status;
    }

}
