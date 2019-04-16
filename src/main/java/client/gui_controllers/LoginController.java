package client.gui_controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class LoginController {
    String onlineName; // a name used for this online session
    String userName;   // the username ID of the player
    String password;   // the password to the username ID

    public LoginController() {}

    public void loginClicked(MouseEvent click) throws java.io.IOException {
        // load the next scene
        Parent GameScreenParent = FXMLLoader.load(getClass().getResource("/client/gui_controllers/gui_controllers/GameLobby.fxml"));
        Scene GameScreenScene = new Scene(GameScreenParent);

        // get the stage... getSource: get object that was clicked on (the button) from the event, getScene: get the scene the button is a part of, getWindow: get the stage the scene is a part of
        Stage window = (Stage)((Node)click.getSource()).getScene().getWindow();

        // set stage to display the next scene
        window.setScene(GameScreenScene);

        window.show();
    }

    public void signUpClicked(MouseEvent click) {

    }

    // get connection to the server
    private void getConnection() {

    }
}
