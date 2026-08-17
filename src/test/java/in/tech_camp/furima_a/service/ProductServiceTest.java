package in.tech_camp.furima_a.service;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import in.tech_camp.furima_a.entity.ProductEntity;
import in.tech_camp.furima_a.form.ProductForm;
import in.tech_camp.furima_a.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private StorageService storageService;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("商品情報と画像が正常に渡された際、画像保存後にDB登録処理が呼ばれること")
    void createProduct_success() throws IOException {
        // Given
        Long userId = 1L;
        ProductForm form = new ProductForm();
        form.setName("テスト商品");
        form.setDescription("商品説明");
        form.setCategory(1);
        form.setCondition(1);
        form.setDeliveryFee(1);
        form.setPrefecture(1);
        form.setUntilDelivery(1);
        form.setPrice(1000L);
        MockMultipartFile mockFile = new MockMultipartFile("img", "test.png", "image/png", "test".getBytes());
        form.setImg(mockFile);

        when(storageService.storeFile(mockFile)).thenReturn("saved-image-uuid.png");

        // When
        ProductEntity result = productService.createProduct(form, userId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("テスト商品");
        assertThat(result.getImg()).isEqualTo("saved-image-uuid.png");
        assertThat(result.getUserId()).isEqualTo(userId);

        verify(storageService).storeFile(mockFile);
        verify(productRepository).insert(any(ProductEntity.class));
    }
}