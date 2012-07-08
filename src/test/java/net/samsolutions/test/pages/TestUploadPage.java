package net.samsolutions.test.pages;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import net.samsolutions.hibernate.Album;
import net.samsolutions.hibernate.File;
import net.samsolutions.hibernate.User;
import net.samsolutions.spring.AlbumService;
import net.samsolutions.spring.FileService;
import net.samsolutions.spring.UserService;
import net.samsolutions.wicket.MySession;
import net.samsolutions.wicket.WicketApp;
import net.samsolutions.wicket.pages.auth.Upload;

import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.spring.test.ApplicationContextMock;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import static net.samsolutions.test.pages.ConstantsForTests.*;

public class TestUploadPage {

	private WicketApp wicketApp;
	private WicketTester tester;
	private Set<File> set = new HashSet<File>();
	{
		this.wicketApp = new WicketApp() {
			@Override
			protected void init() {
				ApplicationContextMock context = new ApplicationContextMock();
				AlbumService mockAlbum = new AlbumService() {
					public void rename(Album album, String newName) { }
					public Album getAlbum(String name, int userId) {
						User user = new User(1, USER_EMAIL_EXIST, USER_PASS_YES);
						Album album = new Album(1, ALBUM_NAME_EXIST, user, null, null);
						set.add(new File(1, "1", new byte[1], new byte[1], album));
						album.setFiles(set);
						user.getAlbums().add(album);
						return album;
					}
					public void delete(Album album) { }
					public void create(Album album) { }
					public Album getById(Integer id) { return null; }
					public ArrayList<Album> getAlbums(Integer id) { return null; }
				};
				FileService mockFile = new FileService() {
					public void create(File file) {	}
					public void delete(File file) {	}
					public File getFileOwn(int id, String name, int userId) { return null; }
					public File getFileShared(int id, String name, int userId) { return null; }
					public void changeAlbum(File file, Album album) {	}
					public File getById(Integer id) { return null; }
					public ArrayList<File> getAlbumFiles(int albumId) { return new ArrayList<File>(); }
					public ArrayList<File> getAlbumFilesPaging(int albumId, int first, int count) { return null; }
					public Long getCountAlbumFiles(int albumId) {
						return (long) 0;
					}
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
		Page page = new Upload(pars);
		this.tester.startPage(page);
		tester.assertVisible("signout");
		this.tester.getSession().setLocale(new Locale("ru", "RU"));
	}

	@Test
	public void testRendered() {
		tester.assertRenderedPage(Upload.class);
		tester.assertComponent("upload:fileInput", FileUploadField.class);
	}
}
