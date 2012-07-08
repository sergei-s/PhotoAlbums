package net.samsolutions.hibernate;

public interface UserDao extends GenericDao<User> {

	void update(User user);

	/**
	 * @param email email of user
	 * @param password password of user
	 * @return user instance if exists or null
	 */
	User getUser(String email, String password);

	/**
	 * @param userEmail email of user
	 * @return user if exists or null
	 */
	User getUser(User userEmail);

	User getById(Integer id);
}
