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

public class GameController
{
    private GameState game = new GameState(); // the state of the game: board spaces, and who's turn it is
 //   private OpponentAI opponent = new OponnentAI("easy"); //
    public Button RESTART; // a prompt to restart the game once it is over (as defined in GameScreen.fxml)

    // highlight the space the mouse is over with the player's symbol
    public void mouseOver(MouseEvent event)
    {
        Pane space = (Pane)event.getSource(); // get the board space that detected this event

        // determine if the space should be highlighted, and what symbol should be used
        if(space.getChildren().isEmpty()) // if this space is empty then add a highlight of the player's token
        {
            if(game.getTurn()) // if it is the player's turn add a X
            {
                // Create a X
                Text x = new Text("X");
                x.setTextAlignment(TextAlignment.CENTER);
                x.setFont(new Font(64));
                x.setOpacity(0.25);
                x.setId("Temporary");

                // Put the X in this space
                space.getChildren().add(x);
            }
            else // else add an O
            {
                // Create an O
                Text o = new Text("O");
                o.setTextAlignment(TextAlignment.CENTER);
                o.setFont(new Font(64));
                o.setOpacity(0.25);
                o.setId("Temporary");

                // Put the O in this space
                space.getChildren().add(o);
            }
        }
    }

    // Clear the highlighted symbol if the mouse moves out of a space
    public void mouseOut(MouseEvent event)
    {
        Pane space = (Pane)event.getSource(); // get the board space that detected this event

        if(space.getChildren().get(0).getId() == "Temporary") // if the symbol on the space was temporary when the mouse was moved off of it
        {
            space.getChildren().remove(0); // then remove that symbol
        }
    }

    public void onClick(MouseEvent event) throws java.io.IOException
    {
        Pane space = (Pane)event.getSource(); // get the board space that detected this event
        Text token = (Text)space.getChildren().get(0); // get the symbol in this space (because mouseOver event is guaranteed to occur first, we can be sure the space is not empty when calling it's children)

        if(token.getId() != "Permanent") // if the text object is not permanently on the space (the space has not been taken yet), then then take this space
        {
            // set properties so that the text generated in the mouseOver event opaque and permanent on the space
            token.setOpacity(1);
            token.setId("Permanent");

            // change playerTurn so that it is the next player's turn
            game.changeTurn();

            // update the raw board data (0 == free space, 1 == O, 2 == X)
            switch(space.getId())
            {
                case "cell00":
                    game.set(game.getTurn() ? 1 : 2, 0, 0); // if O's turn, put an O at (0,0) else put an X at (0,0)
                    break;
                case "cell01":
                    game.set(game.getTurn() ? 1 : 2, 1, 0);
                    break;
                case "cell02":
                    game.set(game.getTurn() ? 1 : 2, 2, 0);
                    break;
                case "cell10":
                    game.set(game.getTurn() ? 1 : 2, 0, 1);
                    break;
                case "cell11":
                    game.set(game.getTurn() ? 1 : 2, 1, 1);
                    break;
                case "cell12":
                    game.set(game.getTurn() ? 1 : 2, 2, 1);
                    break;
                case "cell20":
                    game.set(game.getTurn() ? 1 : 2, 0, 2);
                    break;
                case "cell21":
                    game.set(game.getTurn() ? 1 : 2, 1, 2);
                    break;
                case "cell22":
                    game.set(game.getTurn() ? 1 : 2, 2, 2);
                    break;
            }

            // Compare the game board and look for a win
            switch(game.checkForWin())
            {
                case -1: // draw
                    outputWinner("Draw"); // return to main menu
                    break;
                case 0:
                    // do nothing, game is not over, there are still empty spaces on the board
                    // if playing against a computer player calculate the computer's next move here and change
                    // the playerTurn again so that it is the Human player's turn again
                    break;
                case 1: // O win
                    outputWinner("O wins"); // return to main menu
                    // highlight winning group
                    break;
                case 2: // X wins
                    outputWinner("X wins"); // return to main menu
                    // highlight winning group
                    break;
            }
        }
    }

    // reveals hidden button in this scene that displays who won the game and prompts the user to click it to restart
    private void outputWinner(String winningMessage)
    {
        // overwrite initial properties on the button which made it hidden
        RESTART.setText(winningMessage);    // replace initial message with an end game message
        RESTART.setOpacity(1);              // make the hidden button visible
        RESTART.setMouseTransparent(false); // make false so that the hidden button can detect mouse events again
    }

    public void restart(ActionEvent click) throws java.io.IOException
    {
        // load the next scene
        Parent GameScreenParent = FXMLLoader.load(getClass().getResource("StartupMenu.fxml"));
        Scene GameScreenScene = new Scene(GameScreenParent);

        // get the stage... getSource = get object that was clicked on (the button) from the event, getScene = get the scene the button is a part of, getWindow = get the stage the scene is a part of
        Stage window = (Stage)((Node)click.getSource()).getScene().getWindow();

        // set stage to display the next scene
        window.setScene(GameScreenScene);
        window.show();
    }
}
