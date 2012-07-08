package net.samsolutions.wicket;

import net.samsolutions.hibernate.Album;
import net.samsolutions.spring.AlbumService;
import net.samsolutions.wicket.models.AlbumsModelFull;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.Iterator;

@SuppressWarnings("serial")
public class AlbumListDataProvider implements IDataProvider<Album> {

	@SpringBean
	private AlbumService albumService;
	private int size;

	public AlbumListDataProvider(int size) {
		this.size = size;
		Injector.get().inject(this);
	}

	public void detach() {
	}

	public Iterator<? extends Album> iterator(int first, int count) {
		LoadableDetachableModel<ArrayList<Album>> ldm = new AlbumsModelFull();
		int toIndex = first + count;
		if (toIndex > ldm.getObject().size()) {
			toIndex = ldm.getObject().size();
		}
		return ldm.getObject().subList(first, toIndex).iterator();
	}

	public int size() {
		return this.size;
	}

	public IModel<Album> model(Album object) {
	    final Integer id = object.getId();
	    return new LoadableDetachableModel<Album>() {
	        @Override
	        protected Album load() {
	            return albumService.getById(id);
	        }
	    };
	}
}
