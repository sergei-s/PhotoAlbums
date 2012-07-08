package net.samsolutions.test.pages;

import java.util.Locale;

import net.samsolutions.hibernate.User;
import net.samsolutions.spring.UserService;
import net.samsolutions.wicket.MySession;
import net.samsolutions.wicket.WicketApp;
import net.samsolutions.wicket.pages.auth.Profile;

import org.apache.wicket.Session;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.spring.test.ApplicationContextMock;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import static net.samsolutions.test.pages.ConstantsForTests.*;

public class TestProfilePage {

	private WicketApp wicketApp;
	private WicketTester tester;

	{
		this.wicketApp = new WicketApp() {
			@Override
			protected void init() {
				ApplicationContextMock context = new ApplicationContextMock();
				UserService mock = new UserService() {
					public void create(User user) { }
					public void delete(User user) { }
					public void update(User user) { }
					public User getUser(String email, String password) {
						if ((email.equals(USER_EMAIL_EXIST)) && (password.equals(USER_PASS_YES))) {
							return new User(null, email, password);
						} else {
							return null;
						}
					}
					public User getUser(User userEmail) { return null; }
					public User getById(Integer id) {
						return new User(1, USER_EMAIL_EXIST, USER_PASS_YES);
					}
				};
				context.putBean("albumBean", mock);
				getComponentInstantiationListeners().add(new SpringComponentInjector(this, context));
			}
		};
	}

	@Before
	public void setUp() {
		this.tester = new WicketTester(this.wicketApp);
		((MySession) Session.get()).setuId(1);
		this.tester.startPage(Profile.class);
		this.tester.getSession().setLocale(new Locale("ru", "RU"));
		tester.assertVisible("signout");
	}

	@Test
	public void testRendered() {
		tester.assertRenderedPage(Profile.class);
	}

	@Test
	public void testNoinput() {
		FormTester formTester = this.tester.newFormTester("form");
		formTester.setValue("password", "");
		formTester.setValue("newPassword", "");
		formTester.submit();
		this.tester.assertErrorMessages("Поле 'Старый пароль' обязательно для ввода.",
                "Поле 'Новый пароль' обязательно для ввода.");
	}

	@Test
	public void testNoNew() {
		FormTester formTester = this.tester.newFormTester("form");
		formTester.setValue("password", USER_PASS_YES);
		formTester.setValue("newPassword", "");
		formTester.submit();
		this.tester.assertErrorMessages("Поле 'Новый пароль' обязательно для ввода.");
	}

	@Test
	public void testLength() {
		FormTester formTester = this.tester.newFormTester("form");
		formTester.setValue("password", USER_PASS_YES);
		formTester.setValue("newPassword", USER_PASS_LENGTH);
		formTester.submit();
		this.tester.assertErrorMessages("Пароль должен состоять как минимум из 8 символов");
	}

	@Test
	public void testNoLetters() {
		FormTester formTester = this.tester.newFormTester("form");
		formTester.setValue("password", USER_PASS_YES);
		formTester.setValue("newPassword", USER_PASS_NO_LETTERS);
		formTester.submit();
		this.tester.assertErrorMessages("Пароль должен содержать по крайней мере одну букву в нижнем регистре",
                "Пароль должен содержать по крайней мере одну букву в верхнем регистре");
	}

	@Test
	public void testWrongOld() {
		FormTester formTester = this.tester.newFormTester("form");
		formTester.setValue("password", USER_PASS_NO);
		formTester.setValue("newPassword", USER_PASS_NO);
		formTester.submit();
		this.tester.assertErrorMessages("Неправильный старый пароль");
	}

	@Test
	public void testEditPass() {
		FormTester formTester = this.tester.newFormTester("form");
		formTester.setValue("password", USER_PASS_YES);
		formTester.setValue("newPassword", USER_PASS_NO);
		formTester.submit();
	}
}
