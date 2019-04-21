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
    @Override
    public String toString(){
        return new String(email + " " + username + " " + password);
    }

}
