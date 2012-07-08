package net.samsolutions.test.pages;

import static net.samsolutions.test.pages.ConstantsForTests.ALBUM_NAME_EXIST;
import static net.samsolutions.test.pages.ConstantsForTests.USER_EMAIL_EXIST;
import static net.samsolutions.test.pages.ConstantsForTests.USER_PASS_YES;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.spring.test.ApplicationContextMock;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import net.samsolutions.hibernate.Album;
import net.samsolutions.hibernate.File;
import net.samsolutions.hibernate.ShareInformation;
import net.samsolutions.hibernate.User;
import net.samsolutions.spring.FileService;
import net.samsolutions.spring.ShareInformationService;
import net.samsolutions.spring.UserService;
import net.samsolutions.wicket.MySession;
import net.samsolutions.wicket.WicketApp;
import net.samsolutions.wicket.pages.auth.share.SharedFiles;

public class TestSharedFilesPage {

	private WicketApp wicketApp;
	private WicketTester tester;

	{
		this.wicketApp = new WicketApp() {
			@Override
			protected void init() {
				ApplicationContextMock context = new ApplicationContextMock();
				ShareInformationService mockShare = new ShareInformationService() {
					public void create(ShareInformation shareInformation) { }
					public void delete(ShareInformation shareInformation) {	}
					public List<ShareInformation> getShares(User userShared, User userSharedTo) { return null; }
					public ShareInformation getShare(String albumName, int userSharedToId, String userSharedEmail) {
						return new ShareInformation(1, new Album(1, null, null, null, null), null); 
					}
					public ArrayList<ShareInformation> getAlbumShares(
							int albumId) { return null; }
					public ArrayList<ShareInformation> getUserShares(int userId) { return null; }
				};
				UserService mockUser = new UserService() {
					public void create(User user) {	}
					public void delete(User user) { }
					public void update(User user) { }
					public User getUser(String email, String password) { return null; }
					public User getUser(User userEmail) { return null; }
					public User getById(Integer id) {
						return new User(1, USER_EMAIL_EXIST, USER_PASS_YES);
					}
				};
				FileService mockFile = new FileService() {
					public File getFileShared(int id, String name, int userId) { return null; }
					public File getFileOwn(int id, String name, int userId) { return null; }
					public File getById(Integer id) { return null; }
					public ArrayList<File> getAlbumFiles(int albumId) { return new ArrayList<File>(); }
					public void delete(File file) { }
					public void create(File file) { }
					public void changeAlbum(File file, Album album) { }
					public ArrayList<File> getAlbumFilesPaging(int albumId, int first, int count) { return null; }
					public Long getCountAlbumFiles(int albumId) { return null; }
				};
				context.putBean("userBean", mockUser);
				context.putBean("shareBean", mockShare);
				context.putBean("filBean", mockFile);
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
		pars.add("user", USER_EMAIL_EXIST);
		Page page = new SharedFiles(pars);
		this.tester.startPage(page);
		tester.assertVisible("signout");
		this.tester.getSession().setLocale(new Locale("ru", "RU"));
	}

	@Test
	public void testRendered() {
		tester.assertRenderedPage(SharedFiles.class);
	}
}
