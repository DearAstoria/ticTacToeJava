package client.gui_controllers;
import client.raw_data.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

public class GameSettingsMenuController implements Initializable {
    GameSettings form; // the form that the user fills out to choose game settings

    @FXML public TextField NAME;
    @FXML public ToggleGroup DIFFICULTY;
    @FXML public ToggleButton EASY, HARD;
    @FXML public ToggleGroup CHOICEOFLETTER;
    @FXML public ToggleButton PLAYX, PLAYO;
    @FXML public ToggleGroup FIRSTMOVE;
    @FXML public ToggleButton XFIRST, OFIRST;

    @Override
    public void initialize(URL loacation, ResourceBundle resources) {
        form = new GameSettings(); // default constructor creates a form initialized for an easy opponent where the player is X and goes first
        EASY.setSelected(true); // difficulty is set to easy by default in the GUI
        PLAYX.setSelected(true); // play as X by default in the GUI
        XFIRST.setSelected(true); // X moves first by default in the GUI
        update(); // update the GUI to reflect the changes above
    }

    // called when the EASY or HARD buttons are clicked
    public void difficultySelected(MouseEvent click) {
        ToggleButton clicked = (ToggleButton)DIFFICULTY.getSelectedToggle();  // get the selected button in the toggle group
        boolean difficulty = clicked.getText().equals("Easy") ? true : false; // is the difficulty selected to easy?
        form.setEasy(difficulty);                                             // set the difficulty in the form
        update();                                                             // update the GUI to reflect changes to the form
    }

    // called when the X or O buttons for player's choice of letter are clicked
    public void playerSymbolSelected(MouseEvent click) {
        ToggleButton clicked = (ToggleButton)CHOICEOFLETTER.getSelectedToggle(); // get the selected button in the toggle group
        boolean playsX = clicked.getText().equals("X") ? true : false;           // does the user want to play as X?
        form.setPlayingX(playsX);                                                // set the player symbol in the form
        update();                                                                // update the GUI to reflect changes to the form
    }

    // called when the X or O buttons for which player moves first is clicked
    public void firstMoverSelected(MouseEvent click) {
        ToggleButton clicked = (ToggleButton)FIRSTMOVE.getSelectedToggle(); // get the selected button in the toggle group
        boolean xFirst = clicked.getText().equals("X") ? true : false;      // is X playing first?
        form.setxFirst(xFirst);                                             // set whether X goes first or not
        update();                                                           // update the GUI to reflect changes to the form
    }

    public void back(MouseEvent click) throws java.io.IOException {
        // load the next scene
        Parent GameScreenParent = FXMLLoader.load(getClass().getResource("/client/gui_controllers/gui_controllers/GameModeMenu.fxml"));
        Scene GameScreenScene = new Scene(GameScreenParent);

        // get the stage... getSource: get object that was clicked on (the button) from the event, getScene: get the scene the button is a part of, getWindow: get the stage the scene is a part of
        Stage window = (Stage)((Node)click.getSource()).getScene().getWindow();

        // set stage to display the next scene
        window.setScene(GameScreenScene);

        window.show();
    }

    public void beginClicked(MouseEvent click) throws java.io.IOException {
        GameState newGame = generateGame(form);
        GameController controller= new GameController(newGame, form);

        // load the next scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/gui_controllers/gui_controllers/GameScreen.fxml")); // initialize FXMLLoader to load the next UI
        loader.setController(controller); // initialize the FXMLLoader to tell the UI to use the controller initialized from parameters in this object
        Scene GameScreenScene = new Scene(loader.load());

        // get reference to the current stage
        Stage window = (Stage)((Node)click.getSource()).getScene().getWindow();

        // set current stage to display the next scene
        window.setScene(GameScreenScene);

        window.show();
    }

    // update the GUI to restrict un-clicking toggle buttons in each toggle group
    public void update() {
        if(EASY.isSelected())
        {
            EASY.setMouseTransparent(true); // set so that easy cannot be clicked (un-clicked) when it is selected
            HARD.setMouseTransparent(false); // reset so that hard can be clicked again
        }
        else
        {
            HARD.setMouseTransparent(true); // set so that hard cannot be clicked (un-clicked) when it is selected
            EASY.setMouseTransparent(false); // reset so that easy can be clicked again
        }

        if(PLAYX.isSelected())
        {
            PLAYX.setMouseTransparent(true);
            PLAYO.setMouseTransparent(false);
        }
        else
        {
            PLAYO.setMouseTransparent(true);
            PLAYX.setMouseTransparent(false);
        }

        if(XFIRST.isSelected())
        {
            XFIRST.setMouseTransparent(true);
            OFIRST.setMouseTransparent(false);
        }
        else
        {
            OFIRST.setMouseTransparent(true);
            XFIRST.setMouseTransparent(false);
        }
    }

    // create a new game state based off the game settings the user selected in the form
    private GameState generateGame(GameSettings settings)
    {
        form.setPlayer(NAME.getText());
        return new GameState(settings.isxFirst() ? 'X' : 'O');
    }
}
