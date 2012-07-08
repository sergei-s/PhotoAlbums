package net.samsolutions.wicket.models;

import net.samsolutions.hibernate.File;
import net.samsolutions.spring.FileService;
import net.samsolutions.utils.FileComparator;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.Collections;

@SuppressWarnings("serial")
public class FilesModel extends LoadableDetachableModel<ArrayList<File>> {
	
	@SpringBean
	private FileService fileService;
	private int id;
	
	public FilesModel(int id) {
		this.id = id;
		Injector.get().inject(this);
	}
	
	@Override
	protected ArrayList<File> load() {
		ArrayList<File> list = new ArrayList<File>(fileService.getAlbumFiles(id));
		Collections.sort(list, new FileComparator());
		return list;
	}
}
