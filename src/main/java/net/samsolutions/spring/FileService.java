package net.samsolutions.spring;

import net.samsolutions.hibernate.Album;
import net.samsolutions.hibernate.File;

import java.util.ArrayList;

public interface FileService {

	void create(File file);

	void delete(File file);

	File getFileOwn(int id, String name, int userId);

	File getFileShared(int id, String name, int userId);

	void changeAlbum(File file, Album album);

	File getById(Integer id);
	
	ArrayList<File> getAlbumFiles(int albumId);

	ArrayList<File> getAlbumFilesPaging(int albumId, int first, int count);
	
	Long getCountAlbumFiles(int albumId);
}
