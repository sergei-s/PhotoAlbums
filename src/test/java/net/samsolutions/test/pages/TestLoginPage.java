package net.samsolutions.test.pages;

import java.util.ArrayList;
import java.util.Locale;

import net.samsolutions.hibernate.Album;
import net.samsolutions.hibernate.User;
import net.samsolutions.spring.AlbumService;
import net.samsolutions.spring.UserService;
import net.samsolutions.wicket.WicketApp;
import net.samsolutions.wicket.pages.auth.Albums;
import net.samsolutions.wicket.pages.nonAuth.Login;

import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.spring.test.ApplicationContextMock;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import static net.samsolutions.test.pages.ConstantsForTests.*;

public class TestLoginPage {

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
							return new User(1, email, password);
						} else {
							return null;
						}
					}
					public User getUser(User userEmail) { return null; }
					public User getById(Integer id) {
						return new User(1, USER_EMAIL_EXIST, USER_PASS_YES);
					}
				};
				AlbumService mockAlbum = new AlbumService() {
					public void rename(Album album, String newName) { }
					public Album getAlbum(String name, int userId) { return null; }
					public void delete(Album album) { }
					public void create(Album album) { }
					public Album getById(Integer id) { return null; }
					public ArrayList<Album> getAlbums(Integer id) {
						return new ArrayList<Album>();
					}
				};
				context.putBean("userBean", mock);
				context.putBean("albumBean", mockAlbum);
				getComponentInstantiationListeners().add(
						new SpringComponentInjector(this, context));
			}
		};
	}

	@Before
	public void setUp() {
		this.tester = new WicketTester(this.wicketApp);
		this.tester.startPage(Login.class);
		this.tester.getSession().setLocale(new Locale("ru", "RU"));
	}

	@Test
	public void testNoInputAndLocale() {
		tester.assertInvisible("signout");
		FormTester formTester = this.tester.newFormTester("form");
		formTester.setValue("email", "");
		formTester.setValue("password", "");
		formTester.submit();
		this.tester.assertErrorMessages("Поле 'Почтовый ящик' обязательно для ввода.",
                "Поле 'Пароль' обязательно для ввода.");

		this.tester.getSession().setLocale(Locale.ENGLISH);
		formTester = this.tester.newFormTester("form");
		formTester.setValue("email", "");
		formTester.setValue("password", "");
		formTester.submit();
		this.tester.assertErrorMessages("Field 'Email' is required.",
                "Field 'Password' is required.");
	}

	@Test
	public void testNotEmailNoPass() {
		FormTester formTester = this.tester.newFormTester("form");
		formTester.setValue("email", USER_EMAIL_NOT);
		formTester.setValue("password", "");
		formTester.submit();
		this.tester.assertErrorMessages("'123' не является правильным адресом e-mail.",
                "Поле 'Пароль' обязательно для ввода.");
	}

	@Test
	public void testNotEmailPass() {
		FormTester formTester = this.tester.newFormTester("form");
		formTester.setValue("email", USER_EMAIL_NOT);
		formTester.setValue("password", USER_PASS_NO);
		formTester.submit();
		this.tester.assertErrorMessages("'123' не является правильным адресом e-mail.");
	}

	@Test
	public void testNoEmailPass() {
		FormTester formTester = this.tester.newFormTester("form");
		formTester.setValue("email", "");
		formTester.setValue("password", USER_PASS_NO);
		formTester.submit();
		this.tester.assertErrorMessages("Поле 'Почтовый ящик' обязательно для ввода.");
	}

	@Test
	public void testNoUser() {
		FormTester formTester = this.tester.newFormTester("form");
		formTester.setValue("email", USER_EMAIL_EXIST);
		formTester.setValue("password", USER_PASS_NO);
		formTester.submit();
		this.tester.assertErrorMessages("Пользователя с таким именем/паролем не существует");
	}

	@Test
	public void testUser() {
		FormTester formTester = this.tester.newFormTester("form");
		formTester.setValue("email", USER_EMAIL_EXIST);
		formTester.setValue("password", USER_PASS_YES);
		formTester.submit();
		tester.assertRenderedPage(Albums.class);
		tester.assertVisible("signout");
	}

	@Test
	public void testHeader() {
		tester.assertLabel("header", "Photo Albums");
	}
}
