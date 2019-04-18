package databaseOperations;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class PostgresqlExample {


    static final String DB_URL = "jdbc:postgresql://localhost/";
    static final String driver = "org.postgresql.Driver";

    //  Database credentials
    static final String USER = "user";
    static final String PASS = "password";



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



        static void createTable() {
            Connection conn = null;
            Statement stmt = null;
            try{
                //STEP 2: Register JDBC driver
                Class.forName(driver);

                //STEP 3: Open a connection
                System.out.println("Connecting to a selected database...");
                conn = DriverManager.getConnection(DB_URL, USER, PASS);
                System.out.println("Connected database successfully...");

                //STEP 4: Execute a query
                System.out.println("Creating table in given database...");
                stmt = conn.createStatement();

                String sql = "CREATE TABLE USERS " +
                        "(id INTEGER not NULL, " +
                        " first VARCHAR(255), " +
                        " last VARCHAR(255), " +
                        " age INTEGER, " +
                        " PRIMARY KEY ( id ))";

                stmt.executeUpdate(sql);
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







}


