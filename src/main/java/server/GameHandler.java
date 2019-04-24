package server;

import client.raw_data.GameState;
import client.raw_data.Move;
import client.raw_data.MovePacket;
import com.google.gson.Gson;
import com.pubnub.api.PubNub;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import pubnubWrappers.Subscriber;

import static client.raw_data.GameEngine.*;
import static java.lang.Character.isUpperCase;
import static pubnubWrappers.PubNubWrappers.publish;

public class GameHandler extends Subscriber {

    GameState game = new GameState();  // new GameState, where currentPlayer is X
    private String xPlayer;
    private String oPlayer;

    public GameHandler(String x, String o) {
        init();
        this.xPlayer = x;
        this.oPlayer = o;
    }

    public String getxPlayer() {
        return xPlayer;
    }

    public String getoPlayer() {
        return oPlayer;
    }


    @Override
    public void handleSubCallBack(PubNub pubnub, PNMessageResult message) {

            String sender = message.getPublisher();
            MovePacket move_n_status = computeMove(new Gson().fromJson(message.getMessage(), Move.class));



            if(isUpperCase(move_n_status.status)) {         // send GameOver status to both players
                publish(connection, move_n_status, oPlayer);
                publish(connection, move_n_status, xPlayer);
            }

            else if(sender.equals(xPlayer))
                publish(connection, move_n_status, oPlayer);
            else
                publish(connection, move_n_status, xPlayer);
            game.nextTurn();

        System.out.print("\n" + move_n_status + "\n");
        System.out.println(game.getBoardSpaces());
    }
    public MovePacket computeMove(Move move){


        assert takeSpace(game, move);

        System.out.println("check game state:   " + checkGameState(game));

        return  new MovePacket(move, checkGameStatus_or_lastMover(game));

    }

    @Override public String toString(){
        return new String("gameID " + getUUID() + " " + xPlayer + " " + oPlayer);
    }


}
