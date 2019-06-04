package server;

import client.raw_data.*;
import com.google.gson.Gson;
import com.pubnub.api.PubNub;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import pubnub_things.Subscriber;
import server.databaseOperations.DBOperations;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static client.raw_data.GameEngine.*;
import static java.lang.Character.isUpperCase;
import static pubnub_things.PubNubWrappers.publish;

public class GameHandler extends Subscriber {

    GameState game = new GameState();  // new GameState, where currentPlayer is X
    private String xPlayer;
    private String oPlayer;
    private boolean twoPlayerMode = true;
    private boolean easy;
    int i=0;

    public GameHandler(String x, String o) {  // two players
        init();
        this.xPlayer = x;
        this.oPlayer = o;
    }
    public GameHandler(String singlePlayer, boolean easy){  // one player
        init();
        xPlayer = singlePlayer;
        this.easy = easy;
        twoPlayerMode = false;
    }

    public String getxPlayer() {
        return xPlayer;
    }

    public String getoPlayer() {
        return oPlayer;
    }


    @Override
    public void handleSubCallBack(PubNub pubnub, PNMessageResult message) {

            Move move = new Gson().fromJson(message.getMessage(), Move.class);
            if(twoPlayerMode)
                computeMove(move, message.getPublisher());
            else
                cpuMode(move);
            game.nextTurn();

    }


    public void cpuMode(Move playerMove){

        takeSpace(game, playerMove);
        Move cpuMove;
        if(isUpperCase(checkGameStatus_or_lastMover(game))) {   // game over
            publish(connection, new MovePacket(playerMove, checkGameStatus_or_lastMover(game)), xPlayer);
            return; }

        game.nextTurn();
        if(easy)
            cpuMove = takeRandomSpace(game);
        else
            cpuMove = takeBestSpace(game);

        publish(connection, new MovePacket(cpuMove, checkGameStatus_or_lastMover(game)) , xPlayer);
    }

    public void computeMove(Move move, String sender) {



            takeSpace(game, move);
            MovePacket p = new MovePacket(move, checkGameStatus_or_lastMover(game));
            if (isUpperCase(p.status)) {         // send GameOver status
                publish(connection, p, xPlayer);
                publish(connection, p, oPlayer);
                updateDB(p.status);
            }
            else if (sender.equals(xPlayer) && twoPlayerMode)
                publish(connection, p, oPlayer);
            else
                publish(connection, p, xPlayer);

    }


    private void updateDB(char status){
        try {
            Class.forName(DBOperations.driver);
            Connection databaseConn = DriverManager.getConnection(DBOperations.tictactoe, DBOperations.USER, DBOperations.PASS/*"jdbc:sqlite:/Users/austinrosario/Desktop/Java/TicTacToe/TicTacToe/TicTacToe.db"*/);
            Statement query = databaseConn.createStatement();

            if(!twoPlayerMode)  // do not count games against CPU
                return;

            String loser = oPlayer, winner = xPlayer;
            if (status == 'O') {
                loser = xPlayer;
                winner = oPlayer;
            }

            if (status == 'T') {
                System.out.println("DRAW");
                query.executeUpdate("UPDATE USERS SET draws = draws + 1 WHERE username = " + "'" + xPlayer + "'");
                query.executeUpdate("UPDATE USERS SET draws = draws + 1 WHERE username = " + "'" + oPlayer + "'");
            } else
                System.out.println(" winner is " + winner);
            query.executeUpdate("UPDATE USERS SET wins = wins + 1 WHERE username = " + "'" + winner + "'");
            query.executeUpdate("UPDATE USERS SET losses = losses + 1 WHERE username = " + "'" + loser + "'");

        } catch (ClassNotFoundException e) {
            System.out.println(e.toString());
        } catch (SQLException e) {
            System.out.println(e.toString());
        }



    }


    @Override public String toString(){
        return new String("gameID " + getUUID() + " " + xPlayer + " " + oPlayer);
    }


}
