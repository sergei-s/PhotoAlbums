package net.samsolutions.hibernate;

import java.util.ArrayList;
import java.util.List;

public interface ShareInformationDao extends GenericDao<ShareInformation> {

	/**
	 * @param userShared user, that shared albums
	 * @param userSharedTo user, to whom shared
	 * @return corresponding shares
	 */
	List<ShareInformation> getShares(User userShared, User userSharedTo);

	/**
	 * @param albumName name of album
	 * @param userSharedToId Id of user album shared to
	 * @param userSharedEmail email of user, that shares
	 * @return share
	 */
	ShareInformation getShare(String albumName, int userSharedToId, String userSharedEmail);

	ArrayList<ShareInformation> getAlbumShares(int albumId);

	ArrayList<ShareInformation> getUserShares(int userId);
}
