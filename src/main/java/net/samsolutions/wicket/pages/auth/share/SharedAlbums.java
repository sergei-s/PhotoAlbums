package net.samsolutions.wicket.pages.auth.share;

import net.samsolutions.hibernate.ShareInformation;
import net.samsolutions.hibernate.User;
import net.samsolutions.spring.ShareInformationService;
import net.samsolutions.spring.UserService;
import net.samsolutions.wicket.AjaxDataView;
import net.samsolutions.wicket.MySession;
import net.samsolutions.wicket.pages.auth.BasePageAuth;
import net.samsolutions.wicket.pages.auth.ErrorPage404;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

@SuppressWarnings("serial")
public class SharedAlbums extends BasePageAuth {

	@SpringBean
	private UserService userService;
	@SpringBean
	private ShareInformationService shareInformationService;
	private static final int ITEMS_PER_PAGE = 10;
	private User user;

	public SharedAlbums(final PageParameters parameters) {
		super(parameters);
		if (parameters.getNamedKeys().contains("user")) {
			final String email = parameters.get("user").toString();
			User user = new User();
			user.setEmail(email);
			if (userService.getUser(user) == null) {
				throw new RestartResponseException(ErrorPage404.class);
			}
			this.user = user;
			add(new AjaxDataView("dataContainer", "navigator", createShareDataView()));
		} else {
			throw new RestartResponseException(ErrorPage404.class);
		}
	}

	private DataView<ShareInformation> createShareDataView() {
		List<ShareInformation> list = shareInformationService.getShares(userService.getUser(user),
				userService.getById(((MySession) Session.get()).getuId()));
		if (list.isEmpty()) {
			throw new RestartResponseException(ErrorPage404.class);
		}
		DataView<ShareInformation> dataView = new DataView<ShareInformation>("pageable",
				new ListDataProvider<ShareInformation>(list)) {
			public void populateItem(final Item<ShareInformation> item) {
				final ShareInformation shareInformation = item.getModelObject();
				PageParameters pars = new PageParameters();
				pars.add("user", user.getEmail());
				pars.add("album", shareInformation.getAlbum().getName());
				BookmarkablePageLink<Void> bp = new BookmarkablePageLink<Void>("link", SharedFiles.class, pars);
				bp.add(new Label("name", shareInformation.getAlbum().getName()));
				item.add(bp);
			}
		};
		dataView.setItemsPerPage(ITEMS_PER_PAGE);
		return dataView;
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		response.renderCSSReference("css/SharedAlbums.css");
	}
}
