package net.samsolutions.wicket;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

import java.util.regex.Pattern;

@SuppressWarnings("serial")
public class PasswordPolicyValidator implements IValidator<String> {

	private static final Pattern UPPER = Pattern.compile("[A-Z]");
	private static final Pattern LOWER = Pattern.compile("[a-z]");
	private static final Pattern NUMBER = Pattern.compile("[0-9]");

	public void validate(IValidatable<String> validatable) {
		final String password = validatable.getValue();
		if (!NUMBER.matcher(password).find()) {
			error(validatable, "no-digit");
		}
		if (!LOWER.matcher(password).find()) {
			error(validatable, "no-lower");
		}
		if (!UPPER.matcher(password).find()) {
			error(validatable, "no-upper");
		}
	}

	private void error(IValidatable<String> validatable, String errorKey) {
		ValidationError error = new ValidationError();
		error.addMessageKey(getClass().getSimpleName() + "." + errorKey);
		validatable.error(error);
	}
}
