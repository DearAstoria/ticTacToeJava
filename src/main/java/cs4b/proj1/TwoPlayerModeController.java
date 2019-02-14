package cs4b.proj1;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class TwoPlayerModeController {

    ComboBox<String> firstMover;
    TextField playerX, playerO;
    Button begin, back;




    void start(){

        // LOAD FXML

        firstMover.getItems().addAll("X goes first", "O goes first");
        firstMover.getSelectionModel().selectFirst();



    }
    void begin(){
        //primary.close();
        Game game = new Game(playerX.getText(), playerO.getText(), Icon.toIcon(firstMover.getValue().charAt(0)));
        game.play();

    }

    void back(){


    }
}
