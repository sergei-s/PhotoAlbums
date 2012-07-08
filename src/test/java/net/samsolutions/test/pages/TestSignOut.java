package net.samsolutions.test.pages;

import static net.samsolutions.test.pages.ConstantsForTests.USER_EMAIL_EXIST;
import static net.samsolutions.test.pages.ConstantsForTests.USER_PASS_YES;

import net.samsolutions.hibernate.User;
import net.samsolutions.spring.UserService;
import net.samsolutions.wicket.MySession;
import net.samsolutions.wicket.WicketApp;
import net.samsolutions.wicket.pages.auth.SignOut;

import org.apache.wicket.Session;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.spring.test.ApplicationContextMock;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestSignOut {
	private WicketApp wicketApp;
	private WicketTester tester;

	{
		this.wicketApp = new WicketApp() {
			@Override
			protected void init() {
				ApplicationContextMock context = new ApplicationContextMock();
				UserService mock = new UserService() {
					public void create(User user) {	}
					public void delete(User user) { }
					public void update(User user) { }
					public User getUser(String email, String password) { return null; }
					public User getUser(User userEmail) { return null; }
					public User getById(Integer id) {
						return new User(1, USER_EMAIL_EXIST, USER_PASS_YES);
					}
				};
				context.putBean("userBean", mock);
				getComponentInstantiationListeners().add(new SpringComponentInjector(this, context));
			}
		};
	}

	@Before
	public void setUp() {
		this.tester = new WicketTester(this.wicketApp);
		((MySession) Session.get()).setuId(1);
	}

	@Test
	public void testInvalidate() {
		Assert.assertFalse(Session.get().isSessionInvalidated());
		this.tester.startPage(SignOut.class);
		tester.assertRenderedPage(SignOut.class);
		Assert.assertTrue(Session.get().isSessionInvalidated());
	}
}
