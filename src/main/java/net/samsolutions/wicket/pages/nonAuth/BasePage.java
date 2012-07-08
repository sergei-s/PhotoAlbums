package net.samsolutions.wicket.pages.nonAuth;

import net.samsolutions.spring.UserService;
import net.samsolutions.wicket.MySession;
import net.samsolutions.wicket.pages.auth.SignOut;
import org.apache.wicket.Session;
import org.apache.wicket.devutils.debugbar.DebugBar;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Locale;

@SuppressWarnings("serial")
public class BasePage extends WebPage {
	@SpringBean
	private UserService userService;
	
	public BasePage(final PageParameters parameters) {
		super(parameters);
		add(new DebugBar("debug"));
		add(new Label("header", "Photo Albums"));
		add(new Link<Void>("goEnglish") {
			public void onClick() {
				getSession().setLocale(Locale.US);
			}
		});
		add(new Link<Void>("goRussian") {
			public void onClick() {
				getSession().setLocale(new Locale("ru", "RU"));
			}
		});
		add(new BookmarkablePageLink<Void>("signout", SignOut.class, null) {
			@Override
			public boolean isVisible() {
				return ((MySession) Session.get()).isAuthenticated();
			}
		});
		if (((MySession) Session.get()).isAuthenticated()) {
			add(new Label("fullname", userService.getById(((MySession) Session.get()).getuId()).getEmail()));
		} else {
			add(new Label("fullname"));
		}
	}
}
