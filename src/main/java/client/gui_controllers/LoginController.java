package client.gui_controllers;

import client.User;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.presence.PNHereNowResult;
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

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.DriverManager;

public class LoginController extends pubnubWrappers.Subscriber {
    //PubNub connection = new_PubNub();

    public LoginController() throws java.sql.SQLException, ClassNotFoundException {
        super();

        /*
        // database parameter =‎‎ jdbc:sqlite: + filepath to database
        String url = "jdbc:sqlite:/Users/austinrosario/Desktop/Java/TicTacToe/TicTacToe/TicTacToe.db"; // NOTE: this is the file path to a database on my computer for testing purposes, it does not go to the final database we will use - Austin
        Class.forName("org.sqlite.JDBC");
        dbConn = DriverManager.getConnection(url); // create a connection to the database
        */

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
        init();
    }

    @Override
    public void handleSubCallBack(PubNub pubnub, PNMessageResult message) {

        connection.unsubscribeAll();
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(GameLobbyController.class.getResource("../../gui_resources/GameLobby.fxml"));
                Parent root = (Parent)loader.load();
                GameLobbyController controller = loader.getController();
                //controller.requestedOpponent.setText(usernameField.getText());
                controller.init(usernameField.getText(),new ArrayList<String>(Arrays.asList(Server.LOBBY_CHANNEL,Server.LEAVE_LOBBY_CHANNEL, Server.NEW_GAME_GRANTED)));
                /*ArrayList<String> lobby = new ArrayList<>();
                lobby.addAll(Arrays.asList(Server.LOBBY_CHANNEL, Server.LEAVE_LOBBY_CHANNEL));
                controller.init(usernameField.getText(),lobby);
                checkHereNow(Server.LOBBY_CHANNEL,controller.getHereNowCallBack());*/
                loadFXML(usernameField, root);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    // insert a new user into the database using data entered into the text fields
    private void insertUser() throws java.sql.SQLException, ClassNotFoundException {
        // reference the driver being used to connect to the database
        Class.forName("org.sqlite.JDBC");

        // connect to database
        Connection databaseConn = DriverManager.getConnection("jdbc:sqlite:/Users/austinrosario/Desktop/Java/TicTacToe/TicTacToe/TicTacToe.db");

        // create a statement
        Statement query = databaseConn.createStatement();

        // execute SQL insert
        query.executeUpdate("INSERT INTO USERS VALUES ('" + emailField.getText() + "', '" + usernameField.getText() + "', '" + passwordField.getText() + "')");

        query.close();
        databaseConn.close();
    }

    private boolean queryUser() throws java.sql.SQLException, ClassNotFoundException {
        // reference the driver being used to connect to the database
        Class.forName("org.sqlite.JDBC");

        // connect to database
        Connection databaseConn = DriverManager.getConnection("jdbc:sqlite:/Users/austinrosario/Desktop/Java/TicTacToe/TicTacToe/TicTacToe.db");

        // create a statement
        Statement query = databaseConn.createStatement();

        // execute SQL query
        ResultSet rs = query.executeQuery("SELECT email, username, password FROM USERS WHERE email = '" + emailField.getText() + "' AND username = '" + usernameField.getText() + "' AND password = '" + passwordField.getText() + "'");

        // if the query results in a ResultSet, then the user entered was found in the database
        if(rs.next()) {
            rs.close();
            query.close();
            databaseConn.close();
            return true;
        } else {
            rs.close();
            query.close();
            databaseConn.close();
            return false;
        }
    }

    public void loginClicked(MouseEvent click) throws java.io.IOException, java.sql.SQLException, ClassNotFoundException {
        boolean userFound = queryUser();
        if(userFound) {
            System.out.println("Login Successful");
            //System.out.println(connection.getConfiguration().getUuid() + '\n' + connection.getSubscribedChannels());
            publish(connection, new User("email", "username", "password"), LOGIN_CHANNEL);
        } else {
            System.out.println("Login Failed");
        }
    }

    public void signUpClicked(MouseEvent click) throws java.io.IOException, java.sql.SQLException, ClassNotFoundException {
        boolean userExists = queryUser();
        if(!userExists) { // if the user being signed up does not yet exist
            insertUser(); // add new user to database
            System.out.println("New user created");
            loginClicked(click); // log into the game server
        } else {
            System.out.println("That user already exists");
        }
    }



}
