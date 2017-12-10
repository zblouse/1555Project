//Jacob Winkler and Zach Blouse
import java.util.Scanner;
import java.util.Calendar;
import java.util.ArrayList;
import java.sql.*;
import java.util.HashMap;
import java.util.regex.*;
public class Driver{
    private boolean loggedIn=false;
    private Scanner keyboard = new Scanner(System.in);
    private Phase2Java connection;
    private User thisUser;
    private User someOtherUser;
    private boolean loggedOut=false;
	private boolean clean=true;
    public static void main(String args[]){
       Driver thisDriver = new Driver();
    }
    public Driver(){

        connection=new Phase2Java();
        //user is not logged in so only allow them to log in or create a new user account
        while(!loggedIn){
            welcomePrompt();
        }
        while(!loggedOut){
            mainPrompt();
        }

        connection.closeConnection();
    }
    public void welcomePrompt(){
        System.out.println("Welcome to Social@Panther\nPress 0 to Create a new Account\nPress 1 to Login");
        int response= Integer.parseInt(keyboard.nextLine());
        if(response==0){
            if(!createUser()){
                System.out.println("Error Creating User");
            }
        }else if(response==1){
            loginPrompt();
        }
    }
    public void mainPrompt(){
        System.out.println("Welcome "+thisUser.getName()+"\nPress 0 to Initiate Friendship");
        System.out.println("Press 1 to Confirm Friendship\nPress 2 to Display Friends");
        System.out.println("Press 3 to Create Group\nPress 4 to Initiate Adding Group");
        System.out.println("Press 5 to Send Message to user\nPress 6 to Display Messages");
        System.out.println("Press 7 to Display New Messages\nPress 8 to Search for a User");
        System.out.println("Press 9 to do Three Degrees\nPress 10 to view Top Messages");
        System.out.println("Press 11 to Drop user\nPress 12 to Log Out");
        int response= Integer.parseInt(keyboard.nextLine());
        if(response==0){
            initiateFriendship();
        }else if(response==1){
            confirmFriendship();
        }else if(response==2){
            displayFriends();
        }else if(response==3){
            createGroup();
        }else if(response==4){
            initiateAddingGroup();
        }else if(response==5){
            sendMessageToUser();
        }else if(response==6){
            displayMessages();
        }else if(response==7){
            displayNewMessages();
        }else if(response==8){
            searchForUser();
        }else if(response==9){
            threeDegrees();
        }else if(response==10){
            topMessages();
        }else if(response==11){
            dropUser();
        }else if(response==12){
            logout();
        }

    }
    public boolean createUser(){
        //String username, String name, String password, String email, String dob, Timestamp stamp
        System.out.println("Please Enter Your Name");
        String newName = keyboard.nextLine();
        System.out.println("Please Enter Desired Username");
        String newUsername = keyboard.nextLine();
        System.out.println("Please Enter Your Password");
        String newPassword = keyboard.nextLine();
        System.out.println("Please Enter Your Email Address");
        String newEmail = keyboard.nextLine();
        System.out.println("Please enter your Date of Birth in the form YYYY-MM-DD");
        String newBirth = keyboard.nextLine();
        Timestamp blankStamp = new Timestamp(87);

        return connection.createUser(newUsername,newName,newPassword,newEmail,newBirth,blankStamp);
    }
    public boolean loginPrompt(){
        System.out.println("Username");
        String newUsername = keyboard.nextLine();
        System.out.println("Password");
        String newPassword = keyboard.nextLine();
        boolean validLogin = connection.userLogin(newUsername,newPassword);
        if(validLogin){
            loggedIn=true;
            thisUser = connection.retrieveProfile(newUsername);
            return true;
        }else{
            System.out.println("Invalid Username or Password");
            return false;
        }
    }
    public boolean initiateFriendship(){
        System.out.println("Enter the username of the user you  wish to send a friend request to");
        String friendUsername = keyboard.nextLine();
        System.out.println("Enter the message you would like to send to this user");
        String message = keyboard.nextLine();
        while(message.length()>200){
            System.out.println("That message is too long to send");
            System.out.println("Enter the message you would like to send to this user");
            message = keyboard.nextLine();
        }
        if(connection.initiateFriendship(thisUser.getUsername(),friendUsername,message)){
            System.out.println("Sent request");
            return true;
        }else{
            System.out.println("Request Failed");
            return false;
        }
    }
    public boolean confirmFriendship(){
       HashMap<Integer, String> pendingFriends = connection.getFriendRequests(thisUser.getUsername());
        if(pendingFriends.keySet().size()==0){
            System.out.println("you have no pending requests");
            return true;
        }else {
            System.out.println("Your Pending Friend Requests");
            for (int thisKey : pendingFriends.keySet()) {
                System.out.println(thisKey + ": " + pendingFriends.get(thisKey));
            }
            System.out.println("Please enter the number corresponding to the friend request you would like to accept");
            int response= Integer.parseInt(keyboard.nextLine());
            if(connection.confirmFriendship(thisUser.getUsername(),pendingFriends.get(response))){
                System.out.println("Successfully Confirmed Friend Request");
                return true;
            }else{
                System.out.println("Failed to Confirm Friend Request");
                return false;
            }

        }
    }
    public boolean displayFriends(){
        if(connection.displayFriends(thisUser.getUsername())){
            return true;
        }else{
            System.out.println("Failed to Display Friends");
            return false;
        }
    }
    public boolean createGroup(){
         //String username, String name, String password, String email, String dob, Timestamp stamp
        System.out.println("Please Enter Group Name");
        String newName = keyboard.nextLine();
        System.out.println("Please Enter Group Description");
        String newDescription = keyboard.nextLine();
        System.out.println("Please Enter Group Limit");
        String newGroupLimit = keyboard.nextLine();
		int groupLim = Integer.parseInt(newGroupLimit);
		//getuserID
        if(connection.createGroup(thisUser.getUsername(),newName,newDescription, groupLim))
		{
			System.out.println("Successfully Created Group");
			return true;
		}else
		{
			System.out.println("Failed to Create Group");
			return false;
		}
    }
    public boolean initiateAddingGroup(){
        System.out.println("Please enter Group Name");
		String groupToJoin = keyboard.nextLine();
		System.out.println("Please enter Your message to send");
		String msg = keyboard.nextLine();
		if(connection.initiateAddingGroup(thisUser.getUsername(), groupToJoin, msg))
		{
			System.out.println("Successfully sent join request");
			return true;
		}else 
		{
			System.out.println("Failed to send join request");
			return false;
		}
		
    }
    public boolean sendMessageToUser(){
        System.out.println("Enter the username of the user you  wish to send a message to");
        String friendUsername = keyboard.nextLine();
        System.out.println("Enter the message you would like to send to this user");
        String message = keyboard.nextLine();
        while(message.length()>200){
            System.out.println("That message is too long to send");
            System.out.println("Enter the message you would like to send to this user");
            message = keyboard.nextLine();
        }
        if(connection.sendMessageToUser(thisUser.getUsername(),friendUsername,message)){
            System.out.println("Successfully Sent Message");
            return true;
        }else{
            System.out.println("Error Sending Message");
            return false;
        }
    }
    public boolean displayMessages(){
        if(connection.displayMessages(thisUser.getUsername())){
            return true;
        }else{
            System.out.println("Error displaying Messages");
            return false;
        }
    }
    public boolean displayNewMessages(){
        if(connection.displayNewMessages(thisUser.getUsername())){
            return true;
        }else{
            System.out.println("Error displaying Messages");
            return false;
        }
    }
    public boolean searchForUser(){
        System.out.println("Enter User to search for. To search for multiple users, add a /between names(Ex. first last/first last)");
		String userSearch = keyboard.nextLine();
		ArrayList<String> results = connection.searchForUser(userSearch);
		if(results.size()!=0){
			for(int i=0;i<results.size();i++)
			{
				System.out.println(results.get(i));
			}
	    return true;
		}else{
		return false;
		}
    }
    public boolean threeDegrees(){
        System.out.println("Please enter the username of the first user in the Three Degrees search");
        String userA = keyboard.nextLine();
        System.out.println("Please enter the username of the second user in the Three Degrees search");
        String userB = keyboard.nextLine();
        ArrayList<String> results = connection.threeDegrees(userA,userB);
        if(results == null){
            System.out.println("No result found");
            return false;
        }
        if(results.size()!=0){
            System.out.println("Three Degree search results:");
            for(int i=0;i<results.size();i++)
            {
                System.out.println(results.get(i));
            }
            return true;
        }else{
            System.out.println("Three Degrees search was unsuccessful");
            return false;
        }
    }
    public boolean topMessages(){
       System.out.println("Please enter the number of users you would like ranked");
       int numUsers = Integer.parseInt(keyboard.nextLine());
       System.out.println("Please enter the number of months you would like the ranking for");
       int numMonths = Integer.parseInt(keyboard.nextLine());
       if(connection.topMessages(numUsers,numMonths)){
           return true;
       }else{
           return false;
       }

    }
    public boolean dropUser(){
       System.out.println("Enter the user you would like to drop");
       String droppedUser = keyboard.nextLine();
       if(connection.dropUser(droppedUser)){
           System.out.println("User successfully dropped");
           return true;
       }else{
           System.out.println("Error dropping user");
           return true;
       }
    }
    public boolean logout(){
        if(connection.userLogOut(thisUser.getUsername())){
            loggedOut=true;
            System.out.println("Thank you for using Social@Panther");
            return true;
        }else{
            System.out.println("Error logging you out");
            return false;
        }
    }
	
	private boolean scrubber(String input)
	{
		Pattern p = Pattern.compile("[\\w ]*+");
		Matcher m = p.matcher(input);
		if(m.matches()){
		return true;
		}else{
			System.out.println("Bad Input Identified, please try again");
			return false;
		}
	}

}