package cs4b.proj1;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.Serializable;

public class Player implements Serializable {

    Player opponent;
    boolean myTurn;
    transient Image iconImage;
    private Icon icon;
    private String name;
    transient protected Board board;
    protected Game game;

    public Player(String name, Icon iconParam, Game g) {
        game = g;
        this.name = name;
        icon = iconParam;
    }

    public void execute(int r, int c) {
        game.board[r][c] = getIcon();
        System.out.println(getIcon() + "  " + "[" + r + "," + c + "]");
        board.location[r][c].getChildren().add(icon.newImageView());
        game.current = opponent;
        try{                 Game.save(game);     }
        catch(Exception e){  e.printStackTrace(); }
        opponent.move();
    }

    public char getIcon() { return icon.icon(); }


    public Image getIconImage() {
        return iconImage;
    }

    void move() { } // virtual

    void setGame(Board board) { this.board = board; }
    void setMyTurn(){
        myTurn = true;
        opponent.myTurn = false;
    };
    void swapTurns(){
        myTurn = !myTurn;
        opponent.myTurn = !opponent.myTurn;
    }
    void setOpponent(Player enemy){ opponent = enemy; }

    void setUp() {
    }

    @Override
    public String toString(){
        String str = "";
        str += name;
        str += ", playing as ";
        return str += icon.icon();
    }

}