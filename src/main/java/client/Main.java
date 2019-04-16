package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../gui_resources/Login.fxml"));
        primaryStage.setTitle("TicTacToe");
        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.show();


        //Database example
        //databaseOperations.PostgresqlExample.createDatabase();


    }

    public static void main(String[] args) {
        launch(args);
    }
}
