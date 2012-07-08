package net.samsolutions.spring;

import net.samsolutions.hibernate.User;
import net.samsolutions.hibernate.UserDao;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class UserServiceImpl implements UserService {

	private UserDao userDao;

	public UserDao getUserDao() {
		return this.userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void create(User user) {
		userDao.create(user);
	}

	public void delete(User user) {
		userDao.delete(user);
	}

	public void update(User user) {
		userDao.update(user);
	}

	public User getUser(String email, String password) {
		return userDao.getUser(email, password);
	}

	public User getUser(User userEmail) {
		return userDao.getUser(userEmail);
	}

	public User getById(Integer id) {
		return userDao.getById(id);
	}

}
