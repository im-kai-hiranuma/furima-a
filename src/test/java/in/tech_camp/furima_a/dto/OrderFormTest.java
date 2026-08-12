package in.tech_camp.furima_a.dto;

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

@DisplayName("商品購入モデルの単体テスト")
class OrderFormTest {

    private Validator validator;
    private OrderForm orderForm;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        // テスト前に正常なデータをセットアップ
        orderForm = new OrderForm();
        orderForm.setToken("tok_abcdefghijk00000000000000000");
        orderForm.setPostNumber("123-4567");
        orderForm.setPrefecture(1);
        orderForm.setCity("東京都千代田区");
        orderForm.setBlock("千代田1-1");
        orderForm.setBuilding("ビルディング101");
        orderForm.setPhone("09012345678");
    }

    @Nested
    @DisplayName("正常系")
    class SuccessTests {

        @Test
        @DisplayName("すべての入力値が正常であればバリデーションを通過する")
        void success_allValid() {
            Set<ConstraintViolation<OrderForm>> violations = validator.validate(orderForm);
            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("建物名が空でもバリデーションを通過する")
        void success_buildingEmpty() {
            orderForm.setBuilding(""); // 任意項目
            Set<ConstraintViolation<OrderForm>> violations = validator.validate(orderForm);
            assertThat(violations).isEmpty();
        }
    }

    @Nested
    @DisplayName("異常系")
    class FailureTests {

        @Test
        @DisplayName("tokenが空ではバリデーションエラーになる")
        void fail_tokenEmpty() {
            orderForm.setToken("");
            Set<ConstraintViolation<OrderForm>> violations = validator.validate(orderForm);
            assertThat(violations).isNotEmpty();
        }

        @Test
        @DisplayName("郵便番号が空ではバリデーションエラーになる")
        void fail_postNumberEmpty() {
            orderForm.setPostNumber("");
            Set<ConstraintViolation<OrderForm>> violations = validator.validate(orderForm);
            assertThat(violations).isNotEmpty();
        }

        @Test
        @DisplayName("郵便番号にハイフンがないとバリデーションエラーになる")
        void fail_postNumberNoHyphen() {
            orderForm.setPostNumber("1234567");
            Set<ConstraintViolation<OrderForm>> violations = validator.validate(orderForm);
            assertThat(violations).isNotEmpty();
        }

        @Test
        @DisplayName("都道府県が「---」(0)ではバリデーションエラーになる")
        void fail_prefectureZero() {
            orderForm.setPrefecture(0);
            Set<ConstraintViolation<OrderForm>> violations = validator.validate(orderForm);
            assertThat(violations).isNotEmpty();
        }

        @Test
        @DisplayName("市区町村が空ではバリデーションエラーになる")
        void fail_cityEmpty() {
            orderForm.setCity("");
            Set<ConstraintViolation<OrderForm>> violations = validator.validate(orderForm);
            assertThat(violations).isNotEmpty();
        }

        @Test
        @DisplayName("番地が空ではバリデーションエラーになる")
        void fail_blockEmpty() {
            orderForm.setBlock("");
            Set<ConstraintViolation<OrderForm>> violations = validator.validate(orderForm);
            assertThat(violations).isNotEmpty();
        }

        @Test
        @DisplayName("電話番号が空ではバリデーションエラーになる")
        void fail_phoneEmpty() {
            orderForm.setPhone("");
            Set<ConstraintViolation<OrderForm>> violations = validator.validate(orderForm);
            assertThat(violations).isNotEmpty();
        }

        @Test
        @DisplayName("電話番号が9桁以下ではバリデーションエラーになる")
        void fail_phoneTooShort() {
            orderForm.setPhone("090123456");
            Set<ConstraintViolation<OrderForm>> violations = validator.validate(orderForm);
            assertThat(violations).isNotEmpty();
        }

        @Test
        @DisplayName("電話番号が12桁以上ではバリデーションエラーになる")
        void fail_phoneTooLong() {
            orderForm.setPhone("090123456789");
            Set<ConstraintViolation<OrderForm>> violations = validator.validate(orderForm);
            assertThat(violations).isNotEmpty();
        }

        @Test
        @DisplayName("電話番号にハイフンが含まれているとバリデーションエラーになる")
        void fail_phoneWithHyphen() {
            orderForm.setPhone("090-1234-5678");
            Set<ConstraintViolation<OrderForm>> violations = validator.validate(orderForm);
            assertThat(violations).isNotEmpty();
        }
    }
}