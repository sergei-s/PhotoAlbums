package net.samsolutions.test.pages;

import java.util.Locale;

import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.spring.test.ApplicationContextMock;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataIntegrityViolationException;

import net.samsolutions.hibernate.User;
import net.samsolutions.spring.UserService;
import net.samsolutions.wicket.WicketApp;
import net.samsolutions.wicket.pages.nonAuth.Register;
import net.samsolutions.wicket.pages.nonAuth.RegistryCompleted;

import static net.samsolutions.test.pages.ConstantsForTests.*;

public class TestRegisterPage {

	private WicketApp wicketApp;
	private WicketTester tester;

	{
		this.wicketApp = new WicketApp() {
			@Override
			protected void init() {
				ApplicationContextMock context = new ApplicationContextMock();
				UserService mock = new UserService() {
					public void create(User user) {
						if (user.getEmail().equals(USER_EMAIL_EXIST)) {
							throw new DataIntegrityViolationException("");
						}
					}
					public void delete(User user) { }
					public void update(User user) { }
					public User getUser(String email, String password) { return null; }
					public User getUser(User userEmail) { return null; }
					public User getById(Integer id) { return null; }
				};
				context.putBean("userBean", mock);
				getComponentInstantiationListeners().add(new SpringComponentInjector(this, context));
			}
		};
	}

	@Before
	public void setUp() {
		this.tester = new WicketTester(this.wicketApp);
		this.tester.startPage(Register.class);
		this.tester.assertInvisible("signout");
		this.tester.getSession().setLocale(new Locale("ru", "RU"));
	}

	@Test
	public void testNoInput() {
		tester.assertInvisible("signout");
		FormTester formTester = tester.newFormTester("form");
		formTester.submit();
		tester.assertErrorMessages("Поле 'Почтовый ящик' обязательно для ввода.",
                "Поле 'Пароль' обязательно для ввода.",
                "Поле 'Подтверждения пароля' обязательно для ввода.");
	}

	@Test
	public void testNotEmailNoPass() {
		FormTester formTester = this.tester.newFormTester("form");
		formTester.setValue("email", USER_EMAIL_NOT);
		formTester.setValue("password", "");
		formTester.submit();
		this.tester.assertErrorMessages("'123' не является правильным адресом e-mail.",
                "Поле 'Пароль' обязательно для ввода.",
                "Поле 'Подтверждения пароля' обязательно для ввода.");
	}

	@Test
	public void testNotEmailPassNoPassA() {
		FormTester formTester = this.tester.newFormTester("form");
		formTester.setValue("email", USER_EMAIL_NOT);
		formTester.setValue("password", USER_PASS_LENGTH);
		formTester.submit();
		this.tester.assertErrorMessages("'123' не является правильным адресом e-mail.",
                "Пароль должен состоять как минимум из 8 символов",
                "Поле 'Подтверждения пароля' обязательно для ввода.");
	}

	@Test
	public void testPassLength() {
		FormTester formTester = this.tester.newFormTester("form");
		formTester.setValue("email", USER_EMAIL_EXIST);
		formTester.setValue("password", USER_PASS_LENGTH);
		formTester.setValue("passwordAgain", USER_PASS_LENGTH);
		formTester.submit();
		this.tester.assertErrorMessages("Пароль должен состоять как минимум из 8 символов");
	}

	@Test
	public void testNoLetters() {
		FormTester formTester = this.tester.newFormTester("form");
		formTester.setValue("email", USER_EMAIL_EXIST);
		formTester.setValue("password", USER_PASS_NO_LETTERS);
		formTester.setValue("passwordAgain", USER_PASS_NO_LETTERS);
		formTester.submit();
		this.tester.assertErrorMessages("Пароль должен содержать по крайней мере одну букву в нижнем регистре",
                "Пароль должен содержать по крайней мере одну букву в верхнем регистре");
	}

	@Test
	public void testAlreadyExist() {
		FormTester formTester = this.tester.newFormTester("form");
		formTester.setValue("email", USER_EMAIL_EXIST);
		formTester.setValue("password", USER_PASS_YES);
		formTester.setValue("passwordAgain", USER_PASS_YES);
		formTester.submit();
		this.tester.assertErrorMessages("Имэйл уже зарегестрирован");
	}

	@Test
	public void testNewUser() {
		FormTester formTester = this.tester.newFormTester("form");
		formTester.setValue("email", USER_EMAIL_NOT_EXIST);
		formTester.setValue("password", USER_PASS_YES);
		formTester.setValue("passwordAgain", USER_PASS_YES);
		formTester.submit();
		tester.assertRenderedPage(RegistryCompleted.class);
		tester.assertInvisible("signout");
	}
}
