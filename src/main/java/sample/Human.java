package sample;

public class Human extends Player {

    Human(){ this("player");}
    Human(String str){
        if((name = str).equals(""))
            name = str + "player";
    }
}
