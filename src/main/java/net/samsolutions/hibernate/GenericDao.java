package net.samsolutions.hibernate;

public interface GenericDao<T> {

	void create(T t);

	void delete(T t);
}
