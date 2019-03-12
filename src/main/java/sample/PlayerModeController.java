package sample;


import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.Font;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.text.FontWeight;

//import java.awt.*;


public class PlayerModeController {

    public void onHovered(MouseEvent e){
        ((Button)e.getSource()).setStyle("-fx-text-fill: white; -fx-background-color: #2b2a2a; -fx-border-color: white; -fx-background-radius: 0; -fx-background-insets: 0px;");
    }
    public void onMouseExit(MouseEvent e){
        ((Button)e.getSource()).setStyle("-fx-text-fill: #a4a4a4; -fx-background-color: black; -fx-border-color: grey; -fx-background-insets: 0px; -fx-background-radius: 0;");
    }

    public void singleMode(MouseEvent click) throws java.io.IOException {
        nextWindow(click, "SinglePlayerSettings.fxml");
    }
    public void twoPlayerMode(MouseEvent click) throws java.io.IOException {
        nextWindow(click, "GameScreenForClient.fxml");
    }

    public void back(MouseEvent click) throws java.io.IOException {
            nextWindow(click, "StartupMenu.fxml");
    }

    public void clicked(){}


    public void nextWindow(MouseEvent click, String xmlFile) throws java.io.IOException
    {
        // load the next scene
        //Parent GameScreenParent = FXMLLoader.load(getClass().getResource(xmlFile));
        //Scene GameScreenScene = new Scene(GameScreenParent);


        FXMLLoader loader = new FXMLLoader(getClass().getResource(xmlFile));
        Scene GameScreenScene = new Scene((Pane)loader.load());

        // get the stage... getSource = get object that was clicked on (the button) from the event, getScene = get the scene the button is a part of, getWindow = get the stage the scene is a part of
        Stage window = (Stage)((Node)click.getSource()).getScene().getWindow();


        TicTacToeClient client = new TicTacToeClient();
        if(xmlFile.equals("GameScreenForClient.fxml"))
            client.setUI(loader.<GameControllerForClient>getController());

        // set stage to display the next scene
        window.setScene(GameScreenScene);

        window.show();
    }


}
