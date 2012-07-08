package net.samsolutions.hibernate;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.ArrayList;

public class FileDaoImpl extends HibernateDaoSupport implements FileDao {

	public void create(File file) {
		getHibernateTemplate().save(file);
	}

	public File getById(Integer id) {
		return getHibernateTemplate().get(File.class, id);
	}

	public File getFileOwn(int id, String name, int userId) {
		@SuppressWarnings("unchecked")
		ArrayList<File> list = (ArrayList<File>) getHibernateTemplate().findByCriteria(
				DetachedCriteria.forClass(File.class)
					.add(Restrictions.eq("id", id))
					.createCriteria("album")
						.add(Restrictions.eq("name", name))
					.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY));
		if ((list.size() == 1) && (( list.get(0)).getAlbum().getUser().getId() == userId)) {
			  return list.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public File getFileShared(int id, String name, int userId) {
		File file;
		ArrayList<File> list = (ArrayList<File>) getHibernateTemplate().findByCriteria(
				DetachedCriteria.forClass(File.class)
						.add(Restrictions.eq("id", id))
						.createCriteria("album")
							.add(Restrictions.eq("name", name))
						.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY));
		if (list.size() == 1) {
			  file = list.get(0);
		} else {
			file = null;
		}
		if (file != null) {
			ArrayList<ShareInformation> list2 = (ArrayList<ShareInformation>) getHibernateTemplate()
				.findByCriteria(
						DetachedCriteria.forClass(ShareInformation.class)
						.createAlias("album", "al").createAlias("user", "us")
						.add(Restrictions.eq("al.id", file.getAlbum().getId()))
						.add(Restrictions.eq("us.id", userId))
						.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY));
			if ((list2.isEmpty())) {
				return null;
			} else {
				return file;
			}
		} else {
			return file;
		}
	}

	public void delete(File file) {
		getHibernateTemplate().delete(file);
	}

	public void changeAlbum(File file, Album album) {
		getHibernateTemplate().update(file);
		file.setAlbum(album);
	}

	@SuppressWarnings("unchecked")
	public ArrayList<File> getAlbumFiles(int albumId) {
        return (ArrayList<File>) getHibernateTemplate().findByCriteria(
				DetachedCriteria.forClass(File.class)
				.createCriteria("album")
					.add(Restrictions.eq("id", albumId))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY));
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<File> getAlbumFilesPaging(int albumId, int first, int count) {
        return (ArrayList<File>) getHibernateTemplate().getSessionFactory().getCurrentSession()
			.createCriteria(File.class)
				.createCriteria("album")
					.add(Restrictions.eq("id", albumId))
				.addOrder(Order.asc("id"))
				.setFirstResult(first)
				.setMaxResults(count)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.list();
	}
	
	@SuppressWarnings("unchecked")
	public Long getCountAlbumFiles(int albumId) {
		ArrayList<Long> list = (ArrayList<Long>) getHibernateTemplate().findByCriteria(
				DetachedCriteria.forClass(File.class)
				.createCriteria("album")
					.add(Restrictions.eq("id", albumId))
				.setProjection(Projections.rowCount())
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY));
		return list.get(0);
	}
}
