package net.samsolutions.wicket.pages.auth;

import net.samsolutions.hibernate.Album;
import net.samsolutions.spring.FileService;
import net.samsolutions.wicket.BlobFromFile;
import net.samsolutions.wicket.MySession;
import net.samsolutions.wicket.NavigateForm;
import net.samsolutions.wicket.models.AlbumModel;
import net.samsolutions.wicket.models.AlbumsModel;
import net.samsolutions.wicket.models.FileOwnModel;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.image.resource.BlobImageResource;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.sql.Blob;

@SuppressWarnings("serial")
public class Image extends BasePageAuth {

	@SpringBean
	private FileService fileService;
	private FileOwnModel fileOwnModel;
	private PageParameters parameters;
	private Album selected;

	public Image(final PageParameters parameters) {
		super(parameters);
		if (parameters.getNamedKeys().contains("fid") && parameters.getNamedKeys().contains("album")) {
			int id = parameters.get("fid").toInt();
			String name = parameters.get("album").toString();
			AlbumModel am = new AlbumModel(name);
			FileOwnModel fileOwnModel = new FileOwnModel(id, name, ((MySession) Session.get()).getuId());
			this.fileOwnModel = fileOwnModel;
			if (fileOwnModel.getObject() == null) {
				throw new RestartResponseException(ErrorPage404.class);
			}
			this.parameters = parameters;
			add(new NavigateForm<Void>("formNavigate", am.getObject().getId(), fileOwnModel.getObject().getId(),
					Image.class));
			add(createNonCachingImage());
			add(createFormDelete());
			add(createFormMove());
			add(new BookmarkablePageLink<Void>("linkBack", Upload.class, (new PageParameters()).add("album", name)));
		} else {
			throw new RestartResponseException(ErrorPage404.class);
		}
	}

	private NonCachingImage createNonCachingImage() {
        return new NonCachingImage("img", new BlobImageResource() {
			protected Blob getBlob() {
				return BlobFromFile.getBig(fileOwnModel.getObject());
			}
		});
	}

	private Form<Void> createFormDelete() {
        return new Form<Void>("formDelete") {
			@Override
			public void onSubmit() {
				fileService.delete(fileOwnModel.getObject());
				info(new StringResourceModel("image.deleted", this, null).getString());
				setResponsePage(new Upload(parameters.remove("fid")));
			}
		};
	}

	private Form<Void> createFormMove() {
		Form<Void> form = new Form<Void>("formMove") {
			@Override
			public void onSubmit() {
				fileService.changeAlbum(fileOwnModel.getObject(), selected);
				info(new StringResourceModel("image.moved", this, null).getString());
				setResponsePage(new Upload(parameters.remove("fid")));
			}
		};
		DropDownChoice<Album> listAlbums = new DropDownChoice<Album>("albums", new PropertyModel<Album>(this,
				"selected"), new AlbumsModel(fileOwnModel.getObject().getAlbum()), new ChoiceRenderer<Album>("name",
				"id"));
		listAlbums.setRequired(true);
		listAlbums.setLabel(new StringResourceModel("image.moveAlbum", this, null));
		form.add(listAlbums);
		form.add(new FeedbackPanel("feedback"));
		return form;
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		response.renderCSSReference("css/Image.css");
	}
}
