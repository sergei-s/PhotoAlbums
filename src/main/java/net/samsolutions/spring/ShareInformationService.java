package net.samsolutions.spring;

import net.samsolutions.hibernate.ShareInformation;
import net.samsolutions.hibernate.User;

import java.util.ArrayList;
import java.util.List;

public interface ShareInformationService {

	void create(ShareInformation shareInformation);

	void delete(ShareInformation shareInformation);

	List<ShareInformation> getShares(User userShared, User userSharedTo);

	ShareInformation getShare(String albumName, int userSharedToId, String userSharedEmail);

	ArrayList<ShareInformation> getAlbumShares(int albumId);

	ArrayList<ShareInformation> getUserShares(int userId);
}
