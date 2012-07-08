package net.samsolutions.hibernate;

import java.util.ArrayList;

public interface FileDao extends GenericDao<File> {

	/**
	 * @param id id of file
	 * @param name name of file
	 * @param userId id of user, file belongs to
	 * @return File, that belongs to this userId
	 */
	File getFileOwn(int id, String name, int userId);

	/**
	 * @param id id of file
	 * @param name name of file
	 * @param userId Id of user, file shared to
	 * @return file with this id and name, that shared to user with userIdS
	 */
	File getFileShared(int id, String name, int userId);

	/**
	 * @param file file, album of what will be changed
	 * @param album change to this
	 */
	void changeAlbum(File file, Album album);
	File getById(Integer id);
	ArrayList<File> getAlbumFiles(int albumId);
	ArrayList<File> getAlbumFilesPaging(int albumId, int first, int count);
	Long getCountAlbumFiles(int albumId);
}
