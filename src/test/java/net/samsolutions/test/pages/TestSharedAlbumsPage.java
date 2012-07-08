package net.samsolutions.test.pages;

import static net.samsolutions.test.pages.ConstantsForTests.ALBUM_NAME_EXIST;
import static net.samsolutions.test.pages.ConstantsForTests.USER_EMAIL_EXIST;
import static net.samsolutions.test.pages.ConstantsForTests.USER_PASS_YES;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.samsolutions.hibernate.Album;
import net.samsolutions.hibernate.ShareInformation;
import net.samsolutions.hibernate.User;
import net.samsolutions.spring.ShareInformationService;
import net.samsolutions.spring.UserService;
import net.samsolutions.wicket.MySession;
import net.samsolutions.wicket.WicketApp;
import net.samsolutions.wicket.pages.auth.share.SharedAlbums;

import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.spring.test.ApplicationContextMock;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

public class TestSharedAlbumsPage {
	private WicketApp wicketApp;
	private WicketTester tester;
	{
		this.wicketApp = new WicketApp() {
			@Override
			protected void init() {
				ApplicationContextMock context = new ApplicationContextMock();
				ShareInformationService mockShare = new ShareInformationService() {
					public void create(ShareInformation shareInformation) {
						shareInformation.getUser().getShareInformation().add(shareInformation);
						shareInformation.getAlbum().getShareInformation().add(shareInformation);
					}
					public void delete(ShareInformation shareInformation) {	}
					public List<ShareInformation> getShares(User userShared, User userSharedTo) {
						ArrayList<ShareInformation> list = new ArrayList<ShareInformation>();
						list.add(new ShareInformation(1, new Album(1, ALBUM_NAME_EXIST, null, null, null), new User()));
						return list;
					}
					public ShareInformation getShare(String albumName, int userSharedToId, String userSharedEmail) {
						return null;
					}
					public ArrayList<ShareInformation> getAlbumShares(int albumId) { return null; }
					public ArrayList<ShareInformation> getUserShares(int userId) { return null; }
				};
				UserService mockUser = new UserService() {
					public void create(User user) { }
					public void delete(User user) {	}
					public void update(User user) {	}
					public User getUser(String email, String password) { return new User(1, email, password); }
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
		pars.add("user", USER_EMAIL_EXIST);
		Page page = new SharedAlbums(pars);
		this.tester.startPage(page);
		tester.assertVisible("signout");
		this.tester.getSession().setLocale(new Locale("ru", "RU"));
	}

	@Test
	public void testRendered() {
		tester.assertRenderedPage(SharedAlbums.class);
	}
}
