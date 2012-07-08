package net.samsolutions.test.pages;

import static net.samsolutions.test.pages.ConstantsForTests.ALBUM_NAME_EXIST;
import static net.samsolutions.test.pages.ConstantsForTests.USER_EMAIL_EXIST;
import static net.samsolutions.test.pages.ConstantsForTests.USER_PASS_YES;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import net.samsolutions.hibernate.Album;
import net.samsolutions.hibernate.File;
import net.samsolutions.hibernate.User;
import net.samsolutions.spring.FileService;
import net.samsolutions.spring.UserService;
import net.samsolutions.wicket.MySession;
import net.samsolutions.wicket.WicketApp;
import net.samsolutions.wicket.pages.auth.share.SharedBig;

import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.spring.test.ApplicationContextMock;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

public class TestSharedBigPage {
	private WicketApp wicketApp;
	private WicketTester tester;
	private Album album;
	private Set<File> set = new HashSet<File>();

	{
		this.wicketApp = new WicketApp() {
			@Override
			protected void init() {
				ApplicationContextMock context = new ApplicationContextMock();

				FileService mockFile = new FileService() {
					public void create(File file) {	}
					public void delete(File file) {	}
					public File getFileOwn(int id, String name, int userId) { return null; }
					public File getFileShared(int id, String name, int userId) {
						User user = new User(1, USER_EMAIL_EXIST, USER_PASS_YES);
						album = new Album(1, ALBUM_NAME_EXIST, user, null, null);
						File file = new File(1, "1", new byte[1], new byte[1], album);
						set.add(file);
						album.setFiles(set);
						user.getAlbums().add(album);
						return file;
					}
					public void changeAlbum(File file, Album album) {	}
					public File getById(Integer id) { return null; }
					public ArrayList<File> getAlbumFiles(int albumId) {
						ArrayList<File> list = new ArrayList<File>();
						User user = new User(1, USER_EMAIL_EXIST, USER_PASS_YES);
						album = new Album(1, ALBUM_NAME_EXIST, user, null, null);
						File file = new File(1, "1", new byte[1], new byte[1], album);
						set.add(file);
						album.setFiles(set);
						list.add(file);
						return list;
					}
					public ArrayList<File> getAlbumFilesPaging(int albumId, int first, int count) { return null; }
					public Long getCountAlbumFiles(int albumId) { return null; }
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
				context.putBean("fileBean", mockFile);
				getComponentInstantiationListeners().add(new SpringComponentInjector(this, context));
			}
		};
	}

	@Before
	public void setUp() {
		this.tester = new WicketTester(this.wicketApp);
		((MySession) Session.get()).setuId(1);
		PageParameters pars = new PageParameters();
		pars.add("album", ALBUM_NAME_EXIST);
		pars.add("fid", 1);
		Page page = new SharedBig(pars);
		this.tester.startPage(page);
		tester.assertVisible("signout");
		this.tester.getSession().setLocale(new Locale("ru", "RU"));
	}

	@Test
	public void testRendered() {
		tester.assertRenderedPage(SharedBig.class);
	}
}
