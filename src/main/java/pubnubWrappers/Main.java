package pubnubWrappers;


import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import java.util.Arrays;

import static pubnubWrappers.PubNubWrappers.new_PubNub;

public class Main {
    public static void main(String args[]){

    // pubnub object with UUID of "Austin"
    PubNub austin = PubNubWrappers.new_PubNub("Austin");
    // how austin will handle incoming messages
    austin.addListener(new SubscribeCallback() {
        @Override
        public void status(PubNub pubnub, PNStatus status) {

        }

        @Override
        public void message(PubNub pubnub, PNMessageResult message) {

        }

        @Override
        public void presence(PubNub pubnub, PNPresenceEventResult presence) {

        }
    });

    // subscribe to "AustinsChannel"
    austin.subscribe().channels(Arrays.asList("AustinsChannel")).execute();



    // create a PubNub with UUID of Justis, from which to publish to "AustinsChannel"
    PubNub justis = PubNubWrappers.publish("Justis",
            new client.raw_data.GameSettings("player1",true,true,true), "AustinsChannel");

        justis.addListener(new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {

            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {

            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {

            }
        });










    }
}
