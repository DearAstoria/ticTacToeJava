package cs4b.proj1;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class Human extends Player {
    Human(String name, Icon iconParam, Game gameAssigned){
        super(name, iconParam,  gameAssigned); }


        @Override
        void move(){
        if(game.gameIsOver()) {
            board.removeGameControls();
            return;
        }

            board.location[0][0].setOnMouseClicked(e -> {
                if (!game.occupied(0, 0))
                    execute(0,0);   });
            board.location[0][1].setOnMouseClicked(e -> {
                if (!game.occupied(0, 1))
                    execute(0,1);   });
            board.location[0][2].setOnMouseClicked(e -> {
                if (!game.occupied(0, 2))
                    execute(0,2);   });
            board.location[1][0].setOnMouseClicked(e -> {
                if (!game.occupied(1, 0))
                    execute(1,0);   });
            board.location[1][1].setOnMouseClicked(e -> {
                if (!game.occupied(1, 1))
                    execute(1,1);   });
            board.location[1][2].setOnMouseClicked(e -> {
                if (!game.occupied(1, 2))
                    execute(1,2);   });
            board.location[2][0].setOnMouseClicked(e -> {
                if (!game.occupied(2, 0))
                    execute(2,0);   });
            board.location[2][1].setOnMouseClicked(e -> {
                if (!game.occupied(2, 1))
                    execute(2,1);   });
            board.location[2][2].setOnMouseClicked(e -> {
                if (!game.occupied(2, 2))
                    execute(2,2);   });
    }

}
