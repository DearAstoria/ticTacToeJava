package cs4b.proj1;

import javafx.scene.image.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;


public abstract class Icon implements Serializable {
    abstract char icon();   // purpose: to avoid the use of char literals when constructing Player objects
    static Icon toIcon(char arg1){
        if(arg1 == Xchar)
            return X_obj;
        if(arg1 == Ochar)
            return  O_obj;
        else return new X(); // error: X/Y are the only acceptable types of Icon
    }
    public ImageView newImageView(){
        try{ return new ImageView(new Image(new FileInputStream(path()),90,90,false,false)); }
        catch (Exception e){ return null;}
    }
    public ImageView newImageView(int length){  // for choosing size
        try{ return new ImageView(new Image(new FileInputStream(path()),length,length,false,false)); }
        catch (Exception e){ return null;}
    }

    static X X_obj = new X();
    static O O_obj = new O();

    abstract String path();

    static char Xchar = 'X';
    static char Ochar = 'O';
    static String Xpath = "src/graphics/X.png";
    static String Opath = "src/graphics/O.png";

}

class X extends Icon {
    @Override char icon() { return Xchar; }
    @Override String path(){ return Xpath; }

}
class O extends Icon {
    @Override char icon() { return Ochar; }
    @Override String path(){ return Opath; }
}
