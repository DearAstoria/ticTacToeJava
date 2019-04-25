package server.databaseOperations;

public class SetupDB {

    public static void main(String args[]){
        String DB = "tictactoe";

        //PostgresqlExample.createDatabase(DB);
        PostgresqlExample.createTable(DB, "CREATE TABLE USERS " +
                " (EMAIL VARCHAR(255) NOT NULL," +
                " USERNAME VARCHAR(255) NOT NULL, " +
                " PASSWORD VARCHAR(255) NOT NULL," +
                " WINS INTEGER DEFAULT 0," +
                " LOSSES INTEGER DEFAULT 0, " +
                " DRAWS INTEGER DEFAULT 0," +
                " SCORE INTEGER DEFAULT 0," +
                "PRIMARY KEY (EMAIL, USERNAME) )");




    }
}