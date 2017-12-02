
import java.sql.*;
import java.util.ArrayList;
public class GroupStuff
{
	private static Connection connection; //used to hold the jdbc connection to the DB
    private Statement statement; //used to create an instance of the connection
    private ResultSet resultSet; //used to hold the result of your query (if one
    // exists)
    private String query;  //this will hold the query we are using
    private String username, password;
    public GroupStuff() {
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
			query = "Select userID From profile WHERE userID = '"+searchTerms[i]+"'";
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
        GroupStuff demo = new GroupStuff();
        //demo.createGroup("zab30","trying ssdfsdhard","testDesc", 5);
		//demo.initiateAddingGroup("uav97","trying ssdfsdhard", "hi");
		//demo.confirmMembership("uav97","trying ssdfsdhard");
		//System.out.println(demo.searchForUser("uav97 zab30"));
		//System.out.println(demo.threeDegrees("uav97", "zab32"));
		//demo.dropUser("zab33");
		demo.closeConnection();
		
		
		
    }
}
