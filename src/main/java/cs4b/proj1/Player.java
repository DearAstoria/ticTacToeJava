package sample;

import javafx.scene.image.Image;

import java.io.FileInputStream;

public class Player {

    Player opponent;
    boolean myTurn;
    Image iconImage;
    private char icon;
    private String name;
    protected Board board;
    protected Game game;

    public Player(String name, Icon iconParam, Game g) {
        game = g;
        this.name = name;
        this.icon = iconParam.icon();
        if(icon == Icon.Xchar){
            try{iconImage = new Image(new FileInputStream("src/graphics/X.png"),10,10,false,false);}
            catch(Exception e){e.printStackTrace();}    }
        else if(icon == Icon.Ochar){
        try{iconImage = new Image(new FileInputStream("src/graphics/O.png"),10,10,false,false);}
        catch(Exception e){e.printStackTrace();}    }
        else {} //    error must be X or Y
    }


    public char getIcon() { return icon; }
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

}