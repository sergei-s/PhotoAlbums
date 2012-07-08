package net.samsolutions.test.pages;

import java.util.Locale;

import net.samsolutions.hibernate.User;
import net.samsolutions.spring.UserService;
import net.samsolutions.wicket.WicketApp;
import net.samsolutions.wicket.pages.nonAuth.RegistryCompleted;

import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.spring.test.ApplicationContextMock;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

public class TestRegistryCompletedPage {
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
					public User getById(Integer id) { return null; }
				};
				context.putBean("userBean", mock);
				getComponentInstantiationListeners().add(new SpringComponentInjector(this, context));
			}
		};
	}

	@Before
	public void setUp() {
		this.tester = new WicketTester(this.wicketApp);
		this.tester.getSession().setLocale(new Locale("ru", "RU"));
	}

	@Test
	public void testRendered() {
		this.tester.startPage(RegistryCompleted.class);
		tester.assertRenderedPage(RegistryCompleted.class);
		tester.assertContains("Спасибо за регистрацию.");
		tester.assertContains("Теперь вы можете войти на сайт:");
		tester.assertContains("Пойти войти");
	}
}
