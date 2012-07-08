package net.samsolutions.wicket.pages.auth;

import net.samsolutions.wicket.pages.auth.share.SharedUsers;
import net.samsolutions.wicket.pages.nonAuth.BasePage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;

@SuppressWarnings ("serial")
public class BasePageAuth extends BasePage {

	public BasePageAuth(final PageParameters parameters) {
		super(parameters);
		add(new BookmarkablePageLink<Void>("albums", Albums.class));
		add(new BookmarkablePageLink<Void>("sharedUsers", SharedUsers.class));
		add(new BookmarkablePageLink<Void>("profile", Profile.class));
	}
}
