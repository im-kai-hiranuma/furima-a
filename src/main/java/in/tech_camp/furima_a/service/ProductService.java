package in.tech_camp.furima_a.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.tech_camp.furima_a.dto.ProductDetailDto;
import in.tech_camp.furima_a.dto.ProductListDto;
import in.tech_camp.furima_a.entity.ProductEntity;
import in.tech_camp.furima_a.enums.DeliveryFeeType;
import in.tech_camp.furima_a.form.ProductForm;
import in.tech_camp.furima_a.dto.repository.ProductDetailQueryResult;
import in.tech_camp.furima_a.enums.UntilDelivery;
import in.tech_camp.furima_a.enums.PrefectureType;
import in.tech_camp.furima_a.enums.Category;
import in.tech_camp.furima_a.enums.Condition;
import in.tech_camp.furima_a.repository.ProductRepository;

@Service
public class ProductService {

  private final ProductRepository productRepository;
  private final StorageService storageService;

  public ProductService(ProductRepository productRepository, StorageService storageService) {
    this.productRepository = productRepository;
    this.storageService = storageService;
  }

  // 商品一覧表示機能
  public List<ProductListDto> allProduct() {
    return productRepository.findAll().stream().map(product -> {
      ProductListDto dto = new ProductListDto();
      dto.setId(product.getId());
      dto.setImg(product.getImg());
      dto.setName(product.getName());
      dto.setPrice(product.getPrice());
      dto.setSoldout(product.getProductId() != null);
      dto.setDeliveryFee(DeliveryFeeType.fromCode(product.getDeliveryFee()).getLabel());
      return dto;
    }).collect(Collectors.toList());
  }

@Transactional
    public ProductEntity createProduct(ProductForm form, Long userId) throws IOException {
        // 1. 画像の保存
        String savedFileName = storageService.storeFile(form.getImg());

        // 2. エンティティの作成（Form から Entity へのデータ移送）
        ProductEntity product = new ProductEntity();
        product.setUserId(userId);
        product.setName(form.getName());
        product.setDescription(form.getDescription());
        product.setCategory(form.getCategory());
        product.setCondition(form.getCondition());
        product.setDeliveryFee(form.getDeliveryFee());
        product.setPrefecture(form.getPrefecture());
        product.setUntilDelivery(form.getUntilDelivery());
        product.setPrice(form.getPrice());
        product.setImg(savedFileName);

        // 3. DB保存
        productRepository.insert(product);

        return product;
    }

  // 商品詳細表示
  public ProductDetailDto selectByProductId(Long id) {

    ProductDetailQueryResult result = productRepository.selectByProductId(id);
    if (result == null) {
      return null;
    }

    ProductDetailDto dto = new ProductDetailDto();
    dto.setId(result.getId());
    dto.setImg(result.getImg());
    dto.setName(result.getName());
    dto.setDescription(result.getDescription());
    dto.setNickname(result.getNickname());
    dto.setPrice(result.getPrice());
    dto.setSoldout(result.isSoldout());
    dto.setDeliveryFee(DeliveryFeeType.fromCode(result.getDeliveryFee()).getLabel());
    dto.setCategory(Category.fromCode(result.getCategory()).getDisplayName());
    dto.setCondition(Condition.fromCode(result.getCondition()).getDisplayName());
    dto.setPrefecture(PrefectureType.fromCode(result.getPrefecture()).getLabel());
    dto.setUntilDelivery(UntilDelivery.fromCode(result.getUntilDelivery()).getDisplayName());
    dto.setUserId(result.getUserId());

    return dto;
  }

  // 商品削除機能
  public void deleteProduct(Long id, Long currentUserId) {
    ProductDetailQueryResult product = productRepository.selectByProductId(id);
    
    if (product == null) {
      return;
    }

    if (!product.getUserId().equals(currentUserId) || product.isSoldout()) {
      return;
    }

    productRepository.deleteById(id);
  }

  //商品編集
  @Transactional
  public ProductEntity updateProduct(Long id, ProductForm form, Long currentUserId) throws IOException {
    ProductDetailQueryResult product = productRepository.selectByProductId(id);

    if (product == null) {
      return null;
    }

    if (!product.getUserId().equals(currentUserId) || product.isSoldout()) {
      return null;
    }

    // 画像の保存
    String savedFileName = storageService.storeFile(form.getImg());

    // エンティティの作成（Form から Entity へのデータ移送）
    ProductEntity updatedProduct = new ProductEntity();
    updatedProduct.setId(id);
    updatedProduct.setUserId(currentUserId);
    updatedProduct.setName(form.getName());
    updatedProduct.setDescription(form.getDescription());
    updatedProduct.setCategory(form.getCategory());
    updatedProduct.setCondition(form.getCondition());
    updatedProduct.setDeliveryFee(form.getDeliveryFee());
    updatedProduct.setPrefecture(form.getPrefecture());
    updatedProduct.setUntilDelivery(form.getUntilDelivery());
    updatedProduct.setPrice(form.getPrice());
    updatedProduct.setImg(savedFileName);

    // DB更新
    productRepository.update(updatedProduct);
    return updatedProduct;
  }

}