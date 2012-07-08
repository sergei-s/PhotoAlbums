package net.samsolutions.test.pages;

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
import net.samsolutions.wicket.pages.auth.share.SharedUsers;

import org.apache.wicket.Session;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.spring.test.ApplicationContextMock;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

public class TestSharedUsersPage {
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
				ShareInformationService mockShare = new ShareInformationService() {
					public void create(ShareInformation shareInformation) { }
					public void delete(ShareInformation shareInformation) {	}
					public List<ShareInformation> getShares(User userShared, User userSharedTo) { return null; }
					public ShareInformation getShare(String albumName, int userSharedToId, String userSharedEmail) {
						return new ShareInformation(1, new Album(1, null, null, null, null), null); 
					}
					public ArrayList<ShareInformation> getAlbumShares(
							int albumId) { return null; }
					public ArrayList<ShareInformation> getUserShares(int userId) {
						return new ArrayList<ShareInformation>();
					}
				};
				context.putBean("userBean", mock);
				context.putBean("shareBean", mockShare);
				getComponentInstantiationListeners().add(new SpringComponentInjector(this, context));
			}
		};
	}

	@Before
	public void setUp() {
		this.tester = new WicketTester(this.wicketApp);
		((MySession) Session.get()).setuId(1);
		this.tester.startPage(SharedUsers.class);
		tester.assertVisible("signout");
		this.tester.getSession().setLocale(new Locale("ru", "RU"));
	}

	@Test
	public void testRendered() {
		tester.assertRenderedPage(SharedUsers.class);
	}
}
