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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.text.FontWeight;

public class GameController
{


    @FXML  public StackPane cell00, cell01, cell02, cell10, cell11, cell12, cell20, cell21, cell22;
    @FXML  public Button RESTART; // a prompt to restart the game once it is over (as defined in GameScreen.fxml)
    @FXML  public BorderPane screen;
    @FXML  public Text xBox, oBox;
    public Text [] playerDisplay;


    public GameState game;// = new GameState(); // the state of the game: board spaces, and who's turn it is




    public GameController() { }



    //@FXML public void initialize()
      public void init(GameState gamestate){


        game.location[0][0]  = cell00;
        game.location[0][1]  = cell01;
        game.location[0][2]  = cell02;
        game.location[1][0]  = cell10;
        game.location[1][1]  = cell11;
        game.location[1][2]  = cell12;
        game.location[2][0]  = cell20;
        game.location[2][1]  = cell21;
        game.location[2][2]  = cell22;


        xBox.setText(game.p[0].name + " (" + game.pIcon[0] + ")");
        oBox.setText(game.p[1].name + " (" + game.pIcon[1] + ")");
        playerDisplay = new Text[2];
        playerDisplay[0] = xBox;
        playerDisplay[1] = oBox;
        playerDisplay[(gamestate.currentMover + 1) % 2].setOpacity(.5);

          Text t;
        if(!gamestate.isNewGame)
            for(int i = 0; i<3; ++i)
                for(int j=0; j<3; ++j){
                    if(gamestate.boardSpaces[i][j] != 0)
                    {
                        gamestate.atLocation(i,j).getChildren().add(new Text(Character.toString(gamestate.boardSpaces[i][j])));
                        ((Text)gamestate.atLocation(i,j).getChildren().get(0)).setStyle("-fx-font: 64 System;");
                        gamestate.atLocation(i,j).setId("Permanent");
                    }
                }


    }




    public void mouseOver(MouseEvent event)
    {
        if(game.isOver() || (game.p[game.currentMover] instanceof OpponentAI))
            return;
        Pane space = (Pane)event.getSource();

        if(space.getChildren().isEmpty())
        {
            // Create a icon
            Text icon = new Text(Character.toString(game.pIcon[game.currentMover]));
            icon.setTextAlignment(TextAlignment.CENTER);
            icon.setFont(new Font(64));
            icon.setOpacity(0.25);
            icon.setId("Temporary");

            // Put the icon in this space
            space.getChildren().add(icon);
        }
    }

    // Clear the highlighted symbol if the mouse moves out of a space
    public void mouseOut(MouseEvent event)
    {
        if(game.isOver() || (game.p[game.currentMover] instanceof OpponentAI))
            return;
        Pane space = (Pane)event.getSource(); // get the board space that detected this event

        if(space.getChildren().get(0).getId() == "Temporary") // if the symbol on the space was temporary when the mouse was moved off of it
        {
            space.getChildren().remove(0); // then remove that symbol
        }
    }


    public void onClick(MouseEvent event) throws java.io.IOException
    {
        if(game.isOver() || game.p[game.currentMover] instanceof OpponentAI)
            return;


        Pane space = (Pane)event.getSource(); // get the board space that detected this event
        Text token = (Text)space.getChildren().get(0); // get the symbol in this space (because mouseOver event is guaranteed to occur first, we can be sure the space is not empty when calling it's children)

        if(token.getId() != "Permanent") // if the text object is not permanently on the space (the space has not been taken yet), then then take this space
        {

            // set properties so that the text generated in the mouseOver event opaque and permanent on the space
            token.setOpacity(1);
            token.setId("Permanent");

            //
            int x,y;
            x = GridPane.getRowIndex(space) != null ? GridPane.getRowIndex(space) : 0;
            y = GridPane.getColumnIndex(space) != null ? GridPane.getColumnIndex(space) : 0;
            game.set(game.pIcon[game.currentMover], x, y);
            //

            game.changeTurn();
        }
    }

    // reveals hidden button in this scene that displays who won the game and prompts the user to click it to restart
    public void outputWinner(/*String winningMessage*/)
    {
        game.setGameOver();
        game.savedGame.delete();
        // overwrite initial properties on the button which made it hidden


        RESTART.setText(game.winnerIndex == -1 ? "Draw" : playerDisplay[game.winnerIndex].getText() + " wins");    // replace initial message with an end game message
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
    public void exit(MouseEvent click) throws java.io.IOException
    {
        Parent GameScreenParent = FXMLLoader.load(getClass().getResource("StartupMenu.fxml"));
        Scene GameScreenScene = new Scene(GameScreenParent);
        Stage window = (Stage)((Node)click.getSource()).getScene().getWindow();
        window.setScene(GameScreenScene);
        window.show();
    }







    }
    //public char playerToken(Player p) {return (p == xPlayer) ? 'x' : 'o';}

