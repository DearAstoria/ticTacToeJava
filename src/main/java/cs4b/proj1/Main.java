package cs4b.proj1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.control.*;

import java.io.*;
import java.util.Optional;

    public class Main extends Application {
    private Scene mainMenu;
    private Scene playerMode;


    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));


        if(checkForSavedGame())
            return;





        // back to main menu




        primaryStage.setTitle("Hello World");

        Button newGame = new Button("New Game");
        StackPane menuPane = new StackPane(newGame);
        mainMenu = new Scene(menuPane, 600, 600);
        newGame.setOnMouseClicked(e -> { playerMode(primaryStage); });


        primaryStage.setScene(mainMenu);
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }

    private void playerMode(Stage primary){
        primary.setTitle("player mode select");

        HBox options = new HBox();
        Button singleMode = new Button("single-player mode");
        Button twoPlayer = new Button("two-player mode");
        Button back = new Button("back");
        options.getChildren().addAll(singleMode, twoPlayer);

        options.getChildren().get(0).setOnMouseClicked(e -> { singleMode(primary); } );
        options.getChildren().get(1).setOnMouseClicked(e -> { twoPlayerMode(primary); } );

        BorderPane borderPane = new BorderPane(options, null, null, back, null);
        mainMenu = new Scene(borderPane, 600, 600);

        back.setOnMouseClicked(e -> { try{ start(primary);}
                                        catch (Exception ex ){ ex.printStackTrace();  }} );

        options.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(back, Pos.BOTTOM_CENTER);
        primary.setScene(mainMenu);
        primary.show();
    }
    private void singleMode(Stage primary){

        VBox menu = new VBox();

        HBox cpuSetting = new HBox();
        Label cpuLable = new Label("CPU mode:  ");
        ComboBox<String> cpuMode = new ComboBox<>();
        cpuMode.getItems().addAll("easy", "hard");
        cpuMode.getSelectionModel().selectFirst();
        cpuSetting.getChildren().addAll(cpuLable, cpuMode);
        //cpuSetting.setAlignment(Pos.CENTER);




        ComboBox<String> humanTeam = new ComboBox<>();
        humanTeam.getItems().addAll("X", "O");
        humanTeam.getSelectionModel().selectFirst();
        Button begin = new Button("begin");
                begin.setPrefSize(100,50);
        TextField humanName = new TextField("Player1");
        HBox nameSetting = new HBox(new Label("Player Name:  "), humanName);
             //nameSetting.setAlignment(Pos.CENTER);
        HBox playerIcon = new HBox(new Label("plays as:  "), humanTeam);
             //playerIcon.setAlignment(Pos.CENTER);
        Button back = new Button("back");
        back.setAlignment(Pos.BOTTOM_LEFT);
        menu.setSpacing(50);



        ComboBox<String> firstMover = new ComboBox<>();
        firstMover.getItems().addAll("X goes first", "O goes first");
        firstMover.getSelectionModel().selectFirst();

        menu.getChildren().addAll(cpuSetting, nameSetting, playerIcon, firstMover, begin, back);
        //menu.setAlignment(Pos.CENTER);
        HBox master = new HBox(menu);
        master.setAlignment(Pos.CENTER);
        //master.setTranslateY(50);


        begin.setOnMouseClicked(e ->{
            primary.close();
            Game game = new Game(humanName.getText(), Icon.toIcon(humanTeam.getValue().charAt(0)), Icon.toIcon(firstMover.getValue().charAt(0)));
            game.play();
        });

        back.setOnMouseClicked(e -> { try{ playerMode(primary);}
                                      catch (Exception ex ){ ex.printStackTrace();  }} );

        Scene scene = new Scene(master, 600, 600);
        primary.setScene(scene);
        primary.show();

    }
    private void twoPlayerMode(Stage primary){
        BorderPane parent = new BorderPane();
        ComboBox<String> firstMover = new ComboBox<>();
        firstMover.getItems().addAll("X goes first", "O goes first");
        firstMover.getSelectionModel().selectFirst();
        TextField player1 = new TextField("Player1");
        TextField player2 = new TextField("Player2");
        Button begin = new Button("begin");
        Button back = new Button("back");
        parent.setTop(firstMover);
        parent.setLeft(player1);
        parent.setRight(player2);
        parent.setBottom(new HBox(begin,back));

        begin.setOnMouseClicked(e ->{ primary.close();
                                      Game game = new Game(player1.getText(), player2.getText(), Icon.toIcon(firstMover.getValue().charAt(0)));
                                      game.play();

        });

        back.setOnMouseClicked(e -> { try{ playerMode(primary);}
                                      catch (Exception ex ){ ex.printStackTrace();  }} );


        Scene scene = new Scene(parent, 600, 600);
        primary.setScene(scene);
        primary.show();





    }

    public boolean checkForSavedGame() throws Exception{
        if (Game.savedGame.isFile()) {
            //System.out.println(Game.savedGame.isFile());
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("restore game");
            alert.setHeaderText("The previous game was not finished.");
            alert.setContentText("Would you like to restore it?");

            ButtonType buttonTypeOne = new ButtonType("Yes");
            ButtonType buttonTypeCancel = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeOne) {
                // restore game
                Game.restore().play();
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
}
