package in.tech_camp.furima_a.form;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@DisplayName("ユーザー新規登録フォームの単体テスト")
class UserFormTest {

    private Validator validator;
    private UserForm userForm;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        // 毎回テスト前に「完璧な正解データ」を準備しておきます
        userForm = new UserForm();
        userForm.setNickname("furima太郎");
        userForm.setEmail("test@example.com");
        userForm.setPassword("password123");
        userForm.setPasswordConfirmation("password123");
        userForm.setLastName("山田");
        userForm.setFirstName("陸太郎");
        userForm.setLastNameKana("ヤマダ");
        userForm.setFirstNameKana("リクタロウ");
        userForm.setBirthYear(1990);
        userForm.setBirthMonth(1);
        userForm.setBirthDay(1);
    }

    @Nested
    @DisplayName("ユーザー新規登録ができる場合")
    class SuccessTests {

        @Test
        @DisplayName("すべての入力項目が正しく設定されていれば登録できる")
        void success_allValid() {
            Set<ConstraintViolation<UserForm>> violations = validator.validate(userForm);
            // エラー（violation）が0個であることを確認
            assertThat(violations).isEmpty();
        }
    }

    @Nested
    @DisplayName("ユーザー新規登録ができない場合")
    class FailureTests {

        @Test
        @DisplayName("ニックネームが空だと登録できない")
        void fail_nicknameEmpty() {
            userForm.setNickname("");
            Set<ConstraintViolation<UserForm>> violations = validator.validate(userForm);
            assertThat(violations).isNotEmpty();
        }

        @Test
        @DisplayName("メールアドレスが空だと登録できない")
        void fail_emailEmpty() {
            userForm.setEmail("");
            Set<ConstraintViolation<UserForm>> violations = validator.validate(userForm);
            assertThat(violations).isNotEmpty();
        }

        @Test
        @DisplayName("パスワードが空だと登録できない")
        void fail_passwordEmpty() {
            userForm.setPassword("");
            Set<ConstraintViolation<UserForm>> violations = validator.validate(userForm);
            assertThat(violations).isNotEmpty();
        }
    }
}