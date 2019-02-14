package cs4b.proj1;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class SingleModeController {

    ComboBox<String> firstMover, humanTeam, cpuMode;
    TextField        humanName;
    Button           back, begin;




    void start(){
        // LOAD FXML

        firstMover.getItems().addAll("X goes first", "O goes first");
        firstMover.getSelectionModel().selectFirst();
        cpuMode.getItems().addAll("easy", "hard");
        cpuMode.getSelectionModel().selectFirst();
        humanTeam.getItems().addAll("X", "O");
        humanTeam.getSelectionModel().selectFirst();

    }

    void begin(){
        //primary.close();
        Game game = new Game(humanName.getText(), Icon.toIcon(humanTeam.getValue().charAt(0)), Icon.toIcon(firstMover.getValue().charAt(0)));
        game.play();


    }

    void back(){


    }
}
