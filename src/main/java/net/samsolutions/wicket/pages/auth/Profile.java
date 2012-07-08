package net.samsolutions.wicket.pages.auth;

import net.samsolutions.hibernate.User;
import net.samsolutions.spring.UserService;
import net.samsolutions.utils.MD5;
import net.samsolutions.wicket.MyAjaxButton;
import net.samsolutions.wicket.MySession;
import net.samsolutions.wicket.PasswordPolicyValidator;
import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

@SuppressWarnings("serial")
public class Profile extends BasePageAuth {
	@SpringBean
	private UserService userService;
	private String newPassword;
	private static final int PASSWORD_MIN_LENGTH = 8;
	private static final int WINDOW_WIDTH = 500;
	private static final int WINDOW_HEIGHT = 200;

	public String getNewPassword() {
		return this.newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public Profile(PageParameters parameters) {
		super(parameters);
		add(new ChangePassForm<Void>("form", new CompoundPropertyModel<User>(new User())));
		add(new DeleteForm<Void>("deleteForm", new CompoundPropertyModel<User>(new User())));
	}

	private class ChangePassForm<T> extends Form<User> {
		public ChangePassForm(String id, CompoundPropertyModel<User> cpm) {
			super(id, cpm);
			PasswordTextField password = new PasswordTextField("password");
			password.setLabel(new StringResourceModel("profile.passwordField", this, null));
			PasswordTextField newPassword = new PasswordTextField("newPassword", new PropertyModel<String>(
					Profile.this, "newPassword"));
			newPassword.add(StringValidator.minimumLength(PASSWORD_MIN_LENGTH));
			newPassword.add(new PasswordPolicyValidator());
			newPassword.setLabel(new StringResourceModel("profile.newPasswordField", this, null));
			add(password);
			add(newPassword);
			FeedbackPanel feedback = new FeedbackPanel("feedback");
			feedback.setOutputMarkupId(true);
			add(feedback);
			add(new MyAjaxButton("ajax-button", this, feedback));
		}

		@Override
		protected void onSubmit() {
			User user = getModelObject();
			user.setEmail(userService.getById(((MySession) Session.get()).getuId()).getEmail());
			User userDB = userService.getUser(user.getEmail(), user.getPassword());
			if (!(userDB == null)) {
				userDB.setPassword(MD5.getHash(newPassword));
				userService.update(userDB);
				info(new StringResourceModel("profile.passwordChanged", this, null).getString());
			} else {
				error(new StringResourceModel("profile.wrongPassword", this, null).getString());
			}
		}
	}

	private class DeleteForm<T> extends Form<User> {
		public DeleteForm(String id, CompoundPropertyModel<User> cpm) {
			super(id, cpm);
			final ModalWindow modal = new ModalWindow("modal");
	        modal.setPageCreator(new ModalWindow.PageCreator() {
	            public Page createPage() {
	                return new ModalDelete(modal);
	            }
	        });
	        modal.setResizable(false);
	        modal.setInitialWidth(WINDOW_WIDTH);
	        modal.setInitialHeight(WINDOW_HEIGHT);
	        modal.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
	            public void onClose(AjaxRequestTarget target) {
	            	setResponsePage(Profile.class);
	            }
	        });
	        add(modal);
			AjaxButton delete = new AjaxButton("delete") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
			        modal.show(target);
				}
				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
				}
			};
			add(delete);
		}
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		response.renderCSSReference("css/Profile.css");
	}
}
