package net.samsolutions.wicket.pages.auth.share;

import net.samsolutions.wicket.BlobFromFile;
import net.samsolutions.wicket.MySession;
import net.samsolutions.wicket.NavigateForm;
import net.samsolutions.wicket.models.FileSharedAlbum;
import net.samsolutions.wicket.pages.auth.BasePageAuth;
import net.samsolutions.wicket.pages.auth.ErrorPage404;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.image.resource.BlobImageResource;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.sql.Blob;

@SuppressWarnings("serial")
public class SharedBig extends BasePageAuth {

	private FileSharedAlbum fileSharedAlbum;

	public SharedBig(final PageParameters parameters) {
		super(parameters);
		if (parameters.getNamedKeys().contains("fid") && parameters.getNamedKeys().contains("album")) {
			int id = parameters.get("fid").toInt();
			String name = parameters.get("album").toString();
			FileSharedAlbum fileSharedAlbum = new FileSharedAlbum(id, name, ((MySession) Session.get()).getuId());
			this.fileSharedAlbum = fileSharedAlbum;
			if ((fileSharedAlbum.getObject() == null)
					|| (!(fileSharedAlbum.getObject().getAlbum().getName().equals(name)))) {
				throw new RestartResponseException(ErrorPage404.class);
			}
			add(new NavigateForm<Void>("formNavigate", fileSharedAlbum.getObject().getAlbum().getId(), fileSharedAlbum
					.getObject().getId(), SharedBig.class));
			add(createNonCachingImage());
			PageParameters newPars = new PageParameters();
			newPars.add("album", name);
			newPars.add("user", fileSharedAlbum.getObject().getAlbum().getUser().getEmail());
			add(new BookmarkablePageLink<Void>("linkBack", SharedFiles.class, newPars));
		} else {
			throw new RestartResponseException(ErrorPage404.class);
		}
	}

	private NonCachingImage createNonCachingImage() {
        return new NonCachingImage("img", new BlobImageResource() {
			protected Blob getBlob() {
				return BlobFromFile.getBig(fileSharedAlbum.getObject());
			}
		});
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		response.renderCSSReference("css/SharedBig.css");
	}
}
