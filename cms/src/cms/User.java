package cms;

public class User {
	String username;
	String password;
	
	public User(String l, String p) {
		this.username = l;
		this.password = p;
	}
	
	public User() {
		
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}	
}
