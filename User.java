//Group class for Social@panther
// = new java.text.SimpleDateFormat("yyyy-MM-dd")
import java.util.Date;
import java.sql.Timestamp;
public class User
{
	String UserID;
	String name;
	String password;
	java.text.SimpleDateFormat dateOfBirth;
	Timestamp lastLogin;
	
	public User(String u, String n, String pass, java.text.simpleDateFormat dob, Timestamp lastL)
	{
		userID = u;
		name = n;
		password = pass;
		dateOfBirth = dob;
		lastLogin = lastL;
	}
}