package client.gui_controllers;

import client.raw_data.GameSettings;
import com.google.gson.Gson;
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
import javafx.scene.control.ScrollPane;
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
    Map playerMap = new HashMap();
    boolean needLeaderBoard = true;

    public static void load(Node node, String uuid) {
        try {
            FXMLLoader loader = new FXMLLoader(GameLobbyController.class.getResource("../../gui_resources/GameLobby.fxml"));
            Parent root = (Parent) loader.load();
            GameLobbyController controller = loader.getController();
            controller.init(uuid, new ArrayList<String>(Arrays.asList(Server.LOBBY_CHANNEL, Server.LEAVE_LOBBY_CHANNEL, Server.NEW_GAME_GRANTED, Server.CPU_GRANTED, Server.GET_LEADER_BOARD)));
            publish(controller.getConnection(), "join lobby", Server.LOBBY_CHANNEL);
            loadFXML(node, root);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML public VBox playerList;
    @FXML public VBox leaderBoardList;
    @FXML public ScrollPane leaderBoard;
    @FXML public ScrollPane playerScroll;

    @FXML public void initialize() throws Exception{
        playerScroll.setContent(playerList);
        leaderBoard.setContent(leaderBoardList);


        getSubscribers(Server.LOBBY_CHANNEL);
        FXMLLoader loader = new FXMLLoader(GameController.class.getResource("../../gui_resources/GameScreen.fxml"));
        try{ root = (Parent)loader.load();
            gameController = loader.getController();
        }
        catch (Exception e){e.printStackTrace();}

        publish(connection, "", Server.REQUEST_LEADER_BOARD);



    }


    class playerSelected implements EventHandler<MouseEvent>{


        public playerSelected(String user){ requestedOpponent = user; }
        @Override public void handle(MouseEvent e){
                connection.unsubscribe().channels(Arrays.asList(Server.LOBBY_CHANNEL)).execute();
                publish(connection, "leaving lobby" , Server.LEAVE_LOBBY_CHANNEL);
                publish(connection, requestedOpponent, Server.GAME_REQUEST_CHANNEL);
        }

    }


    @Override
    public void handleHereNow(PNHereNowResult result, PNStatus status){
            hereNow = result;
        Platform.runLater(()->{
            for(PNHereNowChannelData channelData : hereNow.getChannels().values()){
                channelData.getOccupants();
                for (PNHereNowOccupantData occupant : channelData.getOccupants()) {
                    if(!occupant.getUuid().equals(getUUID()))
                        addPlayer(occupant.getUuid());
                }

            }

    });
    }

    @Override
    public void handleSubCallBack(PubNub pubnub, PNMessageResult message){
                if(!((Stage)leaderBoardList.getScene().getWindow()).getTitle().equals(getUUID()))
                    Platform.runLater(()->{((Stage)leaderBoardList.getScene().getWindow()).setTitle(getUUID()); });

                String chan = message.getChannel();
                String sender = message.getPublisher();
                String msg = message.getMessage().toString().replace("\"","");


                if(chan.equals(Server.LOBBY_CHANNEL) && !(sender.equals(getUUID())) && playerMap.get(sender) == null )
                    addPlayer(sender);
                if(chan.equals(Server.LEAVE_LOBBY_CHANNEL) && !(msg.equals(getUUID())) )
                    removePlayer(sender);
                if(chan.equals(Server.NEW_GAME_GRANTED) && msg.equals(getUUID())) { // I requested to play someone
                        publish(connection, sender, requestedOpponent);
                        callInitializers(requestedOpponent, sender, true);
                }
                if(chan.equals(getUUID())){   // someone requested to play me
                    callInitializers(sender, msg, false);
                }
                if(chan.equals(Server.CPU_GRANTED) && msg.equals(getUUID())) {
                    callInitializers("CPU", sender, true);
                }
                if(chan.equals(Server.GET_LEADER_BOARD) && needLeaderBoard) {
                        getLeaderBoard(new Gson().fromJson(message.getMessage(), String[].class));
                        needLeaderBoard = !needLeaderBoard;
                    }





                    //publish to lobby, then get lobby subscribers, then subscribe to lobby
    }


    public void callInitializers(String opponent, String gameID, boolean playingX){
        connection.unsubscribeAll();
        publish(connection, "", Server.LEAVE_LOBBY_CHANNEL);
        gameController.setGameID(gameID);
        gameController.initData(new GameSettings(getUUID(), playingX, true));
        connection.unsubscribeAll();
        gameController.init(getUUID());
        gameController.setNames(getUUID(), opponent);
        launchGame();


    }

    private void getLeaderBoard(String ar[]){

       Platform.runLater(()->{
           for(String elem : ar)
           leaderBoardList.getChildren().add(new Button(elem));});

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
        //connection.unsubscribe().channels(Arrays.asList(Server.LOBBY_CHANNEL));

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
        //gameController.initData(new GameSettings(getUUID(), true ,true));
        switch(((Button)click.getSource()).getText()) {
            case "CPU (Easy)":
                publish(connection, Server.EASY, Server.REQUEST_CPU);
                break;
            case "CPU (Hard)":
                publish(connection, "hard", Server.REQUEST_CPU);
                break;
            default:
                break;
        }
    }

    public void logout(MouseEvent click) throws java.io.IOException {
        publish(connection,"",Server.LEAVE_LOBBY_CHANNEL);
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
