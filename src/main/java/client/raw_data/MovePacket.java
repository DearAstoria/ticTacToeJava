package client.raw_data;

public class MovePacket {

    public Move move;
    public  char status; /**  ''  =  game not over
                      'T' =  draw
                      'x' = x wins
                      'o' = o wins   */
    public MovePacket(Move move, char status ){
        this.move = move;
        this.status = status;
    }

}
