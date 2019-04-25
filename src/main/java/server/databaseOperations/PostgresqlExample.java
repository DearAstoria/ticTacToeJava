package server.databaseOperations;

import javafx.scene.control.Button;

import java.sql.*;
import java.util.Arrays;


public class PostgresqlExample {

    // driver and database url
    public static final String DB_URL = "jdbc:postgresql://localhost/";//jdbc:postgresql://localhost/";
    public static final String driver = "org.postgresql.Driver";
    public static final String tictactoe = "jdbc:postgresql://localhost/tictactoe";


    // database tables
    public static final String USERS = "USERS";
    public static final String GAMES = "games";
    public static final String MOVES = "MOVES";

    //  Database credentials
    public static final String USER = "postgres";
    public static final String PASS = "1234";



    public static boolean signUpQuery(String stm) throws java.sql.SQLException, ClassNotFoundException {
        // reference the driver being used to connect to the database
        Class.forName(PostgresqlExample.driver);

        // connect to database
        Connection databaseConn = DriverManager.getConnection(PostgresqlExample.tictactoe, PostgresqlExample.USER, PostgresqlExample.PASS/*"jdbc:sqlite:/Users/austinrosario/Desktop/Java/TicTacToe/TicTacToe/TicTacToe.db"*/);

        // create a statement
        Statement query = databaseConn.createStatement();

        // execute SQL query
        ResultSet rs = query.executeQuery("SELECT email, username, password FROM USERS WHERE " + stm);// sql email = '" + email+ "' AND username = '" + username + "'");// + "' AND password = '" + passwordField.getText() + "'");

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
        Class.forName(PostgresqlExample.driver);

        // connect to database
        Connection databaseConn = DriverManager.getConnection(PostgresqlExample.tictactoe,PostgresqlExample.USER, PostgresqlExample.PASS/*"jdbc:sqlite:/Users/austinrosario/Desktop/Java/TicTacToe/TicTacToe/TicTacToe.db"*/);

        // create a statement
        Statement query = databaseConn.createStatement();

        // execute SQL insert
        query.executeUpdate("INSERT INTO USERS VALUES ('" + email+ "', '" + username + "', '" + password + "')");

        query.close();
        databaseConn.close();
    }


    public static void createDatabase(String database)

    {
        Connection conn = null;
        Statement stmt = null;
        try{
            //STEP 2: Register JDBC driver
            Class.forName(driver);

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            System.out.println("Creating database...");
            stmt = conn.createStatement();

            String sql = "CREATE DATABASE " + database;
            stmt.executeUpdate(sql);
            System.out.println("Database created successfully...");
        }catch(
                SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
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
        }//end try
        System.out.println("Goodbye!");
    }



    public static void createTable(String db, String sql) {
            StringBuilder url = new StringBuilder();
            url.append(DB_URL).append(db);
            System.out.println(url);
            Connection conn = null;
            Statement stmt = null;
            try{
                //STEP 2: Register JDBC driver
                Class.forName(driver);

                //STEP 3: Open a connection
                System.out.println("Connecting to a selected database...");
                conn = DriverManager.getConnection(url.toString(), USER, PASS);
                System.out.println("Connected database successfully...");



                stmt = conn.createStatement();
                //stmt.execute("CREATE DATABASE TicTacToe");
                stmt.executeUpdate(sql/*"CREATE TABLE USERS " +
                        " email VARCHAR(255), " +
                        " username VARCHAR(255), " +
                        " wins INTEGER, " +
                        " losses INTEGER, " +
                        " draws INTEGER, " +
                        " PRIMARY KEY ( username ))"*/);
                System.out.println("Created table in given database...");
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
            System.out.println("Goodbye!");
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

    public static boolean execute(String sql){

        Connection conn = null;
        Statement stmt = null;
        boolean result = false;
        try {

            Class.forName(driver);
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = conn.createStatement();
            result = stmt.execute(sql);
        }
        catch(
                SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
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

        return result;
    }

    public static int executeUpdate(String sql){

        Connection conn = null;
        Statement stmt = null;
        int result = 0;
        try {

            Class.forName(driver);
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = conn.createStatement();
            result = stmt.executeUpdate(sql);
        }
        catch(
                SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
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

        return result;
    }


    public synchronized String[] getLeaderBoard() throws  Exception {

        String ar[] = new String[1000];
        int i = 0;
        StringBuilder b = new StringBuilder();

        Class.forName(PostgresqlExample.driver);

        // connect to database
        Connection databaseConn = DriverManager.getConnection(PostgresqlExample.tictactoe, PostgresqlExample.USER, PostgresqlExample.PASS/*"jdbc:sqlite:/Users/austinrosario/Desktop/Java/TicTacToe/TicTacToe/TicTacToe.db"*/);

        // create a statement
        Statement query = databaseConn.createStatement();

        // execute SQL insert
        ResultSet rs = query.executeQuery("SELECT username, wins FROM USERS" +
                " ORDER BY WINS DESC");
        while (rs.next()) {
            //ar[i] =
            b = new StringBuilder();
            b.append(rs.getString("username")).append(" ").append(String.valueOf(rs.getInt("wins")));
            ar[i++] = new String(b);
            System.out.println(b);
        }
        //publish(connection, Arrays.copyOf(ar, i), Server.NEW_ACCOUNT_CHANNEL);
        //for (String str : Arrays.copyOf(ar, i))
        //    leaderBoardList.getChildren().add(new Button(str));

        query.close();
        databaseConn.close();

        return Arrays.copyOf(ar, i);
    }



}


