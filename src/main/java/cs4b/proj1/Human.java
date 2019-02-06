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

            /*for(int i=0; i<3; i++)
                for(int j=0; j<3;++j) {*/
            board.location[0][0].setOnMouseClicked(e -> { //int r = i, c = j; System.out.println(r + " " + c);
                if (!game.occupied(0, 0))
                    execute(0,0);   });
            board.location[0][1].setOnMouseClicked(e -> { //int r = i, c = j; System.out.println(r + " " + c);
                if (!game.occupied(0, 1))
                    execute(0,1);   });
            board.location[0][2].setOnMouseClicked(e -> { //int r = i, c = j; System.out.println(r + " " + c);
                if (!game.occupied(0, 2))
                    execute(0,2);   });
            board.location[1][0].setOnMouseClicked(e -> { //int r = i, c = j; System.out.println(r + " " + c);
                if (!game.occupied(1, 0))
                    execute(1,0);   });
            board.location[1][1].setOnMouseClicked(e -> { //int r = i, c = j; System.out.println(r + " " + c);
                if (!game.occupied(1, 1))
                    execute(1,1);   });
            board.location[1][2].setOnMouseClicked(e -> { //int r = i, c = j; System.out.println(r + " " + c);
                if (!game.occupied(1, 2))
                    execute(1,2);   });
            board.location[2][0].setOnMouseClicked(e -> { //int r = i, c = j; System.out.println(r + " " + c);
                if (!game.occupied(2, 0))
                    execute(2,0);   });
            board.location[2][1].setOnMouseClicked(e -> { //int r = i, c = j; System.out.println(r + " " + c);
                if (!game.occupied(2, 1))
                    execute(2,1);   });
            board.location[2][2].setOnMouseClicked(e -> { //int r = i, c = j; System.out.println(r + " " + c);
                if (!game.occupied(2, 2))
                    execute(2,2);   });
               // }
           /* board.location[0][1].setOnMouseClicked(e -> {
                if (!game.occupied(0, 1)) {
                    game.board[0][1] = getIcon();System.out.println(getIcon() + "  " + "[0,1]");
                    board.location[0][1].getChildren().add(new ImageView(iconImage));
                    opponent.move();
                }
            });
            board.location[0][2].setOnMouseClicked(e -> {
                if (!game.occupied(0, 2)) {
                    game.board[0][2] = getIcon();System.out.println(getIcon() + "  " + "[0,2]");;
                    board.location[0][2].getChildren().add(new ImageView(iconImage));
                    opponent.move();

                }
            });
            board.location[1][0].setOnMouseClicked(e -> {
                if (!game.occupied(1, 0)) {
                    game.board[1][0] = getIcon();System.out.println(getIcon() + "  " + "[1,0]");;
                    board.location[1][0].getChildren().add(new ImageView(iconImage));
                    opponent.move();

                }
            });
            board.location[1][1].setOnMouseClicked(e -> {
                if (!game.occupied(1, 1)) {
                    game.board[1][1] = getIcon(); System.out.println(getIcon() + "  " + "[1,1]");;
                    board.location[1][1].getChildren().add(new ImageView(iconImage));
                    opponent.move();

                }
            });
            board.location[1][2].setOnMouseClicked(e -> {
                if (!game.occupied(1, 2)) {
                    game.board[1][2] = getIcon(); System.out.println(getIcon() + "  " + "[1,2]");;
                    board.location[1][2].getChildren().add(new ImageView(iconImage));
                    opponent.move();
                }
            });
            board.location[2][0].setOnMouseClicked(e -> {
                if (!game.occupied(2, 0)) {
                    game.board[2][0] = getIcon(); System.out.println(getIcon() + "  " + "[2,0]");
                   board.location[2][0].getChildren().add(new ImageView(iconImage));
                    opponent.move();
                }
            });
            board.location[2][1].setOnMouseClicked(e -> {
                if (!game.occupied(2, 1)) {
                    game.board[2][1] = getIcon(); System.out.println(getIcon() + "  " + "[2,1]");
                    board.location[2][1].getChildren().add(new ImageView(iconImage));
                    opponent.move();
                }
            });
            board.location[2][2].setOnMouseClicked(e -> {
                if(!game.occupied(2,2)) {
                game.board[2][2] = getIcon(); System.out.println(getIcon() + "  " + "[2,1]");;
                board.location[2][2].getChildren().add(new ImageView(iconImage));
                    opponent.move();
                 }


            });*/
        //}
    //return ret;

    }
    int[] newArray(int a, int b){
        int [] ret = {a,b};
        return ret;
    }
}
