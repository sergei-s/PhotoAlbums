package net.samsolutions.wicket.pages.auth;

import net.samsolutions.hibernate.User;
import net.samsolutions.spring.UserService;
import net.samsolutions.wicket.MySession;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

@SuppressWarnings("serial")
public class ModalDelete extends WebPage {
	private String pass;
	@SpringBean
	private UserService userService;
	private ModalWindow window;
	private FeedbackPanel feedback;

	public ModalDelete(final ModalWindow window) {
		this.window = window;
		final Form<User> form = new Form<User>("form", new CompoundPropertyModel<User>(new User()));
		FeedbackPanel feedback = new FeedbackPanel("feedback");
		feedback.setOutputMarkupId(true);
		form.add(feedback);
		this.feedback = feedback;
		form.add(createButtonOk());
		form.add(createButtonCancel());
		PasswordTextField password = new PasswordTextField("password", new PropertyModel<String>(this, "pass"));
		password.setLabel(new StringResourceModel("modalDelete.pass", this, null));
		form.add(password);
		add(form);
	}

	private AjaxButton createButtonOk() {
        return new AjaxButton("buttonOk") {
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				User user = new User(null, userService.getById(((MySession) Session.get()).getuId()).getEmail(), pass);
				User userDB = userService.getUser(user.getEmail(), user.getPassword());
				if (!(userDB == null)) {
					window.close(target);
					userService.delete(userService.getById(((MySession) Session.get()).getuId()));
					getSession().invalidate();
				} else {
					error(new StringResourceModel("modalDelete.wrongPass", this, null).getString());
					target.add(feedback);
				}
			}

			public void onError(AjaxRequestTarget target, Form<?> form) {
				target.add(feedback);
			}
		};
	}

	private AjaxButton createButtonCancel() {
        return new AjaxButton("buttonCancel") {
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				window.close(target);
			}
			public void onError(AjaxRequestTarget target, Form<?> form) {
				window.close(target);
			}
		};
	}
}
