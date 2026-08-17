package in.tech_camp.furima_a.form;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class ProductFormTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private ProductForm createValidProductForm() {
        ProductForm form = new ProductForm();
        form.setName("テスト商品");
        form.setDescription("テスト用の説明文です。");
        form.setCategory(1);
        form.setCondition(1);
        form.setDeliveryFee(1);
        form.setPrefecture(1);
        form.setUntilDelivery(1);
        form.setPrice(1000L);
        form.setImg(new MockMultipartFile("img", "test.jpg", "image/jpeg", "test image".getBytes()));
        return form;
    }

    @Nested
    @DisplayName("商品出品（正常系）")
    class SuccessTests {

        @Test
        @DisplayName("必要な情報が全て適切に入力されていればバリデーションを通過すること")
        void validProductForm() {
            ProductForm form = createValidProductForm();
            Set<ConstraintViolation<ProductForm>> violations = validator.validate(form);
            assertThat(violations).isEmpty();
        }
    }

    @Nested
    @DisplayName("商品出品（異常系）")
    class FailureTests {

        @Test
        @DisplayName("商品名が空の場合はエラー")
        void nameIsBlank() {
            ProductForm form = createValidProductForm();
            form.setName("");
            Set<ConstraintViolation<ProductForm>> violations = validator.validate(form);
            assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("name"));
        }

        @Test
        @DisplayName("商品名が40文字を超える場合はエラー")
        void nameIsTooLong() {
            ProductForm form = createValidProductForm();
            form.setName("あ".repeat(41));
            Set<ConstraintViolation<ProductForm>> violations = validator.validate(form);
            assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("name"));
        }

        @Test
        @DisplayName("商品の説明が空の場合はエラー")
        void descriptionIsBlank() {
            ProductForm form = createValidProductForm();
            form.setDescription("");
            Set<ConstraintViolation<ProductForm>> violations = validator.validate(form);
            assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("description"));
        }

        @Test
        @DisplayName("カテゴリーが未選択（0）の場合はエラー")
        void categoryIsInvalid() {
            ProductForm form = createValidProductForm();
            form.setCategory(0);
            Set<ConstraintViolation<ProductForm>> violations = validator.validate(form);
            assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("category"));
        }

        @Test
        @DisplayName("商品の状態が未選択（0）の場合はエラー")
        void conditionIsInvalid() {
            ProductForm form = createValidProductForm();
            form.setCondition(0);
            Set<ConstraintViolation<ProductForm>> violations = validator.validate(form);
            assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("condition"));
        }

        @Test
        @DisplayName("配送料の負担が未選択（0）の場合はエラー")
        void deliveryFeeIsInvalid() {
            ProductForm form = createValidProductForm();
            form.setDeliveryFee(0);
            Set<ConstraintViolation<ProductForm>> violations = validator.validate(form);
            assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("deliveryFee"));
        }

        @Test
        @DisplayName("発送元の地域が未選択（0）の場合はエラー")
        void prefectureIsInvalid() {
            ProductForm form = createValidProductForm();
            form.setPrefecture(0);
            Set<ConstraintViolation<ProductForm>> violations = validator.validate(form);
            assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("prefecture"));
        }

        @Test
        @DisplayName("発送までの日数が未選択（0）の場合はエラー")
        void untilDeliveryIsInvalid() {
            ProductForm form = createValidProductForm();
            form.setUntilDelivery(0);
            Set<ConstraintViolation<ProductForm>> violations = validator.validate(form);
            assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("untilDelivery"));
        }

        @Test
        @DisplayName("価格が300円未満の場合はエラー")
        void priceIsLessThan300() {
            ProductForm form = createValidProductForm();
            form.setPrice(299L);
            Set<ConstraintViolation<ProductForm>> violations = validator.validate(form);
            assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("price"));
        }

        @Test
        @DisplayName("価格が9,999,999円を超える場合はエラー")
        void priceIsGreaterThanMax() {
            ProductForm form = createValidProductForm();
            form.setPrice(10_000_000L);
            Set<ConstraintViolation<ProductForm>> violations = validator.validate(form);
            assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("price"));
        }
    }
}