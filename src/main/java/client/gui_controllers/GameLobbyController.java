package client.gui_controllers;

import client.raw_data.GameState;
import client.raw_data.GameSettings;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.presence.PNHereNowChannelData;
import com.pubnub.api.models.consumer.presence.PNHereNowOccupantData;
import com.pubnub.api.models.consumer.presence.PNHereNowResult;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pubnubWrappers.*;
import server.Server;

import java.net.URL;
import java.util.ResourceBundle;

import static pubnubWrappers.PubNubWrappers.checkHereNow;


public class GameLobbyController extends Subscriber

{



    @FXML public VBox playerList;


    @FXML public void initialize(){ }



    @Override
    public void handleHereNow(PNHereNowResult result, PNStatus status){
            hereNow = result;
        Platform.runLater(()->{
            for(PNHereNowChannelData channelData : hereNow.getChannels().values()){
                channelData.getOccupants();
                for (PNHereNowOccupantData occupant : channelData.getOccupants()) {
                    playerList.getChildren().add(new Button(occupant.getUuid()));
                }
            }

    });


    }

    @Override
    public void handleSubCallBack(PubNub pubnub, PNMessageResult message){
                String chan = message.getChannel();
                if(chan.equals(Server.LOBBY_CHANNEL))
                    addPlayer(message.getPublisher());
                else if(chan.equals(Server.LEAVE_LOBBY_CHANNEL))
                    removePlayer(message.getPublisher());

                    //push to lobby, then get lobby subscribers, then subscribe to lobby
    }

    private void removePlayer(String uuid_username){

        // search for user and delete associated button
    }
    private void addPlayer(String username){
        playerList.getChildren().add(new Button(username));
    }

    public void joinGame(MouseEvent click) throws java.io.IOException {
        GameController controller = new GameController();

        switch(((Button)click.getSource()).getText()) {
            case "CPU (Easy)":
                controller.initData(new GameState(), new GameSettings("player 1", true, true, true), false);
                break;
            case "CPU (Hard)":
                controller.initData(new GameState(), new GameSettings("player 1", true, false, true), false);
                break;
            default:
                break;
        }
try{
        // load the next scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../gui_resources/GameScreen.fxml")); // initialize FXMLLoader to load the next UI
        loader.setController(controller); // initialize the FXMLLoader to tell the UI to use the controller initialized from parameters in this object
        Scene GameScreenScene = new Scene(loader.load());

        // get reference to the current stage
        Stage window = (Stage)((Node)click.getSource()).getScene().getWindow();

        // set current stage to display the next scene
        window.setScene(GameScreenScene);

        window.show();}
        catch(Exception e){System.out.println(e.getCause());}
    }

    public void logout(MouseEvent click) throws java.io.IOException {
        // load the next scene
        Parent GameScreenParent = FXMLLoader.load(getClass().getResource("../../gui_resources/Login.fxml"));
        Scene GameScreenScene = new Scene(GameScreenParent);

        // get the stage... getSource: get object that was clicked on (the button) from the event, getScene: get the scene the button is a part of, getWindow: get the stage the scene is a part of
        Stage window = (Stage)((Node)click.getSource()).getScene().getWindow();

        // set stage to display the next scene
        window.setScene(GameScreenScene);

        window.show();
    }


}
