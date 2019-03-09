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



    @FXML  public StackPane cell00, cell01, cell02, cell10, cell11, cell12, cell20, cell21, cell22;
    @FXML  public Button RESTART; // a prompt to restart the game once it is over (as defined in GameScreen.fxml)
    @FXML  public BorderPane screen;
    @FXML  public Text xBox, oBox;
    public Text [] playerDisplay;
    private StackPane [][] location = new StackPane[3][3];



    static int PLAYER1 = 1, PLAYER1_WON = 1, PLAYER2 = 2, PLAYER2_WON = 2, DRAW = 0, CONTINUE = 5;


    // Indicate whether the player has the turn
    @FXML private boolean myTurn = false;

    // Indicate the token for the player
    @FXML private char myToken;

    // Indicate the token for the other player
    @FXML  private char otherToken;

    // Create and initialize cells
    //private Cell[][] cell = new Cell[3][3];

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
        initialize();
        connectToServer();
    }

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

    }


    private void connectToServer() {
        try {
            // Create a socket to connect to the server
            Socket socket = new Socket(host, 8000);

            // Create an input stream to receive data from the server
            fromServer = new DataInputStream(socket.getInputStream());

            // Create an output stream to send data to the server
            toServer = new DataOutputStream(socket.getOutputStream());
        } catch (Exception ex) {
            ex.printStackTrace();
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
                    Platform.runLater(() -> {
                        lblTitle.setText("Player 1 with token 'X'");
                        lblStatus.setText("Waiting for player 2 to join");
                    });

                    // Receive startup notification from the server
                    fromServer.readInt(); // Whatever read is ignored

                    // The other player has joined
                    Platform.runLater(() ->
                            lblStatus.setText("Player 2 has joined. I start first"));

                    // It is my turn
                    myTurn = true;
                } else if (player == PLAYER2) {
                    myToken = 'O';
                    otherToken = 'X';
                    Platform.runLater(() -> {
                        lblTitle.setText("Player 2 with token 'O'");
                        lblStatus.setText("Waiting for player 1 to move");
                    });
                }
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
                Platform.runLater(() -> outputWinner("X wins", "GREEN"));//lblStatus.setText("I won! (X)"));
            } else if (myToken == 'O') {
                Platform.runLater(() ->
                        outputWinner("X wins", "RED"));//RESTART.setText("Player 1 (X) has won!"));
                receiveMove();
            }
        } else if (status == PLAYER2_WON) {
            // Player 2 won, stop playing
            continueToPlay = false;
            if (myToken == 'O') {
                Platform.runLater(() -> outputWinner("O wins", "GREEN"));//lblStatus.setText("I won! (O)"));
            } else if (myToken == 'X') {
                Platform.runLater(() ->
                        outputWinner("O wins", "RED"));//lblStatus.setText("Player 2 (O) has won!"));
                receiveMove();
            }
        } else if (status == DRAW) {
            // No winner, game is over
            continueToPlay = false;
            Platform.runLater(() ->
                    outputWinner("DRAW", "YELLOW"));//lblStatus.setText("Game is over, no winner!"));

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
            location[row][column].getChildren().add(t);
            location[row][column].getChildren().get(0).setId("Permanent");
        });
        }


    public void mouseOver(MouseEvent event)
    {
        System.out.println("myTurn: " + myTurn);
        boolean bool = myTurn;
        System.out.println("myTurn: " + bool);

        //System.out.println("my token: " + myToken + " otherToken: " + otherToken + " my move: " + rowSelected + "," + columnSelected);

        if(!bool)
            return;
        System.out.println("yay");
        Pane space = (Pane)event.getSource();

        if(space.getChildren().isEmpty())
        {
            // Create a icon
            Text icon = new Text(Character.toString(myToken));
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
        System.out.println("myTurn: " + myTurn);
        boolean bool = myTurn;
        System.out.println("myTurn: " + bool);
        //System.out.println("my token: " + myToken + " otherToken: " + otherToken + " my move: " + rowSelected + "," + columnSelected);

        if(!bool)
            return;
        System.out.println("yay");
        Pane space = (Pane)event.getSource(); // get the board space that detected this event

        if(space.getChildren().get(0).getId() == "Temporary") // if the symbol on the space was temporary when the mouse was moved off of it
        {
            space.getChildren().remove(0); // then remove that symbol
        }
    }


    public void onClick(MouseEvent event) throws IOException
    {
        System.out.println("myTurn: " + myTurn);
        boolean bool = myTurn;
        System.out.println("myTurn: " + bool);
        //System.out.println("my token: " + myToken + " otherToken: " + otherToken + " my move: " + rowSelected + "," + columnSelected);

        if(!bool)
            return;
        System.out.println("yay");

        //System.out.println(game.toString()); // DEBUG
        Pane space = (Pane)event.getSource(); // get the board space that detected this event
        Text token = (Text)space.getChildren().get(0); // get the symbol in this space (because mouseOver event is guaranteed to occur first, we can be sure the space is not empty when calling it's children)

        if(token.getId() != "Permanent") // if the text object is not permanently on the space (the space has not been taken yet), then then take this space
        {
            myTurn = false;

            // set properties so that the text generated in the mouseOver event opaque and permanent on the space
            token.setOpacity(1);
            token.setId("Permanent");


            rowSelected = GridPane.getRowIndex(space) != null ? GridPane.getRowIndex(space) : 0;
            columnSelected = GridPane.getColumnIndex(space) != null ? GridPane.getColumnIndex(space) : 0;
            lblStatus.setText("Waiting for the other player to move");
            waiting = false;
        }
    }


    // reveals hidden button in this scene that displays who won the game and prompts the user to click it to restart
    public void outputWinner(/*String winningMessage*/String winner, String color)
    {
        //game.setGameOver();
        //game.savedGame.delete();
        // overwrite initial properties on the button which made it hidden


        RESTART.setText(winner);    // replace initial message with an end game message
        RESTART.setTextFill(Paint.valueOf(color));
        RESTART.setOpacity(1);              // make the hidden button visible
        RESTART.setMouseTransparent(false); // make false so that the hidden button can detect mouse events again
    }

    public void restart(ActionEvent click) throws java.io.IOException
    {
       /* // load the next scene
        Parent GameScreenParent = FXMLLoader.load(getClass().getResource("StartupMenu.fxml"));
        Scene GameScreenScene = new Scene(GameScreenParent);

        // get the stage... getSource = get object that was clicked on (the button) from the event, getScene = get the scene the button is a part of, getWindow = get the stage the scene is a part of
        Stage window = (Stage)((Node)click.getSource()).getScene().getWindow();

        // set stage to display the next scene
        window.setScene(GameScreenScene);
        window.show();*/
    }
    public void exit(MouseEvent click) throws java.io.IOException
    {
        /*Parent GameScreenParent = FXMLLoader.load(getClass().getResource("StartupMenu.fxml"));
        Scene GameScreenScene = new Scene(GameScreenParent);
        Stage window = (Stage)((Node)click.getSource()).getScene().getWindow();
        window.setScene(GameScreenScene);
        window.show();*/
    }
    /*
    // An inner class for a cell
    public class Cell extends Pane {
        // Indicate the row and column of this cell in the board
        private int row;
        private int column;

        // Token used for this cell
        private char token = ' ';

        public Cell(int row, int column) {
            this.row = row;
            this.column = column;
            this.setPrefSize(2000, 2000); // What happens without this?
            setStyle("-fx-border-color: black"); // Set cell's border
            this.setOnMouseClicked(e -> handleMouseClick());
        }

        ///**
         * Return token
        //
        public char getToken() {
            return token;
        }

        /**
         * Set a new token

        public void setToken(char c) {
            token = c;
            repaint();
        }

        protected void repaint() {
            if (token == 'X') {
                Line line1 = new Line(10, 10,
                        this.getWidth() - 10, this.getHeight() - 10);
                line1.endXProperty().bind(this.widthProperty().subtract(10));
                line1.endYProperty().bind(this.heightProperty().subtract(10));
                Line line2 = new Line(10, this.getHeight() - 10,
                        this.getWidth() - 10, 10);
                line2.startYProperty().bind(
                        this.heightProperty().subtract(10));
                line2.endXProperty().bind(this.widthProperty().subtract(10));

                // Add the lines to the pane
                this.getChildren().addAll(line1, line2);
            } else if (token == 'O') {
                Ellipse ellipse = new Ellipse(this.getWidth() / 2,
                        this.getHeight() / 2, this.getWidth() / 2 - 10,
                        this.getHeight() / 2 - 10);
                ellipse.centerXProperty().bind(
                        this.widthProperty().divide(2));
                ellipse.centerYProperty().bind(
                        this.heightProperty().divide(2));
                ellipse.radiusXProperty().bind(
                        this.widthProperty().divide(2).subtract(10));
                ellipse.radiusYProperty().bind(
                        this.heightProperty().divide(2).subtract(10));
                ellipse.setStroke(Color.BLACK);
                ellipse.setFill(Color.WHITE);

                getChildren().add(ellipse); // Add the ellipse to the pane
            }
        }

        ///* Handle a mouse click event
        private void handleMouseClick() {
            // If cell is not occupied and the player has the turn
            if (token == ' ' && myTurn) {
                setToken(myToken);  // Set the player's token in the cell
                myTurn = false;
                rowSelected = row;
                columnSelected = column;
                lblStatus.setText("Waiting for the other player to move");
                waiting = false; // Just completed a successful move
            }
        }

    } */

    }

