package server.databaseOperations;
import java.sql.*;
public class DBOperations {

    // driver and database url
    public static final String DB_URL = "jdbc:postgresql://localhost/";   // base url of rdbms server
    public static final String driver = "org.postgresql.Driver";
    public static final String tictactoe = "jdbc:postgresql://localhost/tictactoe"; // url of tictactoe database


    //  Database credentials
    public static final String USER = "my name";
    public static final String PASS = "my password";

    // database tables
    public static final String USERS = "USERS";
    public static final String GAMES = "games";
    public static final String MOVES = "MOVES";


    public static void main(String args[]) throws Exception {  // sets up the database
        Connection conn = null;
        Statement stmt = null;
        try{
            //STEP 2: Register JDBC driver
            Class.forName(driver);

            //STEP 3: Open a connection
            System.out.println("Connecting to postgres");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connected");



            stmt = conn.createStatement();
            stmt.execute("CREATE DATABASE tictactoe");
            System.out.println("created tictactoe database");
            conn.close();
            stmt.close();

            conn = DriverManager.getConnection(tictactoe, USER, PASS);
            stmt = conn.createStatement();
            stmt.executeUpdate("CREATE TABLE USERS " +
                    " (EMAIL VARCHAR(255) NOT NULL," +
                    " USERNAME VARCHAR(255) NOT NULL, " +
                    " PASSWORD VARCHAR(255) NOT NULL," +
                    " WINS INTEGER DEFAULT 0," +
                    " LOSSES INTEGER DEFAULT 0, " +
                    " DRAWS INTEGER DEFAULT 0," +
                    " SCORE INTEGER DEFAULT 0," +
                    "PRIMARY KEY (USERNAME) )");
            System.out.println("Created users table");
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    conn.close();
            }catch(SQLException se){
            }// do nothing
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try

    }


    public static boolean signUpQuery(String stm) throws java.sql.SQLException, ClassNotFoundException {
        // reference the driver being used to connect to the database
        Class.forName(DBOperations.driver);

        // connect to database
        Connection databaseConn = DriverManager.getConnection(DBOperations.tictactoe, DBOperations.USER, DBOperations.PASS/*"jdbc:sqlite:/Users/austinrosario/Desktop/Java/TicTacToe/TicTacToe/TicTacToe.db"*/);

        // create a statement
        Statement query = databaseConn.createStatement();

        // execute SQL query
        ResultSet rs = query.executeQuery("SELECT email, username, password FROM "+ USERS +" WHERE " + stm);// sql email = '" + email+ "' AND username = '" + username + "'");// + "' AND password = '" + passwordField.getText() + "'");

        // if the query results in a ResultSet, then the user entered was found in the database
        if(rs.next()) {
            rs.close();
            query.close();
            databaseConn.close();
            return true;
        } else {
            rs.close();
            query.close();
            databaseConn.close();
            return false;
        }
    }

    public static void insertUser(String email, String username, String password) throws java.sql.SQLException, ClassNotFoundException {
        // reference the driver being used to connect to the database
        Class.forName(DBOperations.driver);

        // connect to database
        Connection databaseConn = DriverManager.getConnection(DBOperations.tictactoe, DBOperations.USER, DBOperations.PASS/*"jdbc:sqlite:/Users/austinrosario/Desktop/Java/TicTacToe/TicTacToe/TicTacToe.db"*/);

        // create a statement
        Statement query = databaseConn.createStatement();

        // execute SQL insert
        query.executeUpdate("INSERT INTO "+ USERS +" VALUES ('" + email+ "', '" + username + "', '" + password + "')");

        query.close();
        databaseConn.close();
    }

    public static boolean loginQuery(String sql) {

        Connection conn = null;
        Statement stmt = null;
        ResultSet result = null;
        try {

            Class.forName(driver);
            conn = DriverManager.getConnection(tictactoe,USER,PASS);
            stmt = conn.createStatement();
            result = stmt.executeQuery(sql);

            return result.next();


        }
        catch(SQLException se){ se.printStackTrace(); }
        catch(Exception e){ e.printStackTrace(); }
        finally{

            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }
        return false;
    }


}


