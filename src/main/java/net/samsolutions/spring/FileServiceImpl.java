package net.samsolutions.spring;

import net.samsolutions.hibernate.Album;
import net.samsolutions.hibernate.File;
import net.samsolutions.hibernate.FileDao;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Transactional
public class FileServiceImpl implements FileService {

	private FileDao fileDao;

	public FileDao getFileDao() {
		return this.fileDao;
	}

	public void setFileDao(FileDao fileDao) {
		this.fileDao = fileDao;
	}

	public void create(File file) {
		fileDao.create(file);
	}

	public void delete(File file) {
		fileDao.delete(file);
	}

	public File getFileOwn(int id, String name, int userId) {
		return fileDao.getFileOwn(id, name, userId);
	}

	public File getFileShared(int id, String name, int userId) {
		return fileDao.getFileShared(id, name, userId);
	}

	public void changeAlbum(File file, Album album) {
		fileDao.changeAlbum(file, album);
	}

	public File getById(Integer id) {
		return fileDao.getById(id);
	}
	
	public ArrayList<File> getAlbumFiles(int albumId) {
		return fileDao.getAlbumFiles(albumId);
	}

	public ArrayList<File> getAlbumFilesPaging(int albumId, int first, int count) {
		return fileDao.getAlbumFilesPaging(albumId, first, count);
	}
	
	public Long getCountAlbumFiles(int albumId) {
		return fileDao.getCountAlbumFiles(albumId);
	}
}
