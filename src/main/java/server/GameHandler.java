package server;

import client.raw_data.GameState;
import client.raw_data.Move;
import client.raw_data.MovePacket;
import com.google.gson.Gson;
import com.pubnub.api.PubNub;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import pubnubWrappers.Subscriber;

import java.util.Arrays;

import static client.raw_data.GameEngine.checkGameState;
import static client.raw_data.GameEngine.takeSpace;
import static pubnubWrappers.PubNubWrappers.publish;

public class GameHandler extends Subscriber {

    GameState game = new GameState();
    private String x;
    private String o;

    public GameHandler(String x, String o) {
        this.x = x;
        this.o = o;
    }

    public String getX() {
        return x;
    }

    public String getO() {
        return o;
    }

    @Override
    public void handleSubCallBack(PubNub pubnub, PNMessageResult message) {
            String sender = message.getPublisher();
            MovePacket move_n_status = new Gson().fromJson(message.getMessage(), MovePacket.class);
            move_n_status = computeMove(move_n_status);

            if(sender.equals(x))
                publish(connection, move_n_status, o);
            else
                publish(connection, move_n_status, x);

    }
    public MovePacket computeMove(MovePacket move_n_status){

        assert takeSpace(game, move_n_status.move) == true;
        return  new MovePacket(move_n_status.move, checkGameState(game));

    }



}
