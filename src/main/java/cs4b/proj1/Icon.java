package cs4b.proj1;

public class Icon {
          char icon(){return 0;}   // purpose: to avoid the use of char literals when constructing Player objects
    static Icon toIcon(char arg1){
        if(arg1 == Xchar)
            return new X();
        if(arg1 == Ochar)
            return  new O();
        else return new X(); // error: X/Y are the only acceptable types of Icon
    }
    static char Xchar = 'X';
    static char Ochar = 'O';
}

class X extends Icon { @Override char icon() { return Xchar; } }
class O extends Icon { @Override char icon() { return Ochar; } }
