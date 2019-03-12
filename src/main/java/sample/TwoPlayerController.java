package sample;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.Font;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.text.FontWeight;
import javafx.scene.control.TextField;


public class TwoPlayerController {



    public Button firstMover, x, o;
    public TextField name;

    @FXML public void initialize(){
        firstMover = x;
    }
    public void beginGame(MouseEvent click) throws java.io.IOException {
        nextWindow(click, "GameScreenForClient.fxml");
    }
    public void back(MouseEvent click) throws java.io.IOException {
        nextWindow(click, "PlayerMode.fxml");
    }



    public void beginHovered(MouseEvent e){
        ((Button)e.getSource()).setStyle("-fx-border-width: 2; -fx-background-insets: 3;   -fx-border-radius: 90; -fx-background-radius: 90; -fx-text-fill: black; -fx-border-color: black; -fx-background-color: white;");


    }
    public void beginExit(MouseEvent e){
        ((Button)e.getSource()).setStyle("-fx-background-radius: 90; -fx-background-insets: 3;   -fx-text-fill: white; -fx-background-color: black; -fx-border-color: white; -fx-border-radius: 90; -fx-border-width: 2;");
    }

    public void nextWindow(MouseEvent click, String xmlFile) throws java.io.IOException
    {
        // initialize loader
        FXMLLoader loader = new FXMLLoader(getClass().getResource(xmlFile));
        Scene GameScreenScene = new Scene((Pane)loader.load());

        //Parent GameScreenParent = FXMLLoader.load(getClass().getResource(xmlFile));
        //Scene GameScreenScene = new Scene(GameScreenParent);

        // get the stage... getSource = get object that was clicked on (the button) from the event, getScene = get the scene the button is a part of, getWindow = get the stage the scene is a part of
        Stage window = (Stage)((Node)click.getSource()).getScene().getWindow();

        //TicTacToeClient game = new TicTacToeClient();
        //GameControllerForClient gameUI;
        if(xmlFile.equals("GameScreen.fxml"))
            loader.<GameControllerForClient>getController().setClient(new TicTacToeClient());


        // set stage to display the next scene
        window.setScene(GameScreenScene);

        window.show();
    }
}
