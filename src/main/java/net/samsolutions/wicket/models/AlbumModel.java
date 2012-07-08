package net.samsolutions.wicket.models;

import net.samsolutions.hibernate.Album;
import net.samsolutions.spring.AlbumService;
import net.samsolutions.wicket.MySession;
import org.apache.wicket.Session;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

@SuppressWarnings("serial")
public class AlbumModel extends LoadableDetachableModel<Album> {
	
	@SpringBean
	private AlbumService albumService;
	private String name;
	
	public AlbumModel(String name) {
		this.name = name;
		Injector.get().inject(this);
	}
	
	protected Album load() {
		return this.albumService.getAlbum(name, ((MySession) Session.get()).getuId());
	}
}
