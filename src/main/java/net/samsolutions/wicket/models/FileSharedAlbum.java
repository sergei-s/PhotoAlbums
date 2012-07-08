package net.samsolutions.wicket.models;

import net.samsolutions.hibernate.File;
import net.samsolutions.spring.FileService;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

@SuppressWarnings("serial")
public class FileSharedAlbum extends LoadableDetachableModel<File> {
	@SpringBean
	private FileService fileService;
	private int id;
	private String name;
	private int userId;
	
	public FileSharedAlbum(int id, String name, int userId) {
		this.id = id;
		this.name = name;
		this.userId = userId;
		Injector.get().inject(this);
	}
	
	protected File load() {
        return fileService.getFileShared(id, name, userId);
	}
}
