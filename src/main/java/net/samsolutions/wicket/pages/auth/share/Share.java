package net.samsolutions.wicket.pages.auth.share;

import net.samsolutions.hibernate.Album;
import net.samsolutions.hibernate.ShareInformation;
import net.samsolutions.hibernate.User;
import net.samsolutions.spring.AlbumService;
import net.samsolutions.spring.ShareInformationService;
import net.samsolutions.spring.UserService;
import net.samsolutions.wicket.MyAjaxButton;
import net.samsolutions.wicket.MySession;
import net.samsolutions.wicket.pages.auth.BasePageAuth;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.EmailAddressValidator;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class Share extends BasePageAuth {

	@SpringBean
	private AlbumService albumService;
	@SpringBean
	private ShareInformationService shareInformationService;
	@SpringBean
	private UserService userService;
	private Album album;
	private PageParameters parameters;
	private static final int ITEMS_PER_PAGE = 20;

	public Share(final PageParameters parameters) {
		super(parameters);
		this.parameters = parameters;
		if (parameters.getNamedKeys().contains("album")) {
			String name = parameters.get("album").toString();
			add(new Label("album", name));
			this.album = albumService.getAlbum(name, ((MySession) Session.get()).getuId());
		}

		add(createShareForm());
		DataView<ShareInformation> dataView = createShareDataView();
		add(dataView);
		add(new PagingNavigator("navigator", dataView));
	}

	private DataView<ShareInformation> createShareDataView() {
		final List<ShareInformation> list = new ArrayList<ShareInformation>(
				shareInformationService.getAlbumShares(this.album.getId()));
		DataView<ShareInformation> dataView = new DataView<ShareInformation>("pageable",
				new ListDataProvider<ShareInformation>(list)) {

			public void populateItem(final Item<ShareInformation> item) {
				final ShareInformation shareInformation = item.getModelObject();
				item.add(new Label("email", shareInformation.getUser().getEmail()));
				item.add(new Link<Void>("delete") {
					public void onClick() {
						shareInformationService.delete(shareInformation);
						info(new StringResourceModel("share.deleted", this, null).getString());
						setResponsePage(new Share(parameters));
					}
				});
			}
		};
		dataView.setItemsPerPage(ITEMS_PER_PAGE);
		return dataView;
	}

	private Form<User> createShareForm() {
		Form<User> form = new Form<User>("form") {
			@Override
			protected void onSubmit() {
				User user = getModelObject();
				User existedUser = userService.getUser(user);
				if (existedUser == null) {
					error(new StringResourceModel("share.noUser", this, null).getString());
				} else if (existedUser.getEmail().equals(
						userService.getById(((MySession) Session.get()).getuId()).getEmail())) {
					error(new StringResourceModel("share.yourself", this, null).getString());
				} else {
					ShareInformation shareInformation = new ShareInformation(null, album, existedUser);
					if (shareInformationService.getShare(album.getName(), existedUser.getId(),
							(userService.getById(((MySession) Session.get()).getuId()).getEmail())) == null) {
						shareInformationService.create(shareInformation);
						info(new StringResourceModel("share.shareSuccess", this, null).getString());
						setResponsePage(new Share(getPage().getPageParameters()));
					} else {
						error(new StringResourceModel("share.alreadyExist", this, null).getString());
					}
				}
			}
		};
		User user = new User();
		form.setDefaultModel(new Model<User>(user));
		RequiredTextField<String> shareEmail = new RequiredTextField<String>("shareEmail", new PropertyModel<String>(
				user, "email"));
		shareEmail.setLabel(new StringResourceModel("share.emailField", this, null));
		shareEmail.add(EmailAddressValidator.getInstance());
		form.add(shareEmail);
		FeedbackPanel feedback = new FeedbackPanel("feedback");
		feedback.setOutputMarkupId(true);
		form.add(feedback);
		form.add(new MyAjaxButton("ajax-button", form, feedback));
		return form;
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		response.renderCSSReference("css/Share.css");
	}
}
