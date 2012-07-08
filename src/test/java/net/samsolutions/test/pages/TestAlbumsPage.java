package net.samsolutions.test.pages;

import java.util.ArrayList;
import java.util.Locale;

import net.samsolutions.hibernate.Album;
import net.samsolutions.hibernate.User;
import net.samsolutions.spring.AlbumService;
import net.samsolutions.spring.UserService;
import net.samsolutions.wicket.MySession;
import net.samsolutions.wicket.WicketApp;
import net.samsolutions.wicket.pages.auth.Albums;

import org.apache.wicket.Session;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.spring.test.ApplicationContextMock;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataIntegrityViolationException;

import static net.samsolutions.test.pages.ConstantsForTests.*;

public class TestAlbumsPage {

	private WicketApp wicketApp;
	private WicketTester tester;

	{
		this.wicketApp = new WicketApp() {
			@Override
			protected void init() {
				ApplicationContextMock context = new ApplicationContextMock();
				AlbumService mockAlbum = new AlbumService() {
					public void rename(Album album, String newName) { }
					public Album getAlbum(String name, int userId) { return null; }
					public void delete(Album album) {
						album.getUser().getAlbums().remove(album);
					}
					public void create(Album album) {
						if (album.getName().equals(ALBUM_NAME_EXIST)) {
							throw new DataIntegrityViolationException("");
						}
						album.getUser().getAlbums().add(album);
					}
					public Album getById(Integer id) { return null; }
					public ArrayList<Album> getAlbums(Integer id) { return new ArrayList<Album>(); }
				};
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
				context.putBean("albumBean", mockAlbum);
				getComponentInstantiationListeners().add(new SpringComponentInjector(this, context));
			}
		};
	}

	@Before
	public void setUp() {
		this.tester = new WicketTester(this.wicketApp);
		((MySession) Session.get()).setuId(1);
		this.tester.startPage(Albums.class);
		this.tester.getSession().setLocale(new Locale("ru", "RU"));
	}

	@Test
	public void testRendered() {
		tester.assertRenderedPage(Albums.class);
		tester.assertVisible("signout");
	}

	@Test
	public void testCreateNull() {
		FormTester formTester = this.tester.newFormTester("form");
		formTester.setValue("AlbumName", "");
		formTester.submit();
		this.tester.assertErrorMessages("Поле 'Название альбома' обязательно для ввода.");
	}

	@Test
	public void testCreateAlbumExist() {
		FormTester formTester = this.tester.newFormTester("form");
		formTester.setValue("AlbumName", ALBUM_NAME_EXIST);
		formTester.submit();
		this.tester.assertErrorMessages("Альбом с таким названием уже существует");
	}

	@Test
	public void testCreateAlbum() {
		FormTester formTester = this.tester.newFormTester("form");
		formTester.setValue("AlbumName", ALBUM_NAME_NOT_EXIST);
		formTester.submit();
		this.tester.assertNoErrorMessage();
		tester.assertRenderedPage(Albums.class);
	}
}
