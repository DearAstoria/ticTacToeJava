package client;

public class User {
    private String email;
    private String username;
    private String password;

    public User(String emailAddress, String name, String passcode){
        email = emailAddress;
        username = name;
        password = passcode;
    }
    public String getEmail(){return email;}
    public String getUsername(){return username;}
    public String getPassword(){return password;}
    public boolean addToDatabase(){ return true; }  // returns false if username or email is already in use
    public boolean updateGames(){ return true;}    //  returns false if record doesn't exist

    @Override
    public String toString(){
        return new String(email + " " + username + " " + password);
    }

}
