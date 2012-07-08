package net.samsolutions.spring;

import net.samsolutions.hibernate.Album;

import java.util.ArrayList;

public interface AlbumService {

	void create(Album album);

	void delete(Album album);

	Album getAlbum(String name, int userId);

	void rename(Album album, String newName);

	Album getById(Integer id);
	ArrayList<Album> getAlbums(Integer id);
}
