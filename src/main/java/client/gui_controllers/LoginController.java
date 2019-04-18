package client.gui_controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginController {
    String email; // a name used for this online session
    String username;   // the username ID of the player
    String password;   // the password to the username ID

    public LoginController() {}

    public void loginClicked(MouseEvent click) throws java.io.IOException, java.sql.SQLException, java.lang.ClassNotFoundException {
        Connection dbConn;

        // database parameter =‎‎ jdbc:sqlite: + filepath to database
        String url = "jdbc:sqlite:/Users/austinrosario/Desktop/Java/TicTacToe/TicTacToe/TicTacToe.db"; // NOTE: this is the file path to a database on my computer for testing purposes, it does not go to the final database we will use - Austin
        // create a connection to the database
        Class.forName("org.sqlite.JDBC");
        dbConn = DriverManager.getConnection(url);

        boolean loginSucsessful = false;
        Statement query = dbConn.createStatement(); // initialize a statement object to generate and execute sql queries on the database
        ResultSet loginResults = query.executeQuery("SELECT email, username, password FROM USERS"); // get a list of all users from USERS table

        while(loginResults.next() && !loginSucsessful) { // search through results of query for matching login entered by user
            if(loginResults.getString("email").equals(email)) {
                if(loginResults.getString("username").equals(username)) {
                    if(loginResults.getString("password").equals(password)) {
                        loginSucsessful = true;
                    }
                }
            }
        }

        if(loginSucsessful) {
            System.out.println("login sucsessful");
            loginResults.close();

            // load the next scene
            Parent GameScreenParent = FXMLLoader.load(getClass().getResource("../../gui_resources/GameLobby.fxml"));
            Scene GameScreenScene = new Scene(GameScreenParent);

            // get the stage... getSource: get object that was clicked on (the button) from the event, getScene: get the scene the button is a part of, getWindow: get the stage the scene is a part of
            Stage window = (Stage)((Node)click.getSource()).getScene().getWindow();

            // set stage to display the next scene
            window.setScene(GameScreenScene);

            window.show();

        } else {
            System.out.println("login failed");
        }

        loginResults.close();

    }

    public void signUpClicked(MouseEvent click) {

    }

    // get connection to the server
    private void getConnection() {

    }
}
