package net.samsolutions.wicket.models;

import net.samsolutions.hibernate.File;
import net.samsolutions.spring.FileService;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class FilesModelPaging extends LoadableDetachableModel<ArrayList<File>> {
	@SpringBean
	private FileService fileService;
	private int id;
	private int first;
	private int count;
	
	public FilesModelPaging(int id, int first, int count) {
		this.id = id;
		this.first = first;
		this.count = count;
		Injector.get().inject(this);
	}
	
	@Override
	protected ArrayList<File> load() {
        return new ArrayList<File>(fileService.getAlbumFilesPaging(id, first, count));
	}
}
