package pubnubWrappers.UseCases;

import com.google.gson.Gson;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import pubnubWrappers.PubNubWrappers;
import server.Server;

import java.util.Arrays;

import static pubnubWrappers.PubNubWrappers.new_PubNub;
import static pubnubWrappers.PubNubWrappers.publish;

public class LeaveLobby {

    public static void main(String args[]) {

        new Thread(()->{

            String users[] = {"hoodlum", "charmeleon"};
            for(String i : users)
                PubNubWrappers.publish(new_PubNub(i),"leave",Server.LEAVE_LOBBY_CHANNEL);

        }).run();




    }

}
