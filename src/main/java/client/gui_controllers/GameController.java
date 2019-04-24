package client.gui_controllers;

import client.raw_data.*;
import com.google.gson.Gson;
import com.pubnub.api.PubNub;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import pubnubWrappers.Subscriber;

import static java.lang.Character.toUpperCase;
import static pubnubWrappers.PubNubWrappers.publish;

public class GameController extends Subscriber {
    @FXML public StackPane cell00, cell01, cell02, cell10, cell11, cell12, cell20, cell21, cell22;
    @FXML public Button RESTART;
    @FXML public Text myName, opponentName;

    StackPane[][] spaces;  // UI board space objects
    GameState game;        // Data of what is in each board space, who's turn it is
    GameSettings settings; // Name of the player, difficulty of the computer
    boolean humanOpponent; // is the requestedOpponent another human player or the computer

    /************************/
    char status;
    String gameID;
    boolean myTurn;
    boolean gameOver = false;
    /************************/


    public void setGameID(String s){
        gameID = s;
    }

    public void setNames(String myname, String opponent){
        myName.setText(myname);
        opponentName.setText(opponent);
    }

    public void mouseOver(MouseEvent event) {
        // if the game is not over and it is the current player's turn, then provide mouse over feedback for the requestedOpponent when selecting a space
        if(/**!game.isGameOver()*/ !gameOver && myTurn/**game.getCurrentTurn() == settings.getPlayerLetter()*/) {
            Pane space = (Pane)event.getSource(); // get the space that is hovered over
            if(space.getChildren().isEmpty()) { // if the space is empty
                takeSpace(space, settings.getPlayerLetter()/**game.getCurrentTurn()*/,true); // then generate a temporary letter in that space to provide feedback
            }
        }
    }

    // Clear the highlighted symbol if the mouse moves out of a space
    public void mouseOut(MouseEvent event) {
        // if the game is not over and it is the current player's turn, then clear the space that the requestedOpponent had moused over
        if(/**!game.isGameOver()*/ !gameOver && myTurn/**game.getCurrentTurn() == settings.getPlayerLetter()*/) {
            Pane space = (Pane)event.getSource(); // get the space that is hovered over
            if(!space.getChildren().isEmpty()) { // if the space is not empty
                if(space.getChildren().get(0).getId() == "Temporary") { // and if the space is occupied by a temporary letter (permanent would indicate that the space was taken)
                    space.getChildren().remove(0); // then clear the space
                }
            }
        }
    }

    public void spaceClicked(MouseEvent event) {
        if(/**!game.isGameOver()*/ !gameOver && myTurn) { // if the game is not over...
            Pane space = (Pane)event.getSource(); // then get the space that is clicked on
            if(space.getChildren().get(0).getId() != "Permanent") { // ...and if the space is not taken then
                int coord[] = getSpace(space.getId()); // get the numerical coordinates of the space
                /** replaced */ //GameEngine.takeSpace(game, coord[0],coord[1]); // take that space within the GameState

                takeSpace(space, settings.getPlayerLetter()/**game.getCurrentTurn()*/, false); // take that space within the UI
                myTurn = false;
                publish(connection, new Move(coord[0], coord[1]), gameID);


                /** replaced */ //game.nextTurn(); // pass the turn off to the next player

               /** replace */  /*if(GameEngine.winFound(game) || GameEngine.checkGameState(game) == 'T') { // if taking this space results in a win or a tie
                    game.toggleGameOver(); // toggle game over
                    if(humanOpponent) {} // and if also playing against a human requestedOpponent, send this move to the server somehow here
                } else if(humanOpponent) { // else if playing against a human requestedOpponent
                    // send coord[] to server (send move to requestedOpponent) here
                    // wait until the server updates client with requestedOpponent's move (wait for requestedOpponent's turn to end) here
                    game.nextTurn(); // when the server returns that the requestedOpponent has mad his/her turn, switch turns again
                    if(GameEngine.winFound(game)) { game.toggleGameOver(); } // if after requestedOpponent's turn, a win is found, toggle game over
                } else { // else calculate the computer's turn
                    computerTurn();
                    game.nextTurn();
                    if(GameEngine.winFound(game)) { game.toggleGameOver(); }
                }
                */

                update(); // update the spaces in the UI to reflect any changes to the board data by requestedOpponent
                System.out.println(game.toString());

                if(game.isGameOver()) { // if the game is over
                    outputWinner(); // display winner
                }
            }
        }
    }

    public void quit(MouseEvent event) throws java.io.IOException {
        // load the next scene
        Parent GameScreenParent = FXMLLoader.load(getClass().getResource("../../gui_resources/GameLobby.fxml"));
        Scene GameScreenScene = new Scene(GameScreenParent);

        // get the stage... getSource: get object that was clicked on (the button) from the event, getScene: get the scene the button is a part of, getWindow: get the stage the scene is a part of
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        // set stage to display the next scene
        window.setScene(GameScreenScene);

        window.show();
    }

    public void computerTurn() {
        if(settings.isEasy()) {
            GameEngine.takeRandomSpace(game);
        }
        else {
            GameEngine.takeBestSpace(game);
        }
    }



    @FXML // initialize FXML objects in the controller
    public void initialize() {
        spaces = new StackPane[][]{ {cell00, cell01, cell02},
                                    {cell10, cell11, cell12},
                                    {cell20, cell21, cell22} };
        update(); // update spaces to match with GameState
        myName.setText(settings.getPlayer()); // set the player's name in the top-left box
        if(humanOpponent) { // if playing against a human requestedOpponent
            // get the requestedOpponent's name from the server
            // set the requestedOpponent's name in the top-right box
        } else if(settings.isEasy()) { // if playing against the easy mode AI
            opponentName.setText("Easy CPU");
        }
        else { // else playing against the hard mode AI
            opponentName.setText("Hard CPU");
        }

    }

    // initialize data in the controller
    public void initData(GameState state, GameSettings settings, boolean humanOpponent) {
        this.game = state;
        this.settings = settings;
        this.humanOpponent = humanOpponent;

        if(game.getCurrentTurn() != settings.getPlayerLetter()) { // if the player does not move first then wait for the requestedOpponent's move first
            if(humanOpponent) { // if playing a human requestedOpponent
                // wait until requestedOpponent's turn has ended from the server
                game.nextTurn();
            } else { // else calculate the computer's move
                computerTurn();
                game.nextTurn();
            }
        }
    }
    public void initData(GameSettings settings)
    {
        initData(new GameState(), settings, true);
        myTurn = settings.getPlayerLetter() == GameState.X;
        //System.out.println(settings + "\n" + "myTurn: " + myTurn);
       // System.out.println("gameOver" + gameOver);


    }


    @Override public void handleSubCallBack(PubNub pubnub, PNMessageResult message){
        MovePacket move_status = new Gson().fromJson(message.getMessage(), MovePacket.class);
        Move move = move_status.move; //get move
        status = move_status.status;   // get token of person who just moved


        /*if(message.getPublisher().equals(opponentName.getText()))
            Platform.runLater(() -> { outputWinner(); });*/

        //else if(message.getPublisher().equals(getUUID())){ /** do nothing */}

        //else {                                          // from server
            gameOver = Character.isUpperCase(status);
            myTurn = settings.getPlayerLetter() != toUpperCase(status);  // it is my turn if status says last person who moved is not me

            // update UI with opponent's move
            if(status != settings.getPlayerLetter())
            Platform.runLater(() -> { takeSpace(spaces[move.getRow()][move.getCol()], toUpperCase(status), false); });

            if (gameOver) {
                Platform.runLater(() -> { outputWinner(); });             // output winner
                //publish(connection, move_status, opponentName.getText());
            }
        //}


    }

    public GameController() {
        this(new GameState(), new GameSettings());
    }

    // constructor for initializing a game against a computer requestedOpponent
    public GameController(GameState state, GameSettings settings) {
        initData(state, settings, false);
    }

    public void exit(MouseEvent click) throws java.io.IOException {
        Parent GameScreenParent = FXMLLoader.load(getClass().getResource("../../gui_resources/StartupMenu.fxml"));
        Scene GameScreenScene = new Scene(GameScreenParent);
        Stage window = (Stage)((Node)click.getSource()).getScene().getWindow();
        window.setScene(GameScreenScene);
        window.show();
    }

    // update the game board in the UI to reflect what the game state is
    private void update() {
        /*
        // for each space on the board update it with what is in the game state
        for(int y = 0; y < 3; y++) {
            for(int x = 0; x < 3; x++) {
                if(spaces[x][y].getChildren().isEmpty()) { // if the current space on display is empty
                    if(game.getBoardSpaces().get(x, y) != ' ') { // but it does not match with the game state
                        takeSpace(spaces[x][y], game.getBoardSpaces().get(x, y), false); // then update it to display the correct state
                    }
                }
            }
        }*/
    }

    // places a letter on a space in the UI
    private void takeSpace(Pane space, char letter, boolean temporary) {
        Text icon = new Text(Character.toString(letter)); // create a new X or O to be displayed in the space
        icon.setTextAlignment(TextAlignment.CENTER);
        icon.setFont(new Font(64));
        if(temporary) {
            icon.setOpacity(0.25);
            icon.setId("Temporary"); // set the space to temporary to indicate the letter in it should be deleted later
        } else {
            icon.setOpacity(1);
            icon.setId("Permanent"); // set the space to permanent to indicate the letter should not be altered again
        }

        space.getChildren().add(icon); // Put the letter in this space
    }

    // display the restart button showing the winner, and when clicked restarts the game
    public void outputWinner() {
        // overwrite initial properties on the button which made it hidden on the screen
        RESTART.setOpacity(1);              // make the hidden button visible
        RESTART.setMouseTransparent(false); // make false so that the hidden button can detect mouse events again
        switch(status) {
         case 'X':
         RESTART.setText("X wins");
         break;
         case 'O':
         RESTART.setText("O wins");
         break;
         case 'T':
         RESTART.setText("Draw");
         break;
         default:
         break;
         }

        /**switch(GameEngine.checkGameState(game)) {
            case 'X':
                RESTART.setText("X wins");
                break;
            case 'O':
                RESTART.setText("O wins");
                break;
            case 'T':
                RESTART.setText("Draw");
                break;
            case ' ':
            default:
                break;
        }*/
    }

    // given the fxID of a space return the x-y coordinates of it in the GameState
    private int[] getSpace(String spaceID) {
        switch(spaceID) {
            case "cell00":
                return new int[]{0, 0};
            case "cell01":
                return new int[]{0, 1};
            case "cell02":
                return new int[]{0, 2};
            case "cell10":
                return new int[]{1, 0};
            case "cell11":
                return new int[]{1, 1};
            case "cell12":
                return new int[]{1, 2};
            case "cell20":
                return new int[]{2, 0};
            case "cell21":
                return new int[]{2, 1};
            case "cell22":
                return new int[]{2, 2};
            default:
                return new int[]{-1, -1};
        }
    }
}