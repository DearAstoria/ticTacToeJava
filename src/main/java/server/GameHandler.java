package server;

import client.raw_data.*;
import com.google.gson.Gson;
import com.pubnub.api.PubNub;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import pubnubWrappers.Subscriber;

import static client.raw_data.GameEngine.*;
import static java.lang.Character.isUpperCase;
import static pubnubWrappers.PubNubWrappers.publish;

public class GameHandler extends Subscriber {

    GameState game = new GameState();  // new GameState, where currentPlayer is X
    ScoredMove scoredMove;
    private String xPlayer;
    private String oPlayer;
    private boolean twoPlayerMode = true;
    private boolean easy;
    int i=0;

    public GameHandler(String x, String o) {  // two players
        init();
        this.xPlayer = x;
        this.oPlayer = o;
    }
    public GameHandler(String singlePlayer, boolean easy){  // one player
        init();
        xPlayer = singlePlayer;
        this.easy = easy;
        twoPlayerMode = false;
    }

    public String getxPlayer() {
        return xPlayer;
    }

    public String getoPlayer() {
        return oPlayer;
    }


    @Override
    public void handleSubCallBack(PubNub pubnub, PNMessageResult message) {

            Move move = new Gson().fromJson(message.getMessage(), Move.class);//computeMove(new Gson().fromJson(message.getMessage(), Move.class));
            if(twoPlayerMode)
                computeMove(move, message.getPublisher());
            else
                cpuMode(move);
        System.out.println("check game state:   " + checkGameState(game));
        System.out.println(game.getBoardSpaces());
            game.nextTurn();

    }


    public void cpuMode(Move playerMove){

        takeSpace(game, playerMove);
        Move cpuMove;
        if(isUpperCase(checkGameStatus_or_lastMover(game))) {
            publish(connection, new MovePacket(playerMove, checkGameStatus_or_lastMover(game)), xPlayer);
            return; }

        game.nextTurn();
        if(easy)
            cpuMove = takeRandomSpace(game);
        else
            cpuMove = takeBestSpace(game);

        publish(connection, new MovePacket(cpuMove, checkGameStatus_or_lastMover(game)) , xPlayer);
    }

    public void computeMove(Move move, String sender){

        takeSpace(game, move);
        MovePacket p = new MovePacket(move, checkGameStatus_or_lastMover(game));
        if(isUpperCase(p.status)) {         // send GameOver status
            publish(connection, p, xPlayer);
            publish(connection, p, oPlayer);
        }
        else if(sender.equals(xPlayer) && twoPlayerMode)             // always false in CPU mode
            publish(connection, p, oPlayer);
        else
            publish(connection, p, xPlayer);

    }



    @Override public String toString(){
        return new String("gameID " + getUUID() + " " + xPlayer + " " + oPlayer);
    }


}
