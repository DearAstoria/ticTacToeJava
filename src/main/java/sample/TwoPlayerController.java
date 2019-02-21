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
    public TextField xName, oName;

    @FXML public void initialize(){
        firstMover = x;
    }
    public void beginGame(MouseEvent click) throws java.io.IOException {
        nextWindow(click, "GameScreen.fxml");
    }
    public void back(MouseEvent click) throws java.io.IOException {
        nextWindow(click, "PlayerMode.fxml");
    }

    public void xo(MouseEvent click){
        Button bt = (Button)click.getSource();
        Button other;
        if(bt != firstMover) {
            bt.setTextFill(Color.valueOf("White"));
            bt.setStyle("-fx-background-color: #2b2a2a; -fx-border-color: white;");
            other = (bt != x) ? x : o;
            other.setTextFill(Color.web("#909090"));
            other.setStyle("-fx-background-color: black; -fx-border-color: gray;");
            firstMover =  bt;
        }
    }

    public void nextWindow(MouseEvent click, String xmlFile) throws java.io.IOException
    {
        // initialize loader
        FXMLLoader loader = new FXMLLoader(getClass().getResource(xmlFile));

        Scene GameScreenScene = new Scene((Pane)loader.load());



        GameState game = new GameState(xName.getText(),oName.getText(),firstMover.getText().charAt(0));
        GameController gameUI;
        if(xmlFile.equals("GameScreen.fxml")) {
            gameUI = loader.<GameController>getController();
            game.setUI(gameUI);
        }





        // load the next scene


        // get the stage... getSource = get object that was clicked on (the button) from the event, getScene = get the scene the button is a part of, getWindow = get the stage the scene is a part of
        Stage window = (Stage)((Node)click.getSource()).getScene().getWindow();

        // set stage to display the next scene
        window.setScene(GameScreenScene);

        window.show();
    }
}
