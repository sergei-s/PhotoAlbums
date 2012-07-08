package net.samsolutions.test.pages;

import static net.samsolutions.test.pages.ConstantsForTests.ALBUM_NAME_EXIST;
import static net.samsolutions.test.pages.ConstantsForTests.USER_EMAIL_EXIST;
import static net.samsolutions.test.pages.ConstantsForTests.USER_EMAIL_NOT;
import static net.samsolutions.test.pages.ConstantsForTests.USER_PASS_YES;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import junit.framework.Assert;

import net.samsolutions.hibernate.Album;
import net.samsolutions.hibernate.File;
import net.samsolutions.hibernate.ShareInformation;
import net.samsolutions.hibernate.User;
import net.samsolutions.spring.AlbumService;
import net.samsolutions.spring.FileService;
import net.samsolutions.spring.ShareInformationService;
import net.samsolutions.spring.UserService;
import net.samsolutions.wicket.MySession;
import net.samsolutions.wicket.WicketApp;
import net.samsolutions.wicket.pages.auth.share.Share;

import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.spring.test.ApplicationContextMock;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import static net.samsolutions.test.pages.ConstantsForTests.*;

public class TestSharePage {

	private WicketApp wicketApp;
	private WicketTester tester;
	private Set<File> files = new HashSet<File>();
	private Set<ShareInformation> shares = new HashSet<ShareInformation>();
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
						files.add(new File(1, "1", new byte[1], new byte[1], album));
						album.setFiles(files);
						album.setShareInformation(shares);
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
					public ArrayList<File> getAlbumFiles(int albumId) { return null; }
					public ArrayList<File> getAlbumFilesPaging(int albumId, int first, int count) { return null; }
					public Long getCountAlbumFiles(int albumId) { return null; }
				};
				ShareInformationService mockShare = new ShareInformationService() {
					public void create(ShareInformation shareInformation) {
						shareInformation.getUser().getShareInformation().add(shareInformation);
						shareInformation.getAlbum().getShareInformation().add(shareInformation);
					}
					public void delete(ShareInformation shareInformation) {	}
					public List<ShareInformation> getShares(User userShared, User userSharedTo) { return null; }
					public ShareInformation getShare(String albumName, int userSharedToId, String userSharedEmail) {
						return null;
					}
					public ArrayList<ShareInformation> getAlbumShares(
							int albumId) { return new ArrayList<ShareInformation>(); }
					public ArrayList<ShareInformation> getUserShares(int userId) { return null; }
				};
				UserService mockUser = new UserService() {
					public void create(User user) { }
					public void delete(User user) {	}
					public void update(User user) {	}
					public User getUser(String email, String password) { return null; }
					public User getUser(User userEmail) {
						if (userEmail.getEmail().equals(USER_EMAIL_EXIST)) {
							return userEmail;
						} else {
							return null;
						}
					}
					public User getById(Integer id) {
						return new User(1, USER_EMAIL_EXIST, USER_PASS_YES);
					}
				};
				context.putBean("albumBean", mockAlbum);
				context.putBean("fileBean", mockFile);
				context.putBean("shareBean", mockShare);
				context.putBean("userBean", mockUser);
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
		Page page = new Share(pars);
		this.tester.startPage(page);
		tester.assertVisible("signout");
		this.tester.getSession().setLocale(new Locale("ru", "RU"));
	}

	@Test
	public void testRendered() {
		tester.assertRenderedPage(Share.class);
	}

	@Test
	public void testEmailNotExist() {
		FormTester formTester = this.tester.newFormTester("form");
		formTester.setValue("shareEmail", USER_EMAIL_NOT_EXIST);
		formTester.submit();
		this.tester.assertErrorMessages("Нет такого пользователя");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testAddShare() {
		DataView<File> dataView = (DataView<File>) tester.getComponentFromLastRenderedPage("pageable");
		Assert.assertEquals("Number of files", shares.size(), dataView.getItemCount());
		FormTester formTester = this.tester.newFormTester("form");
		formTester.setValue("shareEmail", USER_EMAIL_EXIST);
		formTester.submit();
		dataView = (DataView<File>) tester.getComponentFromLastRenderedPage("pageable");
		Assert.assertEquals("Number of files", shares.size(), dataView.getItemCount());
	}

	@Test
	public void testNotEmail() {
		FormTester formTester = this.tester.newFormTester("form");
		formTester.setValue("shareEmail", USER_EMAIL_NOT);
		formTester.submit();
		this.tester.assertErrorMessages("'123' не является правильным адресом e-mail.");
	}
}
