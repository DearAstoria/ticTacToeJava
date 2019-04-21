package client.gui_controllers;

import client.User;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import server.Server;

import java.util.ArrayList;
import java.util.Arrays;

import static pubnubWrappers.PubNubWrappers.*;
import static sceneLoader.SceneLoader.loadFXML;
import static server.Server.LOGIN_CHANNEL;

public class LoginController extends pubnubWrappers.Subscriber {
    String email; // a name used for this online session
    String username;   // the username ID of the player
    String password;   // the password to the username ID
    //PubNub connection = new_PubNub();

    public LoginController() {
        super();
       // PubNub connection = new_PubNub();
        // add handler
        //connection.addListener(new Subcallback());
        //connection.subscribe().channels(Arrays.asList(connection.getConfiguration().getUuid())).execute();


    }

    @FXML public Button login;
    @FXML public Button signup;
    @FXML public TextField emailField;
    @FXML public TextField usernameField;
    @FXML public TextField passwordField;



    @FXML public void initialize(){
        init("loginScreen");
    }

    @Override
    public void handleSubCallBack(PubNub pubnub, PNMessageResult message) {


        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(GameLobbyController.class.getResource("../../gui_resources/GameLobby.fxml"));
                Parent root = (Parent)loader.load();
                GameLobbyController controller = loader.getController();
                ArrayList<String> lobby = new ArrayList<>();
                lobby.addAll(Arrays.asList(Server.LOBBY_CHANNEL, Server.LEAVE_LOBBY_CHANNEL));
                controller.init(usernameField.getText(),lobby);
                checkHereNow(Server.LOBBY_CHANNEL,controller.getHereNowCallBack());
                loadFXML(usernameField, root);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }
    public void loginClicked(MouseEvent click) throws java.io.IOException, java.sql.SQLException, ClassNotFoundException {
        /*Connection dbConn;

        // database parameter =‎‎ jdbc:sqlite: + filepath to database
        String url = "jdbc:sqlite:/Users/austinrosario/Desktop/Java/TicTacToe/TicTacToe/TicTacToe.db"; // NOTE: this is the file path to a database on my computer for testing purposes, it does not go to the final database we will use - Austin
        // create a connection to the database
        Class.forName("org.sqlite.JDBC");
        dbConn = DriverManager.getConnection(url);

        boolean loginSucsessful = false;
        Statement query = dbConn.createStatement(); // initialize a statement object to generate and execute sql queries on the database
        ResultSet loginResults = query.executeQuery("SELECT email, username, password FROM USERS"); // get a list of all users from USERS table

        while(loginResults.next() && !loginSucsessful) { // search through results of query for matching login entered by user
            if(loginResults.getString("email").equals(email)) {
                if(loginResults.getString("username").equals(username)) {
                    if(loginResults.getString("password").equals(password)) {
                        loginSucsessful = true;
                    }
                }
            }

        }*/

        //if(loginSucsessful)
       /* {
            System.out.println("login sucsessful");
            loginResults.close();
*/

        //System.out.println(connection.getConfiguration().getUuid() + '\n' + connection.getSubscribedChannels());





        publish(connection, new User("email", "username", "password"), LOGIN_CHANNEL);





        /*}
        else { System.out.println("login failed"); }

        loginResults.close();*/

    }







    public void signUpClicked(MouseEvent click) {

    }

    // get connection to the server
    private void getConnection() {

    }


}
