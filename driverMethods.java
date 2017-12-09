public boolean createGroup(){
        //String username, String name, String password, String email, String dob, Timestamp stamp
        System.out.println("Please Enter Group Name");
        String newName = keyboard.nextLine();
        System.out.println("Please Enter Group Description");
        String newDescription = keyboard.nextLine();
        System.out.println("Please Enter Group Limit");
        String newGroupLimit = keyboard.nextLine();
		//getuserID
        return connection.createGroup(userID,newName,newDescription, newGroupLimit);
    }
	
public boolean initiateAddingGroup(){
	System.out.println("Please enter Group Name");
	String groupToJoin = keyboard.nextLine();
	//getuserID
	System.out.println("Please enter Your message to send");
	String msg = keyboard.nextLine();
	
	return connection.createGroup(userID, groupToJoin, msg);
	
}

public boolean confirmMembership(String gName)
{
	HashMap<Integer, String> pendingGroupies = connection.getGroupRequests(gName);
	if(pendingGroupies.keySet().size()==0){
            System.out.println("you have no pending requests");
            return true;
        }else {
            System.out.println("Your Pending Groups Requests");
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