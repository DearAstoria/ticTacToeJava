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



public class YooController {

    Font f;
    Button resume;
    Button newGame;
    public void onHovered(MouseEvent e){
        Button button = (Button)e.getSource();
        button.setStyle("-fx-border-width: 2; -fx-background-insets: 0; -fx-border-radius: 90; -fx-background-radius: 90; -fx-text-fill: black; -fx-border-color: black; -fx-background-color: white;");
        //button.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
    }
    public void mouseExit(MouseEvent e){
        Button button = (Button)e.getSource();
        button.setStyle("-fx-border-width: 2; -fx-border-radius: 90; -fx-background-radius: 90; -fx-text-fill: white; -fx-border-color: white; -fx-background-color: black;");
        //button.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
    }
    public void newGameClicked(MouseEvent click) throws java.io.IOException
    {
        // load the next scene
         System.out.println(1);
        Parent GameScreenParent = FXMLLoader.load(getClass().getResource("GameScreen.fxml"));
        System.out.println(1);
        Scene GameScreenScene = new Scene(GameScreenParent);

        // get the stage... getSource = get object that was clicked on (the button) from the event, getScene = get the scene the button is a part of, getWindow = get the stage the scene is a part of
        System.out.println(1);
        Stage window = (Stage)((Node)click.getSource()).getScene().getWindow();

        // set stage to display the next scene
        System.out.println(1);
        window.setScene(GameScreenScene);
        System.out.println(1);

        window.show();
    }
}