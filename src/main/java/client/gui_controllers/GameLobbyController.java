package client.gui_controllers;

import client.raw_data.GameState;
import client.raw_data.GameSettings;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class GameLobbyController {

    public void joinGame(MouseEvent click) throws java.io.IOException {
        GameController controller = new GameController();

        switch(((Button)click.getSource()).getText()) {
            case "CPU (Easy)":
                controller.initData(new GameState(), new GameSettings("player 1", true, true, true), false);
                break;
            case "CPU (Hard)":
                controller.initData(new GameState(), new GameSettings("player 1", true, false, true), false);
                break;
            default:
                break;
        }

        // load the next scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/gui_controllers/gui_controllers/GameScreen.fxml")); // initialize FXMLLoader to load the next UI
        loader.setController(controller); // initialize the FXMLLoader to tell the UI to use the controller initialized from parameters in this object
        Scene GameScreenScene = new Scene(loader.load());

        // get reference to the current stage
        Stage window = (Stage)((Node)click.getSource()).getScene().getWindow();

        // set current stage to display the next scene
        window.setScene(GameScreenScene);

        window.show();
    }

    public void logout(MouseEvent click) throws java.io.IOException {
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
