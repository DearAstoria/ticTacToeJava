package pubnub_things;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.presence.PNHereNowResult;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import java.util.*;

import static pubnub_things.PubNubWrappers.new_PubNub;

    abstract public class Subscriber extends SubscribeCallback
    {

        protected PubNub connection = new_PubNub();
        protected PNHereNowResult hereNow;
        protected HereNowCallback hereNowCallback = new HereNowCallback();

        public void init(String uuid, ArrayList<String> channels){
            connection = new_PubNub(uuid);
            connection.addListener(this);
            channels.add(uuid);
            subscribe(channels);
        }
        public void init(String uuid){
            init(uuid, new ArrayList<String>());
            /*connection = new_PubNub(uuid);
            connection.addListener(this);
            subscribe();*/
        }
        public void init(){
            connection.addListener(this);
            subscribe();
        }
        public Subscriber() {
            /*connection.addListener(this);
            subscribe();*/
            //addSubsribtion(Arrays.asList(connection.getConfiguration().getUuid()));
        }
        public Subscriber(List<String> channels){

            connection.addListener(this);
            subscribe(channels);
            //addSubsribtion(channels);

        }
        public Subscriber(String uuid, List<String> channels){
            setUUID(uuid);
            channels.add(uuid);
            addSubsribtion(channels);
        }

        protected void getSubscribers(List<String> chan) {
            connection.hereNow()
                    .channels(chan) // who is present on those channels?
                    .includeState(true) // include state with request (false by default)
                    .includeUUIDs(true) // if false, only shows occupancy count
                    .async(hereNowCallback);
        }
        protected void getSubscribers(String chan){getSubscribers(Arrays.asList(chan));}

        //public HereNowCallback getHereNowCallBack(){ return hereNowCallback;}

        public void addSubsribtion(List<String> channels){

            connection.getSubscribedChannels().addAll(channels);
            //connection.
        }

        public void setUUID(String uuid) {
            connection.getConfiguration().setUuid(uuid);
            subscribe();//addSubsribtion(Arrays.asList(connection.getConfiguration().getUuid()));
        }

        public void subscribe(List<String> channels){ connection.subscribe().channels(channels).execute();  }
        public void subscribe() { subscribe(Arrays.asList(getUUID())); }


        abstract public void handleSubCallBack(PubNub pubnub, PNMessageResult message);
        public void handleHereNow(PNHereNowResult result, PNStatus status){}

        public PubNub getConnection(){return connection;}

        public String getUUID(){return connection.getConfiguration().getUuid();}

        @Override
        public void status (PubNub pubnub, PNStatus status){
                System.out.println("UUID: " + pubnub.getConfiguration().getUuid()
                + '\n' + "sub to: " + pubnub.getSubscribedChannels());// + '\n' + "connection status: " + status.getCategory());


        }

        @Override
        public void message (PubNub pubnub, PNMessageResult message){
            System.out.println("message from:   " + message.getPublisher() + " on      [" + message.getChannel() + "]        " + message.getMessage());
            handleSubCallBack(pubnub,message);


        }

        @Override
        public void presence (PubNub pubnub, PNPresenceEventResult presence){

        }

        class HereNowCallback extends PNCallback<PNHereNowResult> {

        @Override
        public void onResponse(PNHereNowResult result, PNStatus status){
            handleHereNow(result, status); }

        }





    }

//}
