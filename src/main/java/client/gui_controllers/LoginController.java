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
            Platform.runLater(()->{
                GameLobbyController.load(usernameField, usernameField.getText());  });
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
