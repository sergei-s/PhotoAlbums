package net.samsolutions.wicket.pages.auth;

import net.samsolutions.hibernate.Album;
import net.samsolutions.spring.AlbumService;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

@SuppressWarnings("serial")
public class ModalRename extends WebPage {

	private String result;
	@SpringBean
	private AlbumService albumService;
	private ModalWindow window;
	private FeedbackPanel feedback;
	private Album album;

	@SuppressWarnings("unused")
	private String getResult() {
		return this.result;
	}

	@SuppressWarnings("unused")
	private void setResult(String result) {
		this.result = result;
	}

	public ModalRename(final Album album, final ModalWindow window) {
		this.result = album.getName();
		this.window = window;
		this.album = album;
		Form<Void> form = new Form<Void>("form");
		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		feedback.setOutputMarkupId(true);
		form.add(feedback);
		this.feedback = feedback;
		RequiredTextField<String> name = new RequiredTextField<String>("name",
				new PropertyModel<String>(this, "result"));
		name.setLabel(new StringResourceModel("modalRename.albumName", this, null));
		form.add(name);
		form.add(createButtonOk());
		form.add(createButtonCancel());
		add(form);
	}

	private AjaxButton createButtonOk() {
        return new AjaxButton("buttonOk") {
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				albumService.rename(album, result);
				window.close(target);
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
