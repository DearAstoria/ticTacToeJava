package cs4b.proj1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.*;

public class Main extends Application {
    private Scene mainMenu;
    private Scene playerMode;


    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
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
        ComboBox<String> cpuMode = new ComboBox<>();  // like ChoiceBox, but does not need listener, you can use setOnAction
        cpuMode.getItems().addAll("easy", "hard");
        cpuSetting.getChildren().addAll(cpuLable, cpuMode);


        HBox humanSetting = new HBox();
        Label humanLabel = new Label("Human plays:  ");
        ComboBox<String> humanTeam = new ComboBox<>();
        humanTeam.getItems().addAll("X", "O");
        Button begin = new Button("begin");
        humanSetting.getChildren().addAll(humanLabel, humanTeam);
        TextField humanName = new TextField("Player1");



        ComboBox<String> firstMover = new ComboBox<>();
        firstMover.getItems().addAll("X goes first", "O goes first");

        menu.getChildren().addAll(cpuSetting, humanSetting, humanName, firstMover, begin);


        begin.setOnMouseClicked(e ->{
            primary.close();
            Game game = new Game(humanName.getText(), Icon.toIcon(humanTeam.getValue().charAt(0)), Icon.toIcon(firstMover.getValue().charAt(0)));
            game.play();
        });

        Scene scene = new Scene(menu, 600, 600);
        primary.setScene(scene);
        primary.show();


    }
    private void twoPlayerMode(Stage primary){
        BorderPane parent = new BorderPane();
        ComboBox<String> firstMover = new ComboBox<>();
        firstMover.getItems().addAll("X goes first", "O goes first");
        TextField player1 = new TextField("Player1");
        TextField player2 = new TextField("Player2");
        Button begin = new Button("begin");
        parent.setTop(firstMover);
        parent.setLeft(player1);
        parent.setRight(player2);
        parent.setBottom(begin);

        begin.setOnMouseClicked(e ->{ primary.close();
                                      Game game = new Game(player1.getText(), player2.getText(), Icon.toIcon(firstMover.getValue().charAt(0)));
                                      game.play();

        });


        Scene scene = new Scene(parent, 600, 600);
        primary.setScene(scene);
        primary.show();





    }
}
