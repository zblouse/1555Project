public class Message{
    private String fromUser;
    private String toUser;
    private String message;
    public Message(String newFromUser, String newToUser, String newMessage){
        fromUser=newFromUser;
        toUser=newToUser;
        message=newMessage;
    }
    public String getFromUser(){
        return fromUser;
    }
    public String getToUser(){
        return toUser;
    }
    public String getMessage(){
        return message;
    }
}