package net.samsolutions.hibernate;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.ArrayList;
import java.util.List;

public class ShareInformationDaoImpl extends HibernateDaoSupport implements ShareInformationDao {

	public void create(ShareInformation shareInformation) {
		getHibernateTemplate().save(shareInformation);
	}

	public void delete(ShareInformation shareInformation) {
		getHibernateTemplate().delete(shareInformation);
	}

	@SuppressWarnings("unchecked")
	public List<ShareInformation> getShares(User userShared, User userSharedTo) {
		return (List<ShareInformation>) getHibernateTemplate().findByCriteria(
				DetachedCriteria.forClass(ShareInformation.class)
									.createAlias("album", "al")
						.createAlias("al.user", "alus")
					.createAlias("user", "us")
					.add(Restrictions.eq("alus.id", userShared.getId()))
					.add(Restrictions.eq("us.id", userSharedTo.getId()))
					.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY));
	}

	public ShareInformation getShare(String albumName, int userSharedToId, String userSharedEmail) {
		@SuppressWarnings("unchecked")
		ArrayList<ShareInformation> list = (ArrayList<ShareInformation>) getHibernateTemplate().findByCriteria(
			DetachedCriteria.forClass(ShareInformation.class)
					.createAlias("album", "al")
					.createAlias("al.user", "alus")
					.createAlias("user", "us")
					.add(Restrictions.eq("al.name", albumName))
					.add(Restrictions.eq("us.id", userSharedToId))
					.add(Restrictions.eq("alus.email", userSharedEmail))
					.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY));
		if (list.size() == 1) {
			  return list.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public ArrayList<ShareInformation> getAlbumShares(int albumId) {
        return (ArrayList<ShareInformation>) getHibernateTemplate().findByCriteria(
				DetachedCriteria.forClass(ShareInformation.class)
				.createCriteria("album")
					.add(Restrictions.eq("id", albumId))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY));
	}

	@SuppressWarnings("unchecked")
	public ArrayList<ShareInformation> getUserShares(int userId) {
        return (ArrayList<ShareInformation>) getHibernateTemplate().findByCriteria(
				DetachedCriteria.forClass(ShareInformation.class)
				.createCriteria("user")
					.add(Restrictions.eq("id", userId))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY));
	}
}
