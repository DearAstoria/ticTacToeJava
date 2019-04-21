package pubnubWrappers;

/**
 * wrapper functions for PubNub creation and publish/subscribe/hereNow operations
 */



import com.pubnub.api.PubNub;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.presence.PNHereNowChannelData;
import com.pubnub.api.models.consumer.presence.PNHereNowOccupantData;
import com.pubnub.api.models.consumer.presence.PNHereNowResult;

import java.util.Arrays;
import java.util.List;


public class PubNubWrappers {

    //publish/subscribe keys are provided when you sign up for pubnub at pubnub.com
    private static final String pubKey = "pub-c-d24d4938-2288-4171-88f5-a5a2c9d5ce06";
    private static final String subKey = "sub-c-668aab96-56a4-11e9-ba0e-562f9424774e";

    public static PubNub new_PubNub() {    // returns a new PubNub
        PNConfiguration config = new PNConfiguration();
        config.setPublishKey(pubKey);
        config.setSubscribeKey(subKey);
        config.setSecure(true);
        return new PubNub(config);
    }

    public static PubNub new_PubNub(String uuid) // for creating a PubNub with a more personalized UUID
    {
        PubNub p = new_PubNub();
        p.getConfiguration().setUuid(uuid);
        return p;
    }

    public static PubNub publish(Object message, String chan) { // creates a PubNub, publishes from it, returns the new PubNub
        return publish(new_PubNub(), message, chan);
    }

    public static PubNub publish(PubNub pubnubObject, Object object, String chan){
        pubnubObject.publish().message(object).channel(chan).async(new PNCallback<PNPublishResult>() {
            @Override
            public void onResponse(PNPublishResult result, PNStatus status) {
            }
        });
        return pubnubObject;
    }

    public static PubNub publish(String sender, Object object, String chan){
        return publish(new_PubNub(sender), object, chan);
    }


    //
    public static void sendAndRecieve(PubNub pubnub, Object message, String correspondent, SubscribeCallback subCallback){
        publish(pubnub, message, correspondent);
        pubnub.addListener(subCallback);
    }

    public static void checkHereNow(String chan, PNCallback<PNHereNowResult> result)
    {
        new_PubNub().hereNow()
                .channels(Arrays.asList(chan)) // who is present on those channels?
                .includeState(true) // include state with request (false by default)
                .includeUUIDs(true) // if false, only shows occupancy count
                .async(result);/*new PNCallback<PNHereNowResult>() {
                    @Override
                    public void onResponse(PNHereNowResult result, PNStatus status) {

                        if (!status.isError()) {
                            for (PNHereNowChannelData channelData : result.getChannels().values()) {
                                channelData.getOccupancy(); // 3
                                channelData.getChannelName(); // my_channel
                                channelData.getOccupants(); // members of a channel
                                for (PNHereNowOccupantData occupant : channelData.getOccupants()) {
                                    String s = occupant;
                                    System.out.println(occupant.getUuid()); // some_uuid;
                                    //System.out.println(occupant.getState()); // channel member state, if applicable
                                }
                            }
                        } else {
                            // an error occurred
                            status.getErrorData().getInformation();
                            status.getErrorData().getThrowable().printStackTrace();
                        }
                    }
                });*/
    }

    public static PubNub sendReceive(String uuid, Object message, Subscriber parentObj, String channel){
        PubNub pubnub = new_PubNub(uuid);
        //pubnub.addListener(new Subcallback(parentObj));
        pubnub.subscribe().channels(Arrays.asList(channel)).execute();

        return null;
    }

    public static PubNub sub(String uuid, List chan){
        PubNub p = new_PubNub(uuid);
        return sub(p,chan);
    }
    public static PubNub sub(PubNub p, List chan){
        p.subscribe().channels(chan).execute();
        return p;
    }
}