package server;

import client.User;
import com.google.gson.Gson;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import pubnubWrappers.Subscriber;

import java.util.Arrays;

import static pubnubWrappers.PubNubWrappers.publish;
import static server.databaseOperations.PostgresqlExample.executeQuery;
import static pubnubWrappers.PubNubWrappers.new_PubNub;

public class Server extends Subscriber {
    public static final String NEW_ACCOUNT_CHANNEL = "new_account";
    public static final String LOGIN_CHANNEL = "login";
    public static final String LOBBY_CHANNEL = "lobby";
    public static final String JOIN_LOBBY_CHANNEL = "join_lobby";
    public static final String LEAVE_LOBBY_CHANNEL = "leave_lobby";







    public Server()
    {
        super(Arrays.asList(NEW_ACCOUNT_CHANNEL,LOGIN_CHANNEL,LOBBY_CHANNEL,JOIN_LOBBY_CHANNEL,LEAVE_LOBBY_CHANNEL));
    }


    @Override
    public void handleSubCallBack(PubNub pubnub, PNMessageResult message){
        String channel = message.getChannel();
        if(channel.equals(NEW_ACCOUNT_CHANNEL))
            addAccount(message);
        else if(channel.equals(LOGIN_CHANNEL))
            login(message);
        else if(channel.equals(LOBBY_CHANNEL))
            requestGame(message);
        else if(channel.equals(LEAVE_LOBBY_CHANNEL))
            leaveLobby(message);
    }

    void addAccount(PNMessageResult message){

        User user = new Gson().fromJson(message.getMessage(), User.class);

        boolean emailExists, nameExists;

        emailExists = userExists(user.getEmail());
        nameExists = userExists(user.getUsername());
        if( emailExists || nameExists )
            {
                if(emailExists)
                    System.out.println("email already used");
                if(nameExists)
                    System.out.println("username already used");
            }
        else
            executeQuery("signup successful");



    }

    boolean userExists(String identifier){
        return false;//!executeQuery("SELECT username FROM USERS WHERE username=" + identifier).next();
    }

    void login(PNMessageResult message){  // verify login information
        {
            User user = new Gson().fromJson(message.getMessage(),User.class);
            System.out.println(user);
            publish(user,message.getPublisher());
        }

    }




    void requestGame(PNMessageResult msg)
    {



    }

    void leaveLobby(PNMessageResult msg)
    {


    }


    public static void main(String args[]){
        Server server = new Server();
        //server.startReceivingMessage();
        //server.createDatabase();

    }
}


