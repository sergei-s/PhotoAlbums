package net.samsolutions.wicket;

import net.samsolutions.hibernate.User;
import net.samsolutions.spring.UserService;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.cookies.CookieUtils;

/**
 * Session that holds current user
 */
@SuppressWarnings("serial")
public class MySession extends WebSession {

	@SpringBean
	private UserService userService;
	private Integer uId;

	public Integer getuId() {
		return uId;
	}

	public void setuId(Integer uId) {
		this.uId = uId;
	}

	public MySession(Request request) {
		super(request);
		Injector.get().inject(this);
	}

	/**
	 * Checks if user is Authenticated
	 * @return boolean
	 */
	public boolean isAuthenticated() {
		return (this.uId != null);
	}

	public boolean isAuthenticatedWithCookies() {
		CookieUtils cu = new CookieUtils();
		String email = cu.load("email");
		String password = cu.load("password");
		if ((email != null) && (password != null)) {
			User user = new User(null, email, password);
			System.out.println(email + "              " + password);
			System.out.println(user.getEmail() + user.getPassword());
			User userDB = userService.getUser(user);
			if (userDB == null) {
				return false;
			}
			if (userDB.getPassword().equals(password)) {
				this.uId = userDB.getId();
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}
