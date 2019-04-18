package pubnubWrappers;

/**
 * wrapper functions for PubNub creation and publish/subscribe/hereNow operations
 */



import com.pubnub.api.PubNub;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.presence.PNHereNowChannelData;
import com.pubnub.api.models.consumer.presence.PNHereNowOccupantData;
import com.pubnub.api.models.consumer.presence.PNHereNowResult;

import java.util.Arrays;


public class PubNubWrappers {

    //publish/subscribe keys are provided when you sign up for pubnub at pubnub.com
    static private final String pubKey = "pub-c-d24d4938-2288-4171-88f5-a5a2c9d5ce06";
    static private final String subKey = "sub-c-668aab96-56a4-11e9-ba0e-562f9424774e";

    static PubNub new_PubNub() {    // returns a new PubNub
        PNConfiguration config = new PNConfiguration();
        config.setPublishKey(pubKey);
        config.setSubscribeKey(subKey);
        config.setSecure(true);
        return new PubNub(config);
    }

    static PubNub new_PubNub(String uuid) // for creating a PubNub with a more personalized UUID
    {
        PubNub p = new_PubNub();
        p.getConfiguration().setUuid(uuid);
        return p;
    }

    static PubNub publish(Object message, String chan) { // creates a PubNub, publishes from it, returns the new PubNub
        return publish(new_PubNub(), message, chan);
    }

    static PubNub publish(PubNub pubnubObject, Object object, String chan){
        pubnubObject.publish().message(object).channel(chan).async(new PNCallback<PNPublishResult>() {
            @Override
            public void onResponse(PNPublishResult result, PNStatus status) {
            }
        });
        return pubnubObject;
    }

    static PubNub publish(String sender, Object object, String chan){
        return publish(new_PubNub(sender), object, chan);
    }


    static void hereNow1(String chan)
    {
        new_PubNub().hereNow()
                .channels(Arrays.asList(chan)) // who is present on those channels?
                .includeState(true) // include state with request (false by default)
                .includeUUIDs(true) // if false, only shows occupancy count
                .async(new PNCallback<PNHereNowResult>() {
                    @Override
                    public void onResponse(PNHereNowResult result, PNStatus status) {

                        if (!status.isError()) {
                            for (PNHereNowChannelData channelData : result.getChannels().values()) {
                                channelData.getOccupancy(); // 3
                                channelData.getChannelName(); // my_channel
                                channelData.getOccupants(); // members of a channel
                                for (PNHereNowOccupantData occupant : channelData.getOccupants()) {
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
                });
    }
}