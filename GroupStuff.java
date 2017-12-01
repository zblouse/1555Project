
import java.sql.*;
public class GroupStuff
{
	private Connection connection; //used to hold the jdbc connection to the DB
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
	public boolean createGroup(){
	int counter = 1;
        try {
            statement = connection.createStatement(); //create an instance
            query = "SELECT * FROM Groups"; //sample query one

            resultSet = statement.executeQuery(query); //run the query on the DB table
      /*the results in resultSet have an odd quality.  The first row in result
      set is not relevant data, but rather a place holder.  This enables us to
      use a while loop to go through all the records.  We must move the pointer
      forward once using resultSet.next() or you will get errors*/

            while (resultSet.next()) //this not only keeps track of if another record
            //exists but moves us forward to the first record
            {
                System.out.println("Record " + counter + ": " +
                        resultSet.getString(1) + ", " + //since the first item was of type
                        //string, we use getString of the
                        //resultSet class to access it.
                        //Notice the one, that is the
                        //position of the answer in the
                        //resulting table
                        resultSet.getString(2) + ", " +   //since second item was number(10),
                        //we use getLong to access it
                        resultSet.getString(3)); //since type date, getDate.
                counter++;
            }


      /*Now, we show an insert, using preparedStatement. Of course for this you can also write the query directly as the above case with select, and vice versa. */

            String groupName="TestNameI";
			String groupID="TestIDI";
			String description="TestDesc";


           // java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");

            //java.sql.Date bday = new java.sql.Date(df.parse("1990-01-20").getTime());

            query = "insert into Groups values (?,?,?)";

            PreparedStatement updateStatement = connection.prepareStatement(query);
            updateStatement.setString(1, groupName);
            updateStatement.setString(2, groupID);
            updateStatement.setString(3, description);

            updateStatement.executeUpdate();


      /* We can also so the insert statement directly as follows:

       query = "INSERT INTO Test VALUES ('Tester', 111111112, '1/Nov/03')";
      int result = statement.executeUpdate(query); //executing update returns
      //either the row count for INSERT, UPDATE or DELETE or 0 for SQL
      //statements that return nothing

      */

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
                        resultSet.getString(3));
                counter++;
            }

            connection.close();

        } catch (Exception Ex) {
            System.out.println("Error running the sample queries.  Machine Error: " +
                    Ex.toString());
        }

        System.out.println("Good Luck");
        return true;
    }
	
	
	public boolean initiateAddingGroup(String uID, String gID)
	{
		try {
			int limit;
            statement = connection.createStatement(); //create an instance
            query = "SELECT grouplimit FROM Groups WHERE groupId = '"+gID+"'"; //sample query one
            ResultSet gLimit = statement.executeQuery(query); //run the query on the DB table
			limit = gLimmit.getInt("grouplimit");
            query = "SELECT COUNT(*) AS numMems FROM groupMembership WHERE gID = '"+gID+"'";
            resultSet = statement.executeQuery(query);
       

            connection.close();

        } catch (Exception Ex) {
            System.out.println("Error running the sample queries.  Machine Error: " +
                    Ex.toString());
        }

        System.out.println("Good Luck");
        return true;
    }
	}
	public static void main(String args[]) {
        GroupStuff demo = new GroupStuff();
        demo.createGroup();
    }
}
