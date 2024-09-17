package cms;

import java.util.List;

public interface Authentication {

	boolean auth(User user);

	void createUser(User user);
	
	List<User> listUsers();
	
	 boolean updateUser(User user);
	 
	 boolean deleteUser(String username);
	 
	 boolean changePassword(User user, String newPassword);
}
