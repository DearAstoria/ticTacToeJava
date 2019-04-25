package client.gui_controllers;

import client.User;
import com.pubnub.api.PubNub;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import server.Server;
import server.databaseOperations.PostgresqlExample;

import java.util.ArrayList;
import java.util.Arrays;

import static pubnubWrappers.PubNubWrappers.*;
import static sceneLoader.SceneLoader.loadFXML;
import static server.Server.LOGIN_CHANNEL;

import java.sql.ResultSet;

public class LoginController extends pubnubWrappers.Subscriber {
    //PubNub connection = new_PubNub();

    public LoginController() throws java.sql.SQLException, ClassNotFoundException {
        super();

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
        String msg = message.getMessage().toString().replace("\"","");


         if(msg.equals("success")) {
            connection.unsubscribeAll();
            Platform.runLater(() -> {
                try {
                    FXMLLoader loader = new FXMLLoader(GameLobbyController.class.getResource("../../gui_resources/GameLobby.fxml"));
                    Parent root = (Parent) loader.load();
                    GameLobbyController controller = loader.getController();
                    //controller.requestedOpponent.setText(usernameField.getText());
                    controller.init(usernameField.getText(), new ArrayList<String>(Arrays.asList(Server.LOBBY_CHANNEL, Server.LEAVE_LOBBY_CHANNEL, Server.NEW_GAME_GRANTED, Server.CPU_GRANTED)));
                    publish(controller.getConnection(), "", Server.LOBBY_CHANNEL);
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
        else
            System.out.println(msg + " used/incorrect");
    }




    public void loginClicked(MouseEvent click) throws Exception {
        publish(connection, new User(emailField.getText(), usernameField.getText(), passwordField.getText()), LOGIN_CHANNEL);

    }

    public void signUpClicked(MouseEvent click) throws java.io.IOException, java.sql.SQLException, ClassNotFoundException {
        publish(connection, new User(emailField.getText(), usernameField.getText(), passwordField.getText()), Server.NEW_ACCOUNT_CHANNEL);

    }



}
