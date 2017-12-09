//Jacob Winkler and Zach Blouse
import java.util.Scanner;
import java.util.Calendar;
public class Driver{
    private boolean loggedIn=false;
    private Scanner keyboard = new Scanner(System.in);
    private Phase2Java connection;
    public static void main(String args[]){
       Driver thisDriver = new Driver();
    }
    public Driver(){

        connection=new Phase2Java();
        //user is not logged in so only allow them to log in or create a new user account
        if(!loggedIn){
            welcomePrompt();
        }

        connection.closeConnection();
    }
    public void welcomePrompt(){
        System.out.println("Welcome to Social@Panther\nPress 0 to Create a new Account\nPress 1 to Login");
        int response= Integer.parseInt(keyboard.nextLine());
        if(response==0){
            if(!createUser()){
                System.out.println("Error Creating User");
                System.exit(0);
            }
        }else if(response==1){
            loginPrompt();
        }
    }
    public boolean createUser(){
        //String username, String name, String password, String email, String dob, Timestamp stamp
        System.out.println("Please Enter Desired Username");
        String newUsername = keyboard.nextLine();
        System.out.println("Please Enter Your Password");
        String newPassword = keyboard.nextLine();
        System.out.println("Please Enter Your Email Address");
        String newEmail = keyboard.nextLine();
        System.out.println("Please enter your Date of Birth in the form YYYY-MM-DD");
        String newBirth = keyboard.nextLine();
        Timestamp blankStamp = new Timestamp(87);
        connection.createUser(newUsername,newPassword,newEmail,newBirth,blankStamp);
    }

}