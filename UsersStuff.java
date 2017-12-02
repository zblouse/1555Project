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
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    //logging out
    public void userLogOut(String loginName){
        try {
            Timestamp currentStamp = new Timestamp(System.currentTimeMillis());
            statement = connection.createStatement(); //create an instance

            //I will show the insert worked by selecting the content of the table again
            //statement = connection.createStatement();
            System.out.println("******Attempting LogOut******");
            query = "UPDATE profile SET lastlogin=? where userID = ?";

            PreparedStatement updateStatement = connection.prepareStatement(query);
            updateStatement.setTimestamp(1, currentStamp);
            updateStatement.setString(2, username);
            updateStatement.executeUpdate();
            closeConnection();
            System.exit(0);
        }catch(Exception Ex) {
            System.out.println("Error logout user querey.  Machine Error: " +
                    Ex.toString());
        }

    }
    //Initiating a friendship
    public boolean initiateFriendship(String username, String friendname, String message){
        System.out.println("Attempting to initiate friend request");
        try {

         //

            query = "insert into pendingFriends values (?,?,?)";

            PreparedStatement updateStatement = connection.prepareStatement(query);
            updateStatement.setString(1, username);
            updateStatement.setString(2, friendname);
            updateStatement.setString(3,message);


            updateStatement.executeUpdate();


      /* We can also so the insert statement directly as follows:

       query = "INSERT INTO Test VALUES ('Tester', 111111112, '1/Nov/03')";
      int result = statement.executeUpdate(query); //executing update returns
      //either the row count for INSERT, UPDATE or DELETE or 0 for SQL
      //statements that return nothing

      */



        } catch (Exception Ex) {
            System.out.println("Error running the Initiate Friendship Querey.  Machine Error: " +
                    Ex.toString());
        }
        try {

            statement = connection.createStatement(); //create an instance

            //I will show the insert worked by selecting the content of the table again
            //statement = connection.createStatement();
            query = "SELECT * FROM pendingFriends";
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
                            resultSet.getString(3));

                    counter++;
                }
            }



        } catch (Exception Ex) {
            System.out.println("Error reading pending friend querey.  Machine Error: " +
                    Ex.toString());
        }


        return true;
    }
    //confirm friendship: username is the logged in user, aka the to id in pendingFriends
    public boolean confirmFriendship(String username, String confirmedFriendUsername){
        String sentMessage="";
        try {
            statement = connection.createStatement(); //create an instance

            //I will show the insert worked by selecting the content of the table again
            //statement = connection.createStatement();
            System.out.println("******Attempting Confirm Friendship******");
            query = "SELECT * FROM pendingFriends where fromID='" + confirmedFriendUsername + "' AND toID='" + username+"'";
            resultSet = statement.executeQuery(query);
            int counter = 0;
            while (resultSet.next()) {
                counter++;
                System.out.println("Record " + counter + ": " +
                        resultSet.getString(1) + ", " +
                        resultSet.getString(2) + ", " +
                        resultSet.getString(3));
                sentMessage=resultSet.getString(3);
            }
            //if counter is 1, there was a pendingFriend request to accept
            if(counter==1){
                connection.setAutoCommit(false);
                String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
                java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");

                java.sql.Date today = new java.sql.Date(df.parse(date).getTime());
                //INSERT into profile VALUES('ZachSmith','Zach Smith','admin',TO_DATE('01-JAN-91'),NULL);

                query = "insert into friends values (?,?,?,?)";

                PreparedStatement updateStatement = connection.prepareStatement(query);
                updateStatement.setString(1, username);
                updateStatement.setString(2,confirmedFriendUsername);
                updateStatement.setDate(3, today);
                updateStatement.setString(4,sentMessage);
                updateStatement.executeUpdate();

                query = "delete from pendingFriends where toID= ? AND fromID = ?";

                PreparedStatement deleteStatement = connection.prepareStatement(query);
                deleteStatement.setString(1, username);
                deleteStatement.setString(2,confirmedFriendUsername);
                deleteStatement.executeUpdate();
                connection.commit();
                System.out.println("Created friends: "+username+" & "+confirmedFriendUsername);
                System.out.println("Deleted request: "+username+" & "+confirmedFriendUsername);

                connection.setAutoCommit(true);
                return true;
            }else{
                System.out.println("Error: Attempting to confirm friend request that doens't exist");
                return false;
            }
        }catch(Exception Ex) {
            System.out.println("Error confirm friend.  Machine Error: " +
                    Ex.toString());
        }
        return false;


    }
    //displaying friends
    public boolean displayFriends(String loginName){
        ArrayList<String> usersFriends = new ArrayList<String>();
        ArrayList<String> friendsFriends = new ArrayList<String>();
        try {
            statement = connection.createStatement(); //create an instance

            //I will show the insert worked by selecting the content of the table again
            //statement = connection.createStatement();
            System.out.println("******Attempting Display Friends******");
            query = "SELECT * FROM friends where userID1='" + loginName + "'";
            resultSet = statement.executeQuery(query);
            int counter = 0;
            while (resultSet.next()) {
                counter++;

                usersFriends.add(resultSet.getString(2));


            }
            query = "SELECT * FROM friends where userID2='" + loginName+"'";
            resultSet = statement.executeQuery(query);
            counter = 0;
            while (resultSet.next()) {
                counter++;

                usersFriends.add(resultSet.getString(1));
            }
            for(String friendName:usersFriends){
                query = "SELECT * FROM friends where userID1='" + friendName + "'";
                resultSet = statement.executeQuery(query);
                counter = 0;
                while (resultSet.next()) {
                    counter++;
                    if(!resultSet.getString(2).equals(loginName)) {
                        friendsFriends.add(resultSet.getString(2));
                    }


                }
                query = "SELECT * FROM friends where userID2='" + friendName + "'";
                resultSet = statement.executeQuery(query);
                counter = 0;
                while (resultSet.next()) {
                    counter++;

                    if(!resultSet.getString(1).equals(loginName)) {
                        friendsFriends.add(resultSet.getString(1));
                    }
                }
            }
            System.out.println("Your friends: "+usersFriends.toString());
            System.out.println("Friends friends: "+friendsFriends.toString());
            if(counter>=1){
                return true;
            }else{
                return false;
            }
        }catch(Exception Ex) {
            System.out.println("Error Display friends querey.  Machine Error: " +
                    Ex.toString());
        }
        return false;


    }
    public User retrieveProfile(String requestedName){
        try {
            statement = connection.createStatement(); //create an instance

            //I will show the insert worked by selecting the content of the table again
            //statement = connection.createStatement();
            System.out.println("******Attempting Login******");
            query = "SELECT * FROM profile where userID='" + requestedName +"'";
            resultSet = statement.executeQuery(query);
            int counter = 0;
            while (resultSet.next()) {
                counter++;
                User theUser = new User( resultSet.getString(1), resultSet.getString(2),resultSet.getString(3),resultSet.getDate(4),resultSet.getTimestamp(4));
                return theUser;

            }
            if(counter==1){


            }else{
                return null;
            }
        }catch(Exception Ex) {
            System.out.println("Error retrieve profile querey.  Machine Error: " +
                    Ex.toString());
        }
        return null;
    }


    public boolean sendMessageToUser(String sendingUser, String receivingUser, String message){
        System.out.println("Attempting to send a message to a user");
        try {
            statement = connection.createStatement();
            String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
            java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");

            java.sql.Date today = new java.sql.Date(df.parse(date).getTime());
            query = "SELECT count(*) AS numMgs FROM messages";

            resultSet = statement.executeQuery(query);

            int counter = 0;
            int numMessages=0;
            while (resultSet.next()) {
                counter++;
                numMessages = resultSet.getInt(1)+1;
            }

            query = "insert into messages values (?,?,?,?,?,?)";

            PreparedStatement updateStatement = connection.prepareStatement(query);
            updateStatement.setString(1, String.valueOf(numMessages));
            updateStatement.setString(2, sendingUser);
            updateStatement.setString(3,message);
            updateStatement.setString(4, receivingUser);
            updateStatement.setString(5,  null);
            updateStatement.setDate(6,today);

            updateStatement.executeUpdate();

            return true;
      /* We can also so the insert statement directly as follows:

       query = "INSERT INTO Test VALUES ('Tester', 111111112, '1/Nov/03')";
      int result = statement.executeUpdate(query); //executing update returns
      //either the row count for INSERT, UPDATE or DELETE or 0 for SQL
      //statements that return nothing

      */



        } catch (Exception Ex) {
            System.out.println("Error sending message to user.  Machine Error: " +
                    Ex.toString());
        }



        return true;
    }
    public boolean displayMessages(String thisName){
        try {
            statement = connection.createStatement(); //create an instance

            ArrayList<Message> theMessages = new ArrayList<Message>();
            System.out.println("******Attempting to Display messages******");
            query = "SELECT * FROM messages where toUserID='" + thisName +"'";
            resultSet = statement.executeQuery(query);
            int counter = 0;
            while (resultSet.next()) {
                counter++;
                theMessages.add(new Message(resultSet.getString(2),resultSet.getString(4),resultSet.getString(3)));

            }
            if(counter>=1){
                for(Message item:theMessages){
                    System.out.println("From: "+item.getFromUser()+" Message: "+item.getMessage());
                }
                return true;

            }else{
                return false;
            }
        }catch(Exception Ex) {
            System.out.println("Error retreiving messages.  Machine Error: " +
                    Ex.toString());
        }
        return false;
    }
    public boolean displayNewMessages(String thisName){
        try {
            statement = connection.createStatement(); //create an instance

            System.out.println("******Attempting to Display messages******");
            query = "SELECT lastlogin FROM profile where userID='" + thisName +"'";
            resultSet = statement.executeQuery(query);
            int counter = 0;
            String lastLogin= "no";
            while (resultSet.next()) {
                counter++;
                lastLogin = new SimpleDateFormat("yyyy-MM-dd").format(resultSet.getTimestamp(1));

            }
            if(lastLogin.equals("no")){
                return false;
            }else {
                System.out.println("******Attempting to Display messages******");
                java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");

                java.sql.Date loginDate = new java.sql.Date(df.parse(lastLogin).getTime());
                query = "SELECT * FROM messages where toUserID= ? AND dateSent > ?";
                PreparedStatement updateStatement = connection.prepareStatement(query);
                updateStatement.setString(1, thisName);
                updateStatement.setDate(2,loginDate);

                resultSet = updateStatement.executeQuery();
                ArrayList<Message> theMessages = new ArrayList<Message>();


                counter = 0;
                while (resultSet.next()) {
                    counter++;
                    theMessages.add(new Message(resultSet.getString(2), resultSet.getString(4), resultSet.getString(3)));

                }
                if (counter >= 1) {
                    for (Message item : theMessages) {
                        System.out.println("From: " + item.getFromUser() + " Message: " + item.getMessage());
                    }
                    return true;

                } else {
                    return false;
                }
            }
        }catch(Exception Ex) {
            System.out.println("Error retreiving messages.  Machine Error: " +
                    Ex.toString());
        }
        return false;
    }
    
    //close the connection to the db
    public void closeConnection(){
        try {
            connection.close();
        }catch(Exception Ex) {
            System.out.println("Error Closing connection.  Machine Error: " +
                    Ex.toString());
        }
    }
    public static void main(String args[]) {
        UsersStuff users = new UsersStuff();
        Timestamp blankStamp = new Timestamp(87);

       // users.createUser("zab31","Zach Blouse","adminPass","zab31@pitt.edu","1996-05-19",blankStamp);
        //users.createUser("zab32","Zach Blouse","adminPass","zab32@pitt.edu","1996-05-19",blankStamp);
        //users.createUser("zab33","Unidentified","adminPass","zab33@pitt.edu","1002-01-12",blankStamp);

        /*
        Boolean validLogin=users.userLogin("zab30","adminPass");
        if(validLogin){
            System.out.println("Logged in successfully");
        }else{
            System.out.println("Invalid username or password");
        }
        Boolean invalidLogin=users.userLogin("zab30","wrongPass");
        if(invalidLogin){
            System.out.println("Logged in successfully");
        }else{
            System.out.println("Invalid username or password");
        }
        */
        //users.initiateFriendship("uav97","zab31","Hello friend. Please accept my request");
        //users.confirmFriendship("zab31","uav97");
        //users.initiateFriendship("zab31","zab32","Hello friend. Please accept my request");
        //users.confirmFriendship("zab32","zab31");
        //users.initiateFriendship("zab32","zab33","Hello friend. Please accept my request");
        //users.confirmFriendship("zab33","zab32");
        //users.displayFriends("zab30");
        //users.displayFriends("uav97");
        //User thisUser= users.retrieveProfile("zab30");
        //System.out.println("Retrieved user: "+thisUser.getName());
        //users.userLogOut("uav97");
        //users.sendMessageToUser("zab30","uav97","Hi");
        //users.sendMessageToUser("zblouse","uav97","Whats up");
        //users.displayNewMessages("uav97");
        users.closeConnection();

    }
}