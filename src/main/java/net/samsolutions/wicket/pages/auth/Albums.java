package net.samsolutions.wicket.pages.auth;

import net.samsolutions.hibernate.Album;
import net.samsolutions.spring.AlbumService;
import net.samsolutions.spring.UserService;
import net.samsolutions.wicket.AjaxDataView;
import net.samsolutions.wicket.AlbumListDataProvider;
import net.samsolutions.wicket.MyAjaxButton;
import net.samsolutions.wicket.MySession;
import net.samsolutions.wicket.models.AlbumsModelFull;
import net.samsolutions.wicket.pages.auth.share.Share;
import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class Albums extends BasePageAuth {

	@SpringBean
	private AlbumService albumService;
	@SpringBean
	private UserService userService;
	private static final int WINDOW_WIDTH = 500;
	private static final int WINDOW_HEIGHT = 120;
	private static final int ALBUMS_PER_PAGE = 10;

	public Albums(final PageParameters parameters) {
		super(parameters);
		add(createAlbumForm());
		add(new AjaxDataView("dataContainer", "navigator", createAlbumDataView()));
	}

	private DataView<Album> createAlbumDataView() {
		LoadableDetachableModel<ArrayList<Album>> ldm = new AlbumsModelFull();
		DataView<Album> dataView = new DataView<Album>("pageable", new AlbumListDataProvider(ldm.getObject().size())) {
			protected void populateItem(final Item<Album> item) {
				PageParameters pars = new PageParameters();
				pars.add("album", item.getModelObject().getName());
				BookmarkablePageLink<Void> bpl = new BookmarkablePageLink<Void>("link", Upload.class, pars);
				bpl.add(new Label("name", item.getModelObject().getName()));
				item.add(bpl);
				item.add(new Link<Void>("delete") {
					public void onClick() {
						info(new StringResourceModel("albums.deleted", this, null).getString());
						albumService.delete(item.getModelObject());
						setResponsePage(new Albums(null));
					}
				});
				final ModalWindow modal = new ModalWindow("modal");
				item.add(modal);
				modal.setPageCreator(new ModalWindow.PageCreator() {
					public Page createPage() {
						return new ModalRename(item.getModelObject(), modal);
					}
				});
				modal.setTitle(new StringResourceModel("albums.rename", this, null));
				modal.setResizable(false);
				modal.setInitialWidth(WINDOW_WIDTH);
				modal.setInitialHeight(WINDOW_HEIGHT);
				modal.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
					public void onClose(AjaxRequestTarget target) {
						setResponsePage(Albums.class);
					}
				});
				item.add(new AjaxLink<Void>("rename") {
					public void onClick(AjaxRequestTarget target) {
						modal.show(target);
					}
				});
				BookmarkablePageLink<Void> bp2 = new BookmarkablePageLink<Void>("share", Share.class, pars);
				item.add(bp2);
			}
		};
		ldm.detach();
		dataView.setItemsPerPage(ALBUMS_PER_PAGE);
		return dataView;
	}

	private Form<Album> createAlbumForm() {
		Form<Album> form = new Form<Album>("form") {
			@Override
			protected void onSubmit() {
				Album album = getModelObject();
				album.setUser(userService.getById(((MySession) Session.get()).getuId()));
				try {
					albumService.create(album);
					info(new StringResourceModel("albums.created", this, null).getString());
					setResponsePage(new Albums(null));
				} catch (RuntimeException e) {
					error(new StringResourceModel("albums.existed", this, null).getString());
				}
			}
		};
		Album album = new Album();
		form.setDefaultModel(new Model<Album>(album));
		RequiredTextField<String> albumName = new RequiredTextField<String>("AlbumName", new PropertyModel<String>(
				album, "name"));
		albumName.setLabel(new StringResourceModel("albums.albumNameField", this, null));
		form.add(albumName);
		FeedbackPanel feedback = new FeedbackPanel("feedback");
		feedback.setOutputMarkupId(true);
		form.add(feedback);
		form.add(new MyAjaxButton("ajax-button", form, feedback));
		return form;
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		response.renderCSSReference("css/Albums.css");
	}
}
