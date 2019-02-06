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

import java.io.FileInputStream;
import java.io.Serializable;

public class Board implements Serializable {

    GridPane board;
    StackPane [][] location;


    Board(){
        board = new GridPane();
        location = new StackPane[3][3];
        Rectangle rectangle;
        board.setAlignment(Pos.CENTER);
        for(int i=0;i<3;++i)
            for(int j=0;j<3;++j){
                rectangle = new Rectangle(100,100, Paint.valueOf("BEIGE"));
                rectangle.setStroke(Paint.valueOf("BLACK"));
                location[i][j] = new StackPane(rectangle);
                board.add(location[i][j],j,i,1,1);
            }
    }

    void show(Player[] players, int current){
        Stage stage = new Stage();


        Scene scene = new Scene(board, 600, 600);
        stage.setScene(scene);
        stage.show();

    }
    public void removeGameControls() {
        for(int i=0;i<3;i++)
            for(int j=0;j<3;++j)
                location[i][j].setOnMouseClicked(null);
    }
}
