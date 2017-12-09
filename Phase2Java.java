//Zach Blouse and Jake Winkler
import java.sql.*;  //import the file containing definitions for the parts
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
public class Phase2Java{
    private Connection connection; //used to hold the jdbc connection to the DB
    private Statement statement; //used to create an instance of the connection
    private ResultSet resultSet; //used to hold the result of your query (if one
    // exists)
    private String query;  //this will hold the query we are using
    private String username, password;

    public Phase2Java() {
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
            return false;
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
            return false;
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
    public boolean userLogOut(String loginName){
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
            return true;
        }catch(Exception Ex) {
            System.out.println("Error logout user querey.  Machine Error: " +
                    Ex.toString());
            return false;
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
            System.out.println("Error running the Initiate Friendship Querey.  Machine Error: " + Ex.toString());
            return false;
        }
        try {

            statement = connection.createStatement(); //create an instance

            //I will show the insert worked by selecting the content of the table again
            //statement = connection.createStatement();
            query = "SELECT * FROM pendingFriends";
            resultSet = statement.executeQuery(query);
            if(resultSet==null){
                //System.out.println("Result set is null");
            }else {
                //System.out.println("\nAfter the insert, data is...\n");
                int counter = 1;
                while (resultSet.next()) {
                    //System.out.println("Record " + counter + ": " +resultSet.getString(1) + ", " +resultSet.getString(2) + ", " +resultSet.getString(3));

                    counter++;
                }
            }



        } catch (Exception Ex) {
            System.out.println("Machine Error: " + Ex.toString());
            return false;
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
            return false;
        }
        return false;


    }
    public HashMap<Integer,String> getFriendRequests(String loginName){
        HashMap<Integer,String> pendingRequests = new HashMap<Integer,String>();
        query = "SELECT * FROM pendingFriends where toID='" + loginName+"'";
        resultSet = statement.executeQuery(query);
        int counter = 0;
        while (resultSet.next()) {
            counter++;

            pendingRequests.put(counter,resultSet.getString(1));
        }
        return pendingRequests;
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
    public void topMessages(int k, int months){
        try {
            statement = connection.createStatement(); //create an instance
            //Hashmap<String,int> messageCounts = new Hashmap<String,int>();
            System.out.println("******Attempting to Display Top Messagers******");
            query = "select * from (SELECT fromID, count(*) as cnt FROM messages where dateSent>add_months(sysdate,-"+months+") group by fromID order by count(*) desc)where rownum<="+k;
            resultSet = statement.executeQuery(query);
            int counter = 0;

            while (resultSet.next()) {
                counter++;
                System.out.println("Name: "+resultSet.getString(1)+"Messages: "+resultSet.getInt(2));
            }
            //query = "SELECT toID, count(*) as cnt FROM messages where dateSent>add_months(sysdate,-"+months+") group by toID";
            //query="Select * from( Select fromID,sentCnt+receivedCnt from (SELECT fromID, count(*) as sentCnt FROM messages where dateSent>add_months(sysdate,-"+months+") group by fromID)sent inner join (SELECT toUserID, count(*) as receivedCnt FROM messages where dateSent>add_months(sysdate,-"+months+") group by toUserID)received ON sent.fromID=received.toUserID order by sentCnt+receivedCnt)where rownum<"+k;
            resultSet = statement.executeQuery(query);
            counter = 0;

            while (resultSet.next()) {
                counter++;
                /*
                int totalMessages= messageCounts.get(resultSet.getString(1))+resultSet.getInt(2);
                messageCounts.replace(resultSet.getString(1),totalMessages);
                */
                //System.out.println("Name: "+resultSet.getString(1)+"Messages: "+resultSet.getInt(2));
            }



        }catch(Exception Ex) {
            System.out.println("Error retreiving top messages.  Machine Error: " +
                    Ex.toString());
        }

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
    public boolean createGroup(String uID, String name, String desc, int lim){
        int counter = 1;
        try {
            statement = connection.createStatement(); //create an instance
            query = "SELECT * FROM Groups"; //sample query one

            resultSet = statement.executeQuery(query);

            while (resultSet.next())
            {
                System.out.println("Record " + counter + ": " +
                        resultSet.getString(1) + ", " +
                        resultSet.getString(2) + ", " +
                        resultSet.getString(3));
                counter++;
            }

            String groupName=name;
            String groupID=name;
            String description=desc;
            int limit = lim;

            query = "insert into Groups values (?,?,?,?)";

            PreparedStatement updateStatement = connection.prepareStatement(query);
            updateStatement.setString(1, groupName);
            updateStatement.setString(2, groupID);
            updateStatement.setString(3, description);
            updateStatement.setInt(4, limit);
            updateStatement.executeUpdate();

            query = "INSERT INTO GroupMembership VALUES (?,?,?)";
            PreparedStatement membershipStatement = connection.prepareStatement(query);
            membershipStatement.setString(1,groupID);
            membershipStatement.setString(2,uID);
            membershipStatement.setString(3,"Manager");
            membershipStatement.executeUpdate();

            //I will show the insert worked by selecting the content of the table again
            //statement = connection.createStatement();
            query = "SELECT * FROM Groups";
            resultSet = statement.executeQuery(query);
            System.out.println("\nAfter the insert, data is...\n");
            counter = 1;
            while (resultSet.next()) {
                System.out.println("Record " + counter + ": " +
                        resultSet.getString(1) + ", " +
                        resultSet.getString(2) + ", " +
                        resultSet.getString(3)+ ", "+
                        resultSet.getInt(4));
                counter++;
            }
            query = "SELECT * FROM GroupMembership";
            resultSet = statement.executeQuery(query);
            System.out.println("\nAfter the insert, data is...\n");
            counter = 1;
            while (resultSet.next()) {
                System.out.println("Record " + counter + ": " +
                        resultSet.getString(1) + ", " +
                        resultSet.getString(2) + ", " +
                        resultSet.getString(3));
                counter++;
            }
            // connection.close();

        } catch (Exception Ex) {
            System.out.println("Error running the sample queries.  Machine Error: " +
                    Ex.toString());
        }

        System.out.println("Good Luck");
        return true;
    }


    public boolean initiateAddingGroup(String uID, String gID, String msg)
    {
        try {
            int limit;
            int curNum;
            int counter;
            statement = connection.createStatement(); //create an instance
            query = "SELECT userID FROM groupMembership WHERE gID ='"+gID+"' AND userID = '"+uID+"'";
            resultSet = statement.executeQuery(query);
            if(resultSet.next())
            {
                System.out.println("Error already in group");
                return false;
            }
            query = "SELECT memberLimit FROM Groups WHERE gId = '"+gID+"'"; //sample query one
            ResultSet gLimit = statement.executeQuery(query); //run the query on the DB table
            gLimit.next();
            limit = gLimit.getInt(1);
            query = "SELECT COUNT(*) AS numMems FROM groupMembership WHERE gID = '"+gID+"'";
            resultSet = statement.executeQuery(query);

            resultSet.next();
            curNum = resultSet.getInt(1);
            if(curNum == limit)
            {
                System.out.println("Group at capacity, cannot join.");
                return false;
            }

            query = "INSERT into pendingGroupMembers VALUES(?,?,?)";
            PreparedStatement pending = connection.prepareStatement(query);
            pending.setString(1, gID);
            pending.setString(2,uID);
            pending.setString(3,msg);
            pending.executeUpdate();

            query = "SELECT * FROM pendingGroupMembers";
            resultSet = statement.executeQuery(query);
            System.out.println("\nAfter the insert, data is...\n");
            counter = 1;
            while (resultSet.next()) {
                System.out.println("Record " + counter + ": " +
                        resultSet.getString(1) + ", " +
                        resultSet.getString(2) + ", " +
                        resultSet.getString(3));
                counter++;
            }



        } catch (Exception Ex) {
            System.out.println("Error running the sample queries.  Machine Error: " +
                    Ex.toString());
        }

        System.out.println("Good Luck");
        return true;
    }

    public boolean confirmMembership(String uID, String gID)
    {
        try{
            statement=connection.createStatement();
            int counter=0;
            query = "SELECT * FROM pendingGroupMembers where gID='" + gID + "' AND userID='" + uID+"'";
            resultSet = statement.executeQuery(query);
            System.out.println("----Trying to Confirm Membership -----");
            if(!resultSet.next()){
                System.out.println("Error No pendingGroup member.");
                return false;
            }
            connection.setAutoCommit(false);
            query = "INSERT INTO groupMembership VALUES (?,?,?)";
            PreparedStatement accepted = connection.prepareStatement(query);
            accepted.setString(1, gID);
            accepted.setString(2,uID);
            accepted.setString(3,"Member");
            accepted.executeUpdate();

            query = "DELETE FROM pendingGroupMembers where gID= ? AND userID = ?";

            PreparedStatement deleteStatement = connection.prepareStatement(query);
            deleteStatement.setString(1, gID);
            deleteStatement.setString(2,uID);
            deleteStatement.executeUpdate();
            connection.commit();

            connection.setAutoCommit(true);

            query = "SELECT * FROM pendingGroupMembers";
            resultSet = statement.executeQuery(query);
            System.out.println("\nAfter the insert, data is...\n");
            counter = 1;
            while (resultSet.next()) {
                System.out.println("Record " + counter + ": " +
                        resultSet.getString(1) + ", " +
                        resultSet.getString(2) + ", " +
                        resultSet.getString(3));
                counter++;
            }

            query = "SELECT * FROM GroupMembership";
            resultSet = statement.executeQuery(query);
            System.out.println("\nAfter the insert, data is...\n");
            counter = 1;
            while (resultSet.next()) {
                System.out.println("Record " + counter + ": " +
                        resultSet.getString(1) + ", " +
                        resultSet.getString(2) + ", " +
                        resultSet.getString(3));
                counter++;
            }

        } catch (Exception Ex) {
            System.out.println("Error running the sample queries.  Machine Error: " +
                    Ex.toString());
        }
        return true;
    }

    public ArrayList<String> searchForUser(String searcher)
    {
        try{
            ArrayList<String> results = new ArrayList<String>();
            statement=connection.createStatement();
            String[] searchTerms = searcher.split(" ");
            for(int i=0;i<searchTerms.length;i++)
            {
                query = "Select name From profile WHERE name = '"+searchTerms[i]+"'";
                resultSet = statement.executeQuery(query);
                while(resultSet.next())
                {
                    if(!results.contains(resultSet.getString(1)))
                        results.add(resultSet.getString(1));
                }
            }
            return results;


        }catch (Exception Ex) {
            System.out.println("Error running the sample queries.  Machine Error: " +
                    Ex.toString());
        }
        return null;
    }

    public ArrayList<String> threeDegrees(String userA, String userB)
    {

        ArrayList<String> results = new ArrayList<String>();
        ArrayList<String> friendsList = getFriendsList(userA);

        results.add(userA);
        if(friendsList.contains(userB))
        {

            return results;
        } else
        {
            for(int i=0;i<friendsList.size();i++)
            {
                ArrayList<String> fl2 = getFriendsList(friendsList.get(i));
                System.out.println(fl2);
                if(fl2.contains(userB)){
                    results.add(friendsList.get(i));
                    return results;

                }else{
                    for(int j=0;j<fl2.size();j++)
                    {
                        ArrayList<String> fl3 = getFriendsList(fl2.get(j));
                        if(fl3.contains(userB))
                        {
                            results.add(friendsList.get(i));
                            results.add(fl2.get(j));
                            return results;
                        } else{
                            for(int k =0;k<fl3.size();k++)
                            {
                                ArrayList<String> fl4 = getFriendsList(fl3.get(k));
                                if(fl4.contains(userB))
                                {
                                    results.add(friendsList.get(i));
                                    results.add(fl2.get(j));
                                    results.add(fl3.get(k));
                                    return results;
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
    private ArrayList<String> getFriendsList(String user)
    {
        ArrayList<String> usersFriends = new ArrayList<String>();
        try{
            statement = connection.createStatement();
            query = "SELECT * FROM friends where userID1='" + user + "'";
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                usersFriends.add(resultSet.getString(2));
            }
            query = "SELECT * FROM friends where userID2='" + user+"'";
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                usersFriends.add(resultSet.getString(1));
            }
            return usersFriends;
        }catch (Exception Ex) {
            System.out.println("Error running the sample queries.  Machine Error: " +
                    Ex.toString());
        }
        return usersFriends;
    }

    public boolean dropUser(String user)
    {
        try{
            statement = connection.createStatement();
            query = "DELETE FROM profile where userID = '"+user+"'";
            statement.executeQuery(query);
            return true;
        }catch(Exception Ex) {
            System.out.println("Error running sample queries.  Machine Error: " +
                    Ex.toString());
            return false;
        }
    }

    public boolean sendMessageToGroup(String sendingUser, String receivingGroup, String message)
    {
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
            updateStatement.setString(4, null);
            updateStatement.setString(5,  receivingGroup);
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
    public static void main(String args[]) {
        Phase2Java users = new Phase2Java();
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
        users.topMessages(2,4);
        users.closeConnection();

    }
}