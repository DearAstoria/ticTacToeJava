package client.gui_controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GameModeMenuController {
    public void singleMode(MouseEvent click) throws java.io.IOException {
        nextWindow(click, "../../gui_resources/GameSettingsMenu.fxml");
    }
    public void twoPlayerMode(MouseEvent click) throws java.io.IOException {
        nextWindow(click, "../../gui_resources/GameScreenForClient.fxml");
    }

    public void back(MouseEvent click) throws java.io.IOException {
        nextWindow(click, "../../gui_resources/StartupMenu.fxml");
    }

    public void nextWindow(MouseEvent click, String xmlFile) throws java.io.IOException
    {
        // load the next scene
        //Parent GameScreenParent = FXMLLoader.load(getClass().getResource(xmlFile));
        //Scene GameScreenScene = new Scene(GameScreenParent);


        FXMLLoader loader = new FXMLLoader(getClass().getResource(xmlFile));
        Scene GameScreenScene = new Scene((Pane)loader.load());

        // get the stage... getSource = get object that was clicked on (the button) from the event, getScene = get the scene the button is a part of, getWindow = get the stage the scene is a part of
        Stage window = (Stage)((Node)click.getSource()).getScene().getWindow();

/*
        TicTacToeClient client = new TicTacToeClient();
        if(xmlFile.equals("GameScreenForClient.fxml"))
        client.setUI(loader.<GameControllerForClient>getController());
*/

        // set stage to display the next scene
        window.setScene(GameScreenScene);

        window.show();
    }


}
