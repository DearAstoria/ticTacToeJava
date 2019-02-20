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
    // indeces for current mover and winner   can only be 0 or 1 in a two player game
    public  int currentMover = 0;
    public int winnerIndex = -1;

    // parallel arrays   -  index with currentMover or winnerIndex above
    char [] pIcon  = {'x','o'};
    private Player [] p = new Player[2];
    //

    // lets OpponentAI access desired board space  - used in the AI's move methods
    private StackPane [][] location = new StackPane[3][3];

    private GameState game = new GameState(); // the state of the game: board spaces, and who's turn it is




    public GameController()
    {
        //init();
        //screen.setMouseTransparent(true);



        //this("justis", 'X', "easy", 'O');
        //initialize();
        //firstMove();
    }

    public void setTwoPlayerMode(String xPlayer, String oPlayer, char firstmover) {
        currentMover = firstmover == 'X' ? 0 : 1;
        p[0] = new Human(xPlayer);
        p[1] = new Human(oPlayer);
        xBox.setText(p[0].name);
        oBox.setText(p[1].name);
        init();
    }
    public void setSingleMode(String humanName, char playerChoice, String easyMode, char firstmover)  // Single Player Mode
    {
        //p = new Player[2];
        //location = new StackPane[3][3];
        System.out.println("GameController(" + humanName + ", " + playerChoice + ", " + easyMode + ", " + firstmover + ")..");
                currentMover = firstmover == 'X' ? 0 : 1;
                if(playerChoice == 'X'){
                    p[0] = new Human(humanName);
                    p[1] = new OpponentAI(easyMode);
                }
                else{
                    p[1] = new Human(humanName);
                    p[0] = new OpponentAI(easyMode);
                }

        xBox.setText(p[0].name);
        oBox.setText(p[1].name);
                System.out.println(p[0].name + " " + p[1].name);
                System.out.println(p[0] instanceof Human);
                System.out.println(currentMover);

        //screen.setMouseTransparent(false);
        init();
        firstMove();


    }
    //@FXML public void initialize()
      public void init(){

        System.out.println("@FXML initialize()..");

        location[0][0]  = cell00;
        location[0][1]  = cell01;
        location[0][2]  = cell02;
        location[1][0]  = cell10;
        location[1][1]  = cell11;
        location[1][2]  = cell12;
        location[2][0]  = cell20;
        location[2][1]  = cell21;
        location[2][2]  = cell22;

        //screen.setMouseTransparent(true);
        //firstMove();   // in case computer gets the first move

    }

    void firstMove(){ //System.out.println("firstMove()");  // debug
        if(p[currentMover] instanceof OpponentAI)           // if mover is CPU, call it's nextMove()
            ((OpponentAI) p[currentMover]).nextMove(game, this);
    }


    public void mouseOver(MouseEvent event)
    {
        if(game.isOver() || (p[currentMover] instanceof OpponentAI))
            return;
        Pane space = (Pane)event.getSource();

        if(space.getChildren().isEmpty())
        {
            // Create a icon
            Text icon = new Text(Character.toString(pIcon[currentMover]));
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
        if(game.isOver() || (p[currentMover] instanceof OpponentAI))
            return;
        Pane space = (Pane)event.getSource(); // get the board space that detected this event

        if(space.getChildren().get(0).getId() == "Temporary") // if the symbol on the space was temporary when the mouse was moved off of it
        {
            space.getChildren().remove(0); // then remove that symbol
        }
    }


    public void onClick(MouseEvent event) throws java.io.IOException
    {
        if(game.isOver()) return;
        if(p[currentMover] instanceof OpponentAI) {
            return;
        }

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
            game.set(pIcon[currentMover], x, y);
            //

            changeTurn();
        }
    }

    // reveals hidden button in this scene that displays who won the game and prompts the user to click it to restart
    private void outputWinner(/*String winningMessage*/)
    {
        game.setGameOver();
        // overwrite initial properties on the button which made it hidden


        RESTART.setText(winnerIndex == -1 ? "Draw" : Character.toString(pIcon[winnerIndex]) + ", " + p[winnerIndex].name + " wins");    // replace initial message with an end game message
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


    public StackPane atLocation(int x, int y){   //used in OpponentAI.randomMove()

        return location[x][y];

    }


    public void  changeTurn(){     // called each time a player has made a move
        updateWinner();
        currentMover = (++currentMover % 2);
        if(p[currentMover] instanceof OpponentAI)
            ((OpponentAI)p[currentMover]).nextMove(game, this);
    }


    public int playerOf(char c){ //returns index associated with the a players symbol;  used by updateWinner()

        return  c == 'x' ? 0 : 1;

    }

    public void updateWinner() {
        for (int x = 0; x < 3; x++) // check all horizontal groups
        {
            // if there is a horizontal match AND that match is not empty spaces (empty space = 0)
            if (game.boardSpaces[0][x] != 0 && game.boardSpaces[0][x] == game.boardSpaces[1][x] && game.boardSpaces[1][x] == game.boardSpaces[2][x]) {
                winnerIndex = playerOf(game.boardSpaces[0][x]); // then return the winning token
                outputWinner();
                return;
            }
        }

        for (int y = 0; y < 3; y++) // check all vertical groups
        {
            // if there is a vertical match AND that match is not empty spaces
            if (game.boardSpaces[y][0] != 0 && game.boardSpaces[y][0] == game.boardSpaces[y][1] && game.boardSpaces[y][1] == game.boardSpaces[y][2]) {
                winnerIndex = playerOf(game.boardSpaces[y][0]); // then return the winning token
                outputWinner();
                return;
            }
        }

        if (game.boardSpaces[0][0] != 0 && game.boardSpaces[0][0] == game.boardSpaces[1][1] && game.boardSpaces[1][1] == game.boardSpaces[2][2]) // check both diagonal groups
        {
            winnerIndex = playerOf(game.boardSpaces[0][0]);
            outputWinner();
            return;
        }
        else if (game.boardSpaces[2][0] != 0 && game.boardSpaces[2][0] == game.boardSpaces[1][1] && game.boardSpaces[1][1] == game.boardSpaces[0][2]) {
                  winnerIndex = playerOf(game.boardSpaces[2][0]);
                  outputWinner();
                  return;
        }

        for(int i=0; i<3;++i)
            for(int j=0; j<3; ++j)
                if(!game.occupied(i,j))
                    return;
         outputWinner();




    }
    //public char playerToken(Player p) {return (p == xPlayer) ? 'x' : 'o';}
}
