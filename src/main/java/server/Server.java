package server;

import client.User;
import client.gui_controllers.GameLobbyController;
import com.google.gson.Gson;
import com.pubnub.api.PubNub;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import pubnubWrappers.Subscriber;
import server.databaseOperations.PostgresqlExample;

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
    public static final String GAME_REQUEST_CHANNEL = "game_request";
    public static final String NEW_GAME_GRANTED = "game_granted";
    public static final String REQUEST_CPU = "request_cpu";
    public static final String CPU_GRANTED = "cpu_granted";
    public static final String EASY = "easy";







    public Server()
    {
        super(Arrays.asList(NEW_ACCOUNT_CHANNEL,LOGIN_CHANNEL,JOIN_LOBBY_CHANNEL, GAME_REQUEST_CHANNEL, REQUEST_CPU));
    }


    @Override
    public void handleSubCallBack(PubNub pubnub, PNMessageResult message){
        String channel = message.getChannel();
        try {
        if(channel.equals(NEW_ACCOUNT_CHANNEL))
            addAccount(message);
        else if(channel.equals(LOGIN_CHANNEL))
            login(message);
        else if(channel.equals(GAME_REQUEST_CHANNEL))
            requestGame(message);
        else if(channel.equals(LEAVE_LOBBY_CHANNEL))
            leaveLobby(message);
        else if(channel.equals(REQUEST_CPU))
            requestCPU(message); }
        catch (Exception e) {e.printStackTrace();}
    }

    void addAccount(PNMessageResult message) throws Exception{

        User user = new Gson().fromJson(message.getMessage(), User.class);
/*
        boolean emailExists, nameExists;
        boolean userExists = PostgresqlExample.queryUser( user.getEmail(), user.getUsername());
        if(!userExists) { // if the user being signed up does not yet exist
            PostgresqlExample.insertUser(user.getEmail(), user.getUsername(), user.getPassword()); // add new user to database
            publish(connection,"success",message.getPublisher());//System.out.println("New user created");
            //loginClicked(click); // log into the game server
        } else {*/
            publish(connection,"user already exists",message.getPublisher());;

        /*emailExists = userExists(user.getEmail());
        nameExists = userExists(user.getUsername());
        if( emailExists || nameExists )
            {
                if(emailExists)
                    System.out.println("email already used");
                if(nameExists)
                    System.out.println("username already used");
            }
        else { try{ executeQuery("signup successful"); }
            catch (Exception e){e.printStackTrace();}*/
       // }



    }

    boolean userExists(String identifier){
        return false;//!executeQuery("SELECT username FROM USERS WHERE username=" + identifier).next();
    }

    void login(PNMessageResult message){  // verify login information
        {
            User user = new Gson().fromJson(message.getMessage(),User.class);
            user.getUsername();
            user.getEmail();


            System.out.println(user);
            publish(user,message.getPublisher());
        }

    }




    void requestGame(PNMessageResult msg)
    {
            GameHandler handler = new GameHandler(msg.getPublisher(), msg.getMessage().toString().replace("\"","")); // (x,o)
            publish(handler.getConnection(), handler.getxPlayer(), Server.NEW_GAME_GRANTED);
    }

    void requestCPU(PNMessageResult msg){
        GameHandler handler;
        if(msg.getMessage().toString().replace("\"","").equals(EASY))
            handler = new GameHandler(msg.getPublisher(),true);
        else
            handler = new GameHandler(msg.getPublisher(),false);
        publish(handler.getConnection(), handler.getxPlayer(), CPU_GRANTED);

    }


    void leaveLobby(PNMessageResult msg)
    {
        System.out.println(msg.getPublisher() + msg.getMessage().toString());

    }


    public static void main(String args[]){
        Server server = new Server();
        //server.startReceivingMessage();
        //server.createDatabase();

    }
}


