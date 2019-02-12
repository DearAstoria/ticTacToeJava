package cs4b.proj1;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.image.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;

public class Board {

    GridPane board;
    StackPane [][] location;


    Board(Game game){
        board = new GridPane();
        location = new StackPane[3][3];
        Rectangle rectangle;
        board.setAlignment(Pos.CENTER);


        for(int i=0;i<3;++i)
            for(int j=0;j<3;++j){
                rectangle = new Rectangle(100,100, Paint.valueOf("BLACK"));
                rectangle.setStroke(Paint.valueOf("WHITE"));
                location[i][j] = new StackPane(rectangle);
                board.add(location[i][j],j,i,1,1);
                if(!game.isNewGame && game.board[i][j] != 0)
                        location[i][j].getChildren().add(Icon.toIcon(game.board[i][j]).newImageView());

            }
    }

    void show(){
        Stage stage = new Stage();
        Scene scene = new Scene(board, 600, 600);
        stage.setScene(scene);
        stage.show();

    }
    public void removeGameControls() {  // when game is finished, this function removes the handlers for inserting more pieces on the board
        for(int i=0;i<3;i++)
            for(int j=0;j<3;++j)
                location[i][j].setOnMouseClicked(null);
    }
}
