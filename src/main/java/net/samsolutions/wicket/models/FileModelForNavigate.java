package net.samsolutions.wicket.models;

import net.samsolutions.hibernate.File;
import net.samsolutions.spring.FileService;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

@SuppressWarnings("serial")
public class FileModelForNavigate extends LoadableDetachableModel<File> {
	private Integer id;
	@SpringBean
	private FileService fileService;
	
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public FileModelForNavigate(Integer id) {
		this.id = id;
		Injector.get().inject(this);
	}
	
	@Override
	protected File load() {
		if (this.id == -1) {
			return null;
		} else {
			System.out.println(this.id);
			return fileService.getById(this.id);
		}
		
	}
}
