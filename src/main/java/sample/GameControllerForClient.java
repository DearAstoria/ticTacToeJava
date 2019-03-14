package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class GameControllerForClient {

    @FXML
    public StackPane cell00, cell01, cell02, cell10, cell11, cell12, cell20, cell21, cell22;
    @FXML  public Button RESTART; // a prompt to restart the game once it is over (as defined in GameScreen.fxml)
    @FXML  public BorderPane screen;
    @FXML  public Text myName, opponentName;
    @FXML  public TextField nameField;
    @FXML  public VBox nameEntryWindow;
    @FXML  public  VBox waitForPlayer2;
    public Text [] playerDisplay;
    private StackPane [][] location = new StackPane[3][3];
    private TicTacToeClient client;

    static int PLAYER1 = 1, PLAYER1_WON = 1, PLAYER2 = 2, PLAYER2_WON = 2, DRAW = 0, CONTINUE = 5;

    private String name;



    // Indicate selected row and column by the current move
    private int rowSelected;
    private int columnSelected;

    // Input and output streams from/to server
    private DataInputStream fromServer;
    private DataOutputStream toServer;

    // Continue to play?
    private boolean continueToPlay = true;

    // Wait for the player to mark a cell
    private boolean waiting = true;

    // Host name or ip
    private String host = "localhost";

    public GameControllerForClient() { }

    @FXML public void initialize(){
        location[0][0]  = cell00;
        location[0][1]  = cell01;
        location[0][2]  = cell02;
        location[1][0]  = cell10;
        location[1][1]  = cell11;
        location[1][2]  = cell12;
        location[2][0]  = cell20;
        location[2][1]  = cell21;
        location[2][2]  = cell22;
        myName.setText(name);

    }

    public StackPane[][] getLocation(){return location;}

    public void setClient(TicTacToeClient client){
        this.client = client;
        //client.setUI(this);
        }

    public void mouseOver(MouseEvent event)
    {

        if(!client.getMyTurn())
            return;
        Pane space = (Pane)event.getSource();

        if(space.getChildren().isEmpty())
        {
            // Create a icon
            Text icon = new Text(Character.toString(client.getMyToken()));
            icon.setTextAlignment(TextAlignment.CENTER);
            icon.setFont(new Font(64));
            icon.setOpacity(0.25);
            icon.setId("Temporary");

            // Put the icon in this space
            space.getChildren().add(icon);
        }
    }

    // Clear the highlighted symbol if the mouse moves out of a space
    public void mouseOut(MouseEvent event)
    {
        if(!client.getMyTurn())
            return;
        Pane space = (Pane)event.getSource(); // get the board space that detected this event

        if(space.getChildren().get(0).getId() == "Temporary") // if the symbol on the space was temporary when the mouse was moved off of it
        {
            space.getChildren().remove(0); // then remove that symbol
        }
    }


    public void onClick(MouseEvent event) throws IOException
    {
        if(!client.getMyTurn())
            return;

        Pane space = (Pane)event.getSource(); // get the board space that detected this event
        Text token = (Text)space.getChildren().get(0); // get the symbol in this space (because mouseOver event is guaranteed to occur first, we can be sure the space is not empty when calling it's children)

        if(token.getId() != "Permanent") // if the text object is not permanently on the space (the space has not been taken yet), then then take this space
        {
            client.setMyTurn(false);

            // set properties so that the text generated in the mouseOver event opaque and permanent on the space
            token.setOpacity(1);
            token.setId("Permanent");

            client.setRowSelected(GridPane.getRowIndex(space) != null ? GridPane.getRowIndex(space) : 0);
            client.setcolumnSelected(GridPane.getColumnIndex(space) != null ? GridPane.getColumnIndex(space) : 0);
            myName.setOpacity(.5);
            client.setWaiting(false);
        }
    }


    // reveals hidden button in this scene that displays who won the game and prompts the user to click it to restart
    public void outputWinner(/*String winningMessage*/String winner, String color)
    {

        RESTART.setText(winner);    // replace initial message with an end game message
        RESTART.setTextFill(Paint.valueOf(color));
        RESTART.setOpacity(1);              // make the hidden button visible
        RESTART.setMouseTransparent(false); // make false so that the hidden button can detect mouse events again
    }

    public void restart(ActionEvent click) throws java.io.IOException
    {
        // load the next scene
        Parent GameScreenParent = FXMLLoader.load(getClass().getResource("StartupMenu.fxml"));
        Scene GameScreenScene = new Scene(GameScreenParent);

        // get the stage... getSource = get object that was clicked on (the button) from the event, getScene = get the scene the button is a part of, getWindow = get the stage the scene is a part of
        Stage window = (Stage)((Node)click.getSource()).getScene().getWindow();

        // set stage to display the next scene
        window.setScene(GameScreenScene);
        window.show();
    }
    public void exit(MouseEvent click) throws java.io.IOException
    {
        Parent GameScreenParent = FXMLLoader.load(getClass().getResource("StartupMenu.fxml"));
        Scene GameScreenScene = new Scene(GameScreenParent);
        Stage window = (Stage)((Node)click.getSource()).getScene().getWindow();
        window.setScene(GameScreenScene);
        window.show();
    }

    public void join(MouseEvent click){
        nameEntryWindow.setVisible(false);
        client.connectToServer();
    }

}

