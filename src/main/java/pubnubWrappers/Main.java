package pubnubWrappers;


import client.raw_data.GameSettings;
import com.google.gson.Gson;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import server.Server;

import java.util.Arrays;

import static pubnubWrappers.PubNubWrappers.new_PubNub;
import static pubnubWrappers.PubNubWrappers.publish;

public class Main {
    public static void main(String args[]) {

        new Thread(()->{

            String users[] = {"yoBro", "hoodlum", "charmeleon"};
            for(String i : users)
                PubNubWrappers.sub(String.valueOf(i),Arrays.asList(Server.LOBBY_CHANNEL));
            while(true){
                try {Thread.sleep(2222);}
                catch(Exception e){e.printStackTrace();}
            }
        }).run();




    }

    public void example1() {

    // pubnub object with UUID of "Austin"
    PubNub austin = PubNubWrappers.new_PubNub("Austin");
    // how austin will handleSubCallBack incoming messages
    austin.addListener(new

    SubscribeCallback() {
        @Override
        public void status (PubNub pubnub, PNStatus status){

        }

        @Override
        public void message (PubNub pubnub, PNMessageResult message){
            System.out.println("from " + message.getPublisher() + " on " + message.getChannel());
            client.raw_data.GameSettings gamesettings = new Gson().fromJson(message.getMessage(), client.raw_data.GameSettings.class);
            System.out.println(gamesettings);
            publish(pubnub, gamesettings, message.getPublisher());
        }

        @Override
        public void presence (PubNub pubnub, PNPresenceEventResult presence){

        }
    });

    // create a PubNub with UUID of Justis, from which to publish to "AustinsChannel"
    PubNub justis = new_PubNub("Justis");
    // add handler for justis as a subscriber
        justis.addListener(new

    SubscribeCallback() {
        @Override
        public void status (PubNub pubnub, PNStatus status){

        }

        @Override
        public void message (PubNub pubnub, PNMessageResult message){
            System.out.println("from " + message.getPublisher() + " on " + message.getChannel());
            System.out.println(message.getMessage());


        }

        @Override
        public void presence (PubNub pubnub, PNPresenceEventResult presence){

        }
    });


    // subscribe to "AustinsChannel"
        austin.subscribe().

    channels(Arrays.asList("AustinsChannel")).

    execute();

    // justis is subscribing to a channel named "Justis" which is also the UUID for justis
        justis.subscribe().

    channels(Arrays.asList(justis.getConfiguration().

    getUuid())).

    execute();


    publish(justis, new client.raw_data.GameSettings("player1",true,true,true),
                "AustinsChannel");


    }
}
