package net.samsolutions.wicket;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

/**
 * Ajax button, that adds feedback on submit and on error
 */
@SuppressWarnings("serial")
public class MyAjaxButton extends AjaxButton {

	private FeedbackPanel feedback;

	/**
	 * @param id
	 * @param form
	 * @param feedback feedback to add
	 */
	public MyAjaxButton(String id, Form<?> form, FeedbackPanel feedback) {
		super(id, form);
		this.feedback = feedback;
	}

	@Override
	protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
		target.add(feedback);
	}

	@Override
	protected void onError(AjaxRequestTarget target, Form<?> form) {
		target.add(feedback);
	}
}
