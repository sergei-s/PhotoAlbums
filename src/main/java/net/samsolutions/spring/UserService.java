package net.samsolutions.spring;

import net.samsolutions.hibernate.User;

public interface UserService {

	void create(User user);

	void delete(User user);

	void update(User user);

	User getUser(String email, String password);

	User getUser(User userEmail);

	User getById(Integer id);
}
