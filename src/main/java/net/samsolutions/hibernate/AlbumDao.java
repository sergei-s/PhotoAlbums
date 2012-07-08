package net.samsolutions.hibernate;

import java.util.ArrayList;

public interface AlbumDao extends GenericDao<Album> {

	/**
	 * @param name name of Album
	 * @param userId album's user id
	 * @return album instance if exists or null
	 */
	Album getAlbum(String name, int userId);

	/**
	 * Changes album name
	 * @param album this album's name will be changed
	 * @param newName change name to
	 */
	void rename(Album album, String newName);
	Album getById(Integer id);
	ArrayList<Album> getAlbums(Integer id);
}
