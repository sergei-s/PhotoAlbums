package net.samsolutions.wicket.pages.auth;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.cookies.CookieUtils;

import static net.samsolutions.wicket.CookiesConstants.COOKIE_EMAIL;
import static net.samsolutions.wicket.CookiesConstants.COOKIE_PASSWORD;

@SuppressWarnings("serial")
public class SignOut extends WebPage {

	public SignOut(final PageParameters parameters) {
		super(parameters);
		CookieUtils cu = new CookieUtils();
		cu.getSettings().setMaxAge(0);
		cu.save(COOKIE_EMAIL, "");
		cu.save(COOKIE_PASSWORD, "");
		getSession().invalidate();
	}
}
