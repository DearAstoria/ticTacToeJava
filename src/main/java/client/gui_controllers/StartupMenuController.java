package client.gui_controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class StartupMenuController
{
    public void newGameClicked(MouseEvent click) throws java.io.IOException {
        // load the next scene
        Parent GameScreenParent = FXMLLoader.load(getClass().getResource("../../gui_resources/GameModeMenu.fxml"));
        Scene GameScreenScene = new Scene(GameScreenParent);

        // get the stage... getSource: get object that was clicked on (the button) from the event, getScene: get the scene the button is a part of, getWindow: get the stage the scene is a part of
        Stage window = (Stage)((Node)click.getSource()).getScene().getWindow();

        // set stage to display the next scene
        window.setScene(GameScreenScene);

        window.show();
    }

    public void resumeClicked(MouseEvent click) throws Exception {

    }

}
