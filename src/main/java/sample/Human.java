package sample;
import java.io.*;

public class Human extends Player implements Serializable{

    Human(){ this("player");}
    Human(String str){
        if((name = str).equals(""))
            name = str + "player";
    }
}
