import java.util.Date;
import java.util.UUID;

public class session {
	private UUID sessionID;
	private int version;
	private String message;
	private Date expirationTimestamp;
	
	session(){
		sessionID = UUID.randomUUID();;
		version = 1;
		message = "Hello User";
		expirationTimestamp = new Date((new Date()).getTime() + 10000L);
	}
	
	public UUID getSessionID(){
		return sessionID;
	}
	
	public int getVersion(){
		return version;
	}
	
	public String getMessege(){
		return message;
	}
	
	public Date getExpiration(){
		return expirationTimestamp;
	}
	
	public void setVersion(int newVersion){
		version = newVersion;
	}
	
	public void setMesssage(String newMessage){
		message = newMessage;
	}
	
	public void setExpiration(Date newExpiration){
		expirationTimestamp = newExpiration;
	}
}
