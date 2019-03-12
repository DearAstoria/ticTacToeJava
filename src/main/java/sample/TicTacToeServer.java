package sample;

import java.io.*;
import java.net.*;
import java.util.Date;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class TicTacToeServer extends Application  {
    private int sessionNo = 1; // Number a session

    static int PLAYER1 = 1, PLAYER1_WON = 1, PLAYER2 = 2, PLAYER2_WON = 2, DRAW = 0, CONTINUE = 5;

    @Override // Override the start method in the Application class
    public void start(Stage primaryStage) {
        TextArea taLog = new TextArea();

        // Create a scene and place it in the stage
        Scene scene = new Scene(new ScrollPane(taLog), 450, 200);
        primaryStage.setTitle("TicTacToeServer"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage

        new Thread( () -> {
            try {
                // Create a server socket
                ServerSocket serverSocket = new ServerSocket(8000);
                Platform.runLater(() -> taLog.appendText(new Date() +
                        ": Server started at socket 8000\n"));

                // Ready to create a session for every two players
                while (true) {
                    Platform.runLater(() -> taLog.appendText(new Date() +
                            ": Wait for players to join session " + sessionNo + '\n'));

                    // Connect to player 1
                    Socket player1 = serverSocket.accept();

                    Platform.runLater(() -> {
                        taLog.appendText(new Date() + ": Player 1 joined session "
                                + sessionNo + '\n');
                        taLog.appendText("Player 1's IP address" +
                                player1.getInetAddress().getHostAddress() + '\n');
                    });

                    // Notify that the player is Player 1
                    new DataOutputStream(
                            player1.getOutputStream()).writeInt(PLAYER1);

                    // Connect to player 2
                    Socket player2 = serverSocket.accept();

                    Platform.runLater(() -> {
                        taLog.appendText(new Date() +
                                ": Player 2 joined session " + sessionNo + '\n');
                        taLog.appendText("Player 2's IP address" +
                                player2.getInetAddress().getHostAddress() + '\n');
                    });


                    Platform.runLater(() -> {
                        try {
                        taLog.appendText(new BufferedReader(new InputStreamReader(player1.getInputStream())).readLine());}
                        catch (IOException e){e.printStackTrace();}
                    //String a = new BufferedReader(new InputStreamReader(player1.getInputStream())).readLine();
                    //System.out.println(a);
                            });

                     /** edit */
                    //new OutputStreamWriter(player2.getOutputStream(), "UTF-8").flush();
                    //new PrintWriter(player2.getOutputStream(), true).write(a); //.write(new BufferedReader(new InputStreamReader(player1.getInputStream())).readLine());


                    // Notify that the player is Player 2
                    new DataOutputStream(
                            player2.getOutputStream()).writeInt(PLAYER2);

                    // Display this session and increment session number
                    Platform.runLater(() ->
                            taLog.appendText(new Date() +
                                    ": Start a thread for session " + sessionNo++ + '\n'));

                    /** edit */
                    new OutputStreamWriter(player1.getOutputStream(), "UTF-8").flush();

                    // Launch a new thread for this session of two players
                    new Thread(new HandleASession(player1, player2)).start();
                }
            }
            catch(IOException ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    // Define the thread class for handling a new session for two players
    class HandleASession implements Runnable {
        private Socket player1;
        private Socket player2;

        // Create and initialize cells
        //private char[][] cell =  new char[3][3];

        GameState game = new GameState();

        private DataInputStream fromPlayer1;
        private DataOutputStream toPlayer1;
        private DataInputStream fromPlayer2;
        private DataOutputStream toPlayer2;

        // Continue to play
        private boolean continueToPlay = true;

        /** Construct a thread */
        public HandleASession(Socket player1, Socket player2) {
            this.player1 = player1;
            this.player2 = player2;

         /*   // Initialize cells
            for (int i = 0; i < 3; i++)
                for (int j = 0; j < 3; j++)
                    cell[i][j] = ' ';
        */
        }

        /** Implement the run() method for the thread */
        public void run() {
            try {
                // Create data input and output streams
                DataInputStream fromPlayer1 = new DataInputStream(
                        player1.getInputStream());
                DataOutputStream toPlayer1 = new DataOutputStream(
                        player1.getOutputStream());
                DataInputStream fromPlayer2 = new DataInputStream(
                        player2.getInputStream());
                DataOutputStream toPlayer2 = new DataOutputStream(
                        player2.getOutputStream());

                // Write anything to notify player 1 to start
                // This is just to let player 1 know to start
                toPlayer1.writeInt(1);

                // Continuously serve the players and determine and report
                // the game status to the players
                while (true) {
                    // Receive a move from player 1
                    int row = fromPlayer1.readInt();
                    int column = fromPlayer1.readInt();
                    System.out.println("reveived X move");

                    //cell[row][column] = 'X';
                    game.set('X',row,column);

                    char isWon = game.checkForWin();

                    // Check if Player 1 wins
                    if (/*isWon('X')*/isWon == 'X') {
                        toPlayer1.writeInt(PLAYER1_WON);
                        toPlayer2.writeInt(PLAYER1_WON);
                        sendMove(toPlayer2, row, column);
                        break; // Break the loop
                    }
                    else if (isWon == 'T') { // Check if all cells are filled
                        toPlayer1.writeInt(DRAW);
                        toPlayer2.writeInt(DRAW);
                        sendMove(toPlayer2, row, column);
                        break;
                    }
                    else {
                        // Notify player 2 to take the turn
                        toPlayer2.writeInt(CONTINUE);

                        // Send player 1's selected row and column to player 2
                        sendMove(toPlayer2, row, column);
                    }

                    // Receive a move from Player 2
                    row = fromPlayer2.readInt();
                    column = fromPlayer2.readInt();
                    System.out.println("reveived X move");
                    //cell[row][column] = 'O';
                    game.set('O',row,column);

                    // Check if Player 2 wins
                    if (isWon == 'O') {
                        toPlayer1.writeInt(PLAYER2_WON);
                        toPlayer2.writeInt(PLAYER2_WON);
                        sendMove(toPlayer1, row, column);
                        break;
                    }
                    else {
                        // Notify player 1 to take the turn
                        toPlayer1.writeInt(CONTINUE);

                        // Send player 2's selected row and column to player 1
                        sendMove(toPlayer1, row, column);
                    }
                }
            }
            catch(IOException ex) {
                ex.printStackTrace();
            }
        }

        /** Send the move to other player */
        private void sendMove(DataOutputStream out, int row, int column)
                throws IOException {
            out.writeInt(row); // Send row index
            out.writeInt(column); // Send column index
        }
    }

    /**
     * The main method is only needed for the IDE with limited
     * JavaFX support. Not needed for running from the command line.
     */
    public static void main(String[] args) {
        launch(args);
    }
}