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
        FXMLLoader loader = new FXMLLoader(getClass().getResource(xmlFile));
        Scene GameScreenScene = new Scene((Pane)loader.load());

        Stage window = (Stage)((Node)click.getSource()).getScene().getWindow();


        window.setScene(GameScreenScene);

        window.show();
    }


}
