package server;

import client.User;
import com.google.gson.Gson;
import com.pubnub.api.PubNub;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import pubnub_things.Subscriber;
import server.databaseOperations.DBOperations;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;

import static pubnub_things.PubNubWrappers.publish;
import static pubnub_things.PubNubWrappers.new_PubNub;

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
    public static final String GET_LEADER_BOARD = "get_update_leader";
    public static final String REQUEST_LEADER_BOARD = "request_update_leader";
    public static final String EASY = "easy";



    public Server()
    {
        super(Arrays.asList(NEW_ACCOUNT_CHANNEL,LOGIN_CHANNEL,JOIN_LOBBY_CHANNEL, GAME_REQUEST_CHANNEL, REQUEST_CPU, GET_LEADER_BOARD, REQUEST_LEADER_BOARD));
    }


    @Override
    public void handleSubCallBack(PubNub pubnub, PNMessageResult message){
        String channel = message.getChannel();
        try {
            if (channel.equals(NEW_ACCOUNT_CHANNEL))
                addAccount(message);
            else if (channel.equals(LOGIN_CHANNEL))
                login(message);
            else if (channel.equals(GAME_REQUEST_CHANNEL))
                requestGame(message);
            else if (channel.equals(LEAVE_LOBBY_CHANNEL))
                leaveLobby(message);
            else if (channel.equals(REQUEST_CPU))
                requestCPU(message);
            else if (channel.equals(REQUEST_LEADER_BOARD))
                    leaderBoard(message);
        }
        catch (Exception e) {e.printStackTrace();}
    }

    void addAccount(PNMessageResult message) throws Exception{

        User user = new Gson().fromJson(message.getMessage(), User.class);

        if(DBOperations.signUpQuery( "email = '" + user.getEmail() + "'" ) ) { // if the user being signed up does not yet exist
            if (DBOperations.signUpQuery("username = '" + user.getUsername() + "'"))
                publish(connection, "email and username already used", message.getPublisher());
            else
                publish(connection, "email already used", message.getPublisher());
            return;
        }
        else{
            DBOperations.insertUser(user.getEmail(), user.getUsername(), user.getPassword());
            publish(connection,"success",message.getPublisher()); }
    }


    void login(PNMessageResult message) {  // verify login information
        {
            User user = new Gson().fromJson(message.getMessage(),User.class);

            if(DBOperations.loginQuery("SELECT email, username, password FROM USERS WHERE email = '"
                                        + user.getEmail() + "' AND username = '" + user.getUsername()
                                         + "' AND password = '" + user.getPassword() + "'"))
                publish(connection, "success" ,message.getPublisher());
            else
                publish(connection,"login fail", message.getPublisher());
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

    private void leaderBoard(PNMessageResult msg) throws Exception
    {
        if(msg.getPublisher().equals(getUUID()))
            return;
        else {


            String ar[] = new String[1000];
            int i = 0;
            StringBuilder b = new StringBuilder();

            Class.forName(DBOperations.driver);


            Connection databaseConn = DriverManager.getConnection(DBOperations.tictactoe, DBOperations.USER, DBOperations.PASS);

            // create a statement
            Statement query = databaseConn.createStatement();

            // execute SQL insert
            ResultSet rs = query.executeQuery("SELECT username, wins FROM USERS" +
                    " ORDER BY WINS DESC");
            while (rs.next()) {

                b = new StringBuilder();
                b.append(rs.getString("username")).append("       ").append(String.valueOf(rs.getInt("wins")));
                ar[i] = new String(b);
                System.out.println(ar[i++]);
            }

            query.close();
            databaseConn.close();
            publish(connection, Arrays.copyOf(ar,i), GET_LEADER_BOARD);

        }
    }

    public static void main(String args[]){
        Server server = new Server();
    }
}


