/*
    Here is an example of connecting to a database using jdbc

    The table we will use in the example is
    Table Test(
       name     varchar(30),
       ssn      number(10),
       bday     date
    );

    For demostratration purpose, insert two records into this table:
    ( 'Mike', 123456789, '09/Nov/03' )
    ( 'Amy', 987654321, '10/Nov/03' )

    Written by: Jonathan Beaver, modified by Thao Pham
    Purpose: Demo JDBC for CS1555 Class

    IMPORTANT (otherwise, your code may not compile)
    Same as using sqlplus, you need to set oracle environment variables by
    sourcing bash.env or tcsh.env
*/

import java.sql.*;  //import the file containing definitions for the parts

//needed by java for database connection and manipulation

public class UsersStuff {
    private Connection connection; //used to hold the jdbc connection to the DB
    private Statement statement; //used to create an instance of the connection
    private ResultSet resultSet; //used to hold the result of your query (if one
    // exists)
    private String query;  //this will hold the query we are using
    private String username, password;

    public UsersStuff() {
    /*Making a connection to a DB causes certian exceptions.  In order to handle
    these, you either put the DB stuff in a try block or have your function
    throw the Execptions and handle them later.  For this demo I will use the
    try blocks*/
        username = "zab30"; //This is your username in oracle
        password = "3970513"; //This is your password in oracle
        try {
            //Register the oracle driver.  This needs the oracle files provided
            //in the oracle.zip file, unzipped into the local directory and
            //the class path set to include the local directory
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            //This is the location of the database.  This is the database in oracle
            //provided to the class
            String url = "jdbc:oracle:thin:@class3.cs.pitt.edu:1521:dbclass";

            connection = DriverManager.getConnection(url, username, password);
            //create a connection to DB on class3.cs.pitt.edu
        } catch (Exception Ex)  //What to do with any exceptions
        {
            System.out.println("Error connecting to database.  Machine Error: " +
                    Ex.toString());
            Ex.printStackTrace();
        }


    }
    //creating a user in the db
    public boolean createUser(String username, String name, String password, String email, String dob, Timestamp stamp){

        try {

            java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");

            java.sql.Date bday = new java.sql.Date(df.parse(dob).getTime());
            //INSERT into profile VALUES('ZachSmith','Zach Smith','admin',TO_DATE('01-JAN-91'),NULL);

            query = "insert into profile values (?,?,?,?,?)";

            PreparedStatement updateStatement = connection.prepareStatement(query);
            updateStatement.setString(1, username);
            updateStatement.setString(2, name);
            updateStatement.setString(3,password);
            updateStatement.setDate(4, bday);
            updateStatement.setTimestamp(5,stamp);

            updateStatement.executeUpdate();


      /* We can also so the insert statement directly as follows:

       query = "INSERT INTO Test VALUES ('Tester', 111111112, '1/Nov/03')";
      int result = statement.executeUpdate(query); //executing update returns
      //either the row count for INSERT, UPDATE or DELETE or 0 for SQL
      //statements that return nothing

      */



        } catch (Exception Ex) {
            System.out.println("Error running the Create user Querey.  Machine Error: " +
                    Ex.toString());
        }
        try {

            statement = connection.createStatement(); //create an instance

            //I will show the insert worked by selecting the content of the table again
            //statement = connection.createStatement();
            query = "SELECT * FROM profile";
            resultSet = statement.executeQuery(query);
            if(resultSet==null){
                System.out.println("Result set is null");
            }else {
                System.out.println("\nAfter the insert, data is...\n");
                int counter = 1;
                while (resultSet.next()) {
                    System.out.println("Record " + counter + ": " +
                            resultSet.getString(1) + ", " +
                            resultSet.getString(2) + ", " +
                            resultSet.getString(3) + ", " +
                            resultSet.getDate(4));
                    counter++;
                }
            }



        } catch (Exception Ex) {
            System.out.println("Error reading Create user querey.  Machine Error: " +
                    Ex.toString());
        }


        return true;
    }
    //logging in a user
    public boolean userLogin(String loginName, String loginPassword){
        try {
            statement = connection.createStatement(); //create an instance

            //I will show the insert worked by selecting the content of the table again
            //statement = connection.createStatement();
            System.out.println("******Attempting Login******");
            query = "SELECT * FROM profile where userID='" + loginName + "' AND password='" + loginPassword+"'";
            resultSet = statement.executeQuery(query);
            int counter = 0;
            while (resultSet.next()) {
                counter++;
                System.out.println("Record " + counter + ": " +
                        resultSet.getString(1) + ", " +
                        resultSet.getString(2) + ", " +
                        resultSet.getString(3) + ", " +
                        resultSet.getDate(4));

            }
            if(counter==1){
                return true;
            }else{
                return false;
            }
        }catch(Exception Ex) {
            System.out.println("Error login user querey.  Machine Error: " +
                    Ex.toString());
        }
        return false;


    }
    //Initiating a friendship
    public boolean initiateFriendship(String username, String fiendname, String message){

        try {

         //

            query = "insert into profile values (?,?,?,?,?)";

            PreparedStatement updateStatement = connection.prepareStatement(query);
            updateStatement.setString(1, username);
            updateStatement.setString(2, name);
            updateStatement.setString(3,password);
            updateStatement.setDate(4, bday);
            updateStatement.setTimestamp(5,stamp);

            updateStatement.executeUpdate();


      /* We can also so the insert statement directly as follows:

       query = "INSERT INTO Test VALUES ('Tester', 111111112, '1/Nov/03')";
      int result = statement.executeUpdate(query); //executing update returns
      //either the row count for INSERT, UPDATE or DELETE or 0 for SQL
      //statements that return nothing

      */



        } catch (Exception Ex) {
            System.out.println("Error running the Create user Querey.  Machine Error: " +
                    Ex.toString());
        }
        try {

            statement = connection.createStatement(); //create an instance

            //I will show the insert worked by selecting the content of the table again
            //statement = connection.createStatement();
            query = "SELECT * FROM profile";
            resultSet = statement.executeQuery(query);
            if(resultSet==null){
                System.out.println("Result set is null");
            }else {
                System.out.println("\nAfter the insert, data is...\n");
                int counter = 1;
                while (resultSet.next()) {
                    System.out.println("Record " + counter + ": " +
                            resultSet.getString(1) + ", " +
                            resultSet.getString(2) + ", " +
                            resultSet.getString(3) + ", " +
                            resultSet.getDate(4));
                    counter++;
                }
            }



        } catch (Exception Ex) {
            System.out.println("Error reading Create user querey.  Machine Error: " +
                    Ex.toString());
        }


        return true;
    }
    public void closeConnection(){
        try {
            connection.close();
        }catch(Exception Ex) {
            System.out.println("Error Closing connection.  Machine Error: " +
                    Ex.toString());
        }
    }
    public static void main(String args[]) {
        UsersStuff demo = new UsersStuff();
        Timestamp blankStamp = new Timestamp(87);
        //demo.createUser("zab30","Zach Blouse","adminPass","zab30@pitt.edu","1996-05-19",blankStamp);
        //demo.createUser("zblouse","Zach Blouse","adminPass","zab30@pitt.edu","1996-05-19",blankStamp);
        //demo.createUser("uav97","Unidentified","adminPass","uav97@pitt.edu","1002-01-12",blankStamp);
        Boolean validLogin=demo.userLogin("zab30","adminPass");
        if(validLogin){
            System.out.println("Logged in successfully");
        }else{
            System.out.println("Invalid username or password");
        }
        Boolean invalidLogin=demo.userLogin("zab30","wrongPass");
        if(invalidLogin){
            System.out.println("Logged in successfully");
        }else{
            System.out.println("Invalid username or password");
        }
        System.out.println("Did it work?");
        demo.closeConnection();

    }
}