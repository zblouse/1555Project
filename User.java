//Group class for Social@panther
// = new java.text.SimpleDateFormat("yyyy-MM-dd")
import java.util.Date;
import java.sql.Timestamp;

public class User
{
	private String userID;
	private String name;
	private String password;
	private Date dateOfBirth;
	private Timestamp lastLogin;
	
	public User(String u, String n, String pass, Date dob, Timestamp lastL)
	{
		userID = u;
		name = n;
		password = pass;
		dateOfBirth = dob;
		lastLogin = lastL;
	}
	public String getName(){
		return name;
	}
}