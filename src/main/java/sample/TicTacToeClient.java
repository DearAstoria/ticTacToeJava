package sample;


import java.io.*;
import java.net.*;
import java.util.Date;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import static javafx.application.Application.launch;

public class TicTacToeClient {



   /* @FXML  public StackPane cell00, cell01, cell02, cell10, cell11, cell12, cell20, cell21, cell22;
    @FXML  public Button RESTART; // a prompt to restart the game once it is over (as defined in GameScreen.fxml)
    @FXML  public BorderPane screen;
    @FXML  public Text myName, opponentName; */
    public Text [] playerDisplay;
    //private StackPane [][] location = new StackPane[3][3];



    static int PLAYER1 = 1, PLAYER1_WON = 1, PLAYER2 = 2, PLAYER2_WON = 2, DRAW = 0, CONTINUE = 5;


    // Indicate whether the player has the turn
    private boolean myTurn = false;

    // Indicate the token for the player
    private char myToken;

    // Indicate the token for the other player
    private char otherToken;


    private GameControllerForClient UI;

    private String name;

    // Create and initialize a title label
    private Label lblTitle = new Label();

    // Create and initialize a status label
    private Label lblStatus = new Label();

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

    public TicTacToeClient() {
        otherToken = ' ';
        myToken = ' ';
    }
    public void setUI(GameControllerForClient ui){
        UI = ui;
        ui.setClient(this);
    }


    public void connectToServer() {
            Socket socket;
        try {
            // Create a socket to connect to the server
            socket = new Socket(host, 8000);

            // Create an input stream to receive data from the server
            fromServer = new DataInputStream(socket.getInputStream());

            // Create an output stream to send data to the server
            toServer = new DataOutputStream(socket.getOutputStream());
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

        // Control the game on a separate thread
        new Thread(() -> {
            try {
                // Get notification from the server
                int player = fromServer.readInt();

                // Am I player 1 or 2?
                if (player == PLAYER1) {
                    myToken = 'X';
                    otherToken = 'O';

                    // write myName
                    toServer.writeBytes(UI.nameField.getText() + '\n');
                    // read opponent
                    String str = new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine() + " (" + otherToken + ")";
                    Platform.runLater(() -> {
                        UI.opponentName.setText(str);
                    });

                    Platform.runLater(() -> {
                        lblTitle.setText("Player 1 with token 'X'");
                        lblStatus.setText("Waiting for player 2 to join");
                    });


                    // The other player has joined
                    Platform.runLater(() ->
                            lblStatus.setText("Player 2 has joined. I start first"));

                    // It is my turn
                    myTurn = true;
                }

                else if (player == PLAYER2) {

                    myToken = 'O';
                    otherToken = 'X';

                    // read opponent
                    String str = new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine() + " (" + otherToken + ")";
                    Platform.runLater(() -> {
                        UI.opponentName.setText(str);
                    });
                    // write myName
                    toServer.writeBytes(UI.nameField.getText() + '\n');

                    Platform.runLater(() -> {
                        lblTitle.setText("Player 2 with token 'O'");
                        lblStatus.setText("Waiting for player 1 to move");
                    });
                }

                Platform.runLater(() -> {
                    UI.myName.setText(UI.nameField.getText() + " (" + myToken + ")"); });
                System.out.println("myTurn initialized to: " + myTurn);
                System.out.println("my token: " + myToken + " otherToken: " + otherToken + " my move: " + rowSelected + "," + columnSelected);

                // Continue to play
                while (continueToPlay) {
                    if (player == PLAYER1) {
                        waitForPlayerAction(); // Wait for player 1 to move
                        sendMove(); // Send the move to the server
                        receiveInfoFromServer(); // Receive info from the server
                    } else if (player == PLAYER2) {
                        receiveInfoFromServer(); // Receive info from the server
                        waitForPlayerAction(); // Wait for player 2 to move
                        sendMove(); // Send player 2's move to the server
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    /**
     * Wait for the player to mark a cell
     */
    private void waitForPlayerAction() throws InterruptedException {
        System.out.println(" wait   myTurn: " + myTurn);
        while (waiting) {
            Thread.sleep(100);
        }
        System.out.println("my token: " + myToken + " otherToken: " + otherToken + " my move: " + rowSelected + "," + columnSelected);

        waiting = true;
    }

    /**
     * Send this player's move to the server
     */
    private void sendMove() throws IOException {
        toServer.writeInt(rowSelected); // Send the selected row
        toServer.writeInt(columnSelected); // Send the selected column
    }

    /**
     * Receive info from the server
     */
    private void receiveInfoFromServer() throws IOException {
        // Receive game status
        int status = fromServer.readInt();

        if (status == PLAYER1_WON) {
            // Player 1 won, stop playing
            continueToPlay = false;
            if (myToken == 'X') {
                Platform.runLater(() -> UI.outputWinner(UI.myName.getText() + " wins", "GREEN"));//lblStatus.setText("I won! (X)"));
            } else if (myToken == 'O') {
                Platform.runLater(() ->
                        UI.outputWinner(UI.opponentName.getText() + " wins", "RED"));//RESTART.setText("Player 1 (X) has won!"));
                receiveMove();
            }
        } else if (status == PLAYER2_WON) {
            // Player 2 won, stop playing
            continueToPlay = false;
            if (myToken == 'O') {
                Platform.runLater(() -> UI.outputWinner(UI.myName.getText() + " wins", "GREEN"));//lblStatus.setText("I won! (O)"));
            } else if (myToken == 'X') {
                Platform.runLater(() ->
                        UI.outputWinner(UI.opponentName.getText() + " wins", "RED"));//lblStatus.setText("Player 2 (O) has won!"));
                receiveMove();
            }
        } else if (status == DRAW) {
            // No winner, game is over
            continueToPlay = false;
            Platform.runLater(() ->
                    UI.outputWinner("DRAW", "YELLOW"));//lblStatus.setText("Game is over, no winner!"));

            if (myToken == 'O') {
                receiveMove();
            }
        } else {
            receiveMove();
            Platform.runLater(() -> lblStatus.setText("My turn"));
            myTurn = true; // It is my turn
        }
    }

    private void receiveMove() throws IOException {
        // Get the other player's move
        int row = fromServer.readInt();
        int column = fromServer.readInt();

        //Platform.runLater(() -> cell[row][column].setToken(otherToken));

        Text t = new Text(Character.toString(otherToken));
        t.setStyle("-fx-font: 64 System;");
        Platform.runLater(() -> {
            (UI.getLocation())[row][column].getChildren().add(t);
            (UI.getLocation())[row][column].getChildren().get(0).setId("Permanent");
        });
        }

    public void setRowSelected(int arg) { rowSelected = arg;}
    public void setcolumnSelected(int arg){ columnSelected = arg;}
    public void setWaiting(boolean arg){ waiting = arg;}
    public void setMyTurn(boolean arg){myTurn = arg;}
    public boolean getMyTurn(){ return myTurn;}
    public char getMyToken(){return myToken;}



    }

