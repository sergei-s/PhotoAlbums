package net.samsolutions.hibernate;

import net.samsolutions.utils.MD5;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.ArrayList;


public class UserDaoImpl extends HibernateDaoSupport implements UserDao {

	public void create(User user) {
		getHibernateTemplate().save(user);
	}

	public void delete(User user) {
		getHibernateTemplate().delete(user);
	}

	public void update(User user) {
		getHibernateTemplate().update(user);
	}

	public User getUser(String email, String password) {
		@SuppressWarnings("unchecked")
		ArrayList<User> list = (ArrayList<User>) getHibernateTemplate().findByCriteria(
				DetachedCriteria.forClass(User.class)
					.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
					.add(Restrictions.eq("email", email).ignoreCase())
					.add(Restrictions.eq("password", MD5.getHash(password))));
		if (list.size() == 1) {
			  return list.get(0);
		} else {
			return null;
		}
	}

	public User getUser(User userEmail) {
		@SuppressWarnings("unchecked")
		ArrayList<User> list = (ArrayList<User>) getHibernateTemplate().findByCriteria(
				DetachedCriteria.forClass(User.class)
				.add(Restrictions.eq("email", userEmail.getEmail()))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY));
		if (list.size() == 1) {
			  return list.get(0);
		} else {
			return null;
		}
	}

	public User getById(Integer id) {
		return getHibernateTemplate().get(User.class, id);
	}
}
