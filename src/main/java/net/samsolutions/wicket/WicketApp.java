package net.samsolutions.wicket;

import net.samsolutions.wicket.pages.auth.*;
import net.samsolutions.wicket.pages.auth.share.*;
import net.samsolutions.wicket.pages.nonAuth.ForgotPassword;
import net.samsolutions.wicket.pages.nonAuth.Login;
import net.samsolutions.wicket.pages.nonAuth.Register;
import net.samsolutions.wicket.pages.nonAuth.RegistryCompleted;
import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.application.IComponentInstantiationListener;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;


public class WicketApp extends WebApplication {

	public WicketApp() {
	}

	public Class<? extends Page> getHomePage() {
		return Login.class;
	}

	@Override
	protected void init() {
		super.init();
		getComponentInstantiationListeners().add(new SpringComponentInjector(this));
		getSecuritySettings().setAuthorizationStrategy(
				new AuthorizationStrategy());
		mountPage("albums", Albums.class);
		mountPage("error404", ErrorPage404.class);
		mountPage("bigPic", Image.class);
		mountPage("register", Register.class);
		mountPage("registerCompleted", RegistryCompleted.class);
		mountPage("share", Share.class);
		mountPage("sharedAlbums", SharedAlbums.class);
		mountPage("sharedBig", SharedBig.class);
		mountPage("sharedFiles", SharedFiles.class);
		mountPage("sharedUsers", SharedUsers.class);
		mountPage("pics", Upload.class);
		mountPage("profile", Profile.class);
		mountPage("forgotPassword", ForgotPassword.class);
		mountPage("signOut", SignOut.class);
	}

	@Override
	public Session newSession(Request request, Response response) {
		return new MySession(request);
	}
}
