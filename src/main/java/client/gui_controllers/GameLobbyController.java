package client.gui_controllers;

import client.raw_data.GameState;
import client.raw_data.GameSettings;
import com.pubnub.api.PubNub;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.presence.PNHereNowChannelData;
import com.pubnub.api.models.consumer.presence.PNHereNowOccupantData;
import com.pubnub.api.models.consumer.presence.PNHereNowResult;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pubnubWrappers.*;
import server.Server;

import java.util.*;

import static pubnubWrappers.PubNubWrappers.publish;
import static sceneLoader.SceneLoader.loadFXML;


public class GameLobbyController extends Subscriber

{


    GameController gameController;
    String requestedOpponent;
    Parent root;

    @FXML public VBox playerList;
    //@FXML public Label requestedOpponent;

    @FXML public void initialize(){
        getSubscribers(Server.LOBBY_CHANNEL);
        FXMLLoader loader = new FXMLLoader(GameController.class.getResource("../../gui_resources/GameScreen.fxml"));
        try{ root = (Parent)loader.load();
            gameController = loader.getController();
        }
        catch (Exception e){e.printStackTrace();}

    }

    class playerSelected implements EventHandler<MouseEvent>{


        public playerSelected(String user){ requestedOpponent = user; }
        @Override public void handle(MouseEvent e){
                connection.unsubscribe().channels(Arrays.asList(Server.LOBBY_CHANNEL)).execute();
                publish(connection, "leaving lobby" , Server.LEAVE_LOBBY_CHANNEL);
                publish(connection, requestedOpponent, Server.GAME_REQUEST_CHANNEL);
        }

    }

    Map playerMap = new HashMap();

    @Override
    public void handleHereNow(PNHereNowResult result, PNStatus status){
            hereNow = result;
        Platform.runLater(()->{
            for(PNHereNowChannelData channelData : hereNow.getChannels().values()){
                channelData.getOccupants();
                for (PNHereNowOccupantData occupant : channelData.getOccupants()) {
                    if(!occupant.getUuid().isEmpty())
                        addPlayer(occupant.getUuid());
                }

            }

    });
    }

    @Override
    public void handleSubCallBack(PubNub pubnub, PNMessageResult message){
                String chan = message.getChannel();
                String msg = message.getMessage().toString().replace("\"","");
                if(chan.equals(Server.LOBBY_CHANNEL) && !(msg.equals(getUUID())) )
                    addPlayer(message.getPublisher());
                else if(chan.equals(Server.LEAVE_LOBBY_CHANNEL) && !(msg.equals(getUUID())) )
                    removePlayer(message.getPublisher());
                else if(chan.equals(Server.NEW_GAME_GRANTED)){   // I requested to play someone
                    if(msg.equals(getUUID())) {
                        publish(connection, message.getPublisher(), requestedOpponent);
                        gameController.setGameID(message.getPublisher());
                        gameController.initData(new GameState(), new GameSettings(getUUID(), true ,true));
                        gameController.setNames(getUUID(), requestedOpponent);
                        launchGame();
                    }

                }
                else if(chan.equals(getUUID())){   // someone requested to play me
                    gameController.setGameID(message.getMessage().toString());
                    gameController.initData(new GameState(), new GameSettings(getUUID(), false ,true));
                    gameController.setNames(getUUID(), message.getPublisher());
                    launchGame();
                }

                    //publish to lobby, then get lobby subscribers, then subscribe to lobby
    }

    private void launchGame() {
        Platform.runLater(()->{
            try {
                loadFXML(playerList, root);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    private void removePlayer(String uuid_username){


        Platform.runLater(()->{
            playerList.getChildren().remove(playerMap.get(uuid_username));
            playerMap.remove(uuid_username);
        });
        connection.unsubscribe().channels(Arrays.asList(Server.LOBBY_CHANNEL));

        // search for requestedOpponent and delete associated button
    }
    private void addPlayer(String username){

        Platform.runLater(()->{
            playerMap.put(username,new Button(username));
            playerList.getChildren().add((Node)playerMap.get(username));
            ((Button)playerMap.get(username)).setOnMouseClicked(new playerSelected(username));
        });
        //new Button().setO

    }

    public void joinGame(MouseEvent click) throws java.io.IOException {
        //GameController controller = new GameController();

        switch(((Button)click.getSource()).getText()) {
            case "CPU (Easy)":
                gameController.initData(new GameState(), new GameSettings("player 1", true, true, true), false);
                break;
            case "CPU (Hard)":
                gameController.initData(new GameState(), new GameSettings("player 1", true, false, true), false);
                break;
            default:
                break;
        }
//try{
        // load the next scene
        /*FXMLLoader loader = new FXMLLoader(getClass().getResource("../../gui_resources/GameScreen.fxml")); // initialize FXMLLoader to load the next UI
        loader.setController(controller); // initialize the FXMLLoader to tell the UI to use the controller initialized from parameters in this object
        Scene GameScreenScene = new Scene(loader.load());

        // get reference to the current stage
        Stage window = (Stage)((Node)click.getSource()).getScene().getWindow();

        // set current stage to display the next scene
        window.setScene(GameScreenScene);

        window.show();}*/ launchGame();
        //catch(Exception e){System.out.println(e.getCause()); e.printStackTrace();}
    }

    public void logout(MouseEvent click) throws java.io.IOException {
        connection.unsubscribeAll();
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
