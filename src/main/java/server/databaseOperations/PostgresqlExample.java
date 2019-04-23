package server.databaseOperations;

import java.sql.*;


public class PostgresqlExample {

    // driver and database url
    static final String DB_URL = "jdbc:postgresql://localhost/TicTacToe";
    static final String driver = "org.postgresql.Driver";



    // database tables
    static final String USERS = "USERS";
    static final String GAMES = "games";
    static final String MOVES = "MOVES";

    //  Database credentials
    static final String USER = "";
    static final String PASS = "";



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



    public static void createTable() {
            Connection conn = null;
            Statement stmt = null;
            try{
                //STEP 2: Register JDBC driver
                Class.forName(driver);

                //STEP 3: Open a connection
                System.out.println("Connecting to a selected database...");
                conn = DriverManager.getConnection(DB_URL, USER, PASS);
                System.out.println("Connected database successfully...");



                stmt = conn.createStatement();
                //stmt.execute("CREATE DATABASE TicTacToe");
                stmt.executeUpdate("CREATE TABLE USERS " +
                        " email VARCHAR(255), " +
                        " username VARCHAR(255), " +
                        " wins INTEGER, " +
                        " losses INTEGER, " +
                        " draws INTEGER, " +
                        " PRIMARY KEY ( username ))");
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


    public static ResultSet executeQuery(String sql){

        Connection conn = null;
        Statement stmt = null;
        ResultSet result = null;
        try {

            Class.forName(driver);
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = conn.createStatement();
            result = stmt.executeQuery(sql);
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





}


