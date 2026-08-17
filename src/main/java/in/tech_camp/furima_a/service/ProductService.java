package in.tech_camp.furima_a.service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import in.tech_camp.furima_a.dto.ProductDetailDto;
import in.tech_camp.furima_a.dto.ProductListDto;
import in.tech_camp.furima_a.entity.ProductEntity;
import in.tech_camp.furima_a.enums.DeliveryFeeType;
import in.tech_camp.furima_a.form.ProductForm;
import in.tech_camp.furima_a.dto.repository.ProductDetailQueryResult;
import in.tech_camp.furima_a.dto.repository.ProductEditForm;
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

  // 出品処理
  @Transactional
  public ProductEntity createProduct(ProductForm form, Long userId) throws IOException {
    // 1. 画像の保存
    String savedFileName = storageService.storeFile(form.getImg());

    // 2. エンティティの作成
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
    
    // 【修正】result.getCategory() だったものを getDeliveryFee() に修正
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

    if (!Objects.equals(product.getUserId(), currentUserId) || product.isSoldout()) {
      return;
    }

    productRepository.deleteById(id);
  }

  // 商品更新ページ
  @Transactional(readOnly = true)
  public ProductForm showEditProduct(ProductDetailDto dto, Long userId) {

    if (!productRepository.existsByIdANDUserId(dto.getId(), userId)) {
      throw new RuntimeException("所有者ではありませんので編集できません");
    }

    ProductForm form = new ProductForm();
    form.setName(dto.getName());
    form.setDescription(dto.getDescription());
    form.setCategory(Category.fromDisplayName(dto.getCategory()).getCode());
    form.setCondition(Condition.fromDisplayName(dto.getCondition()).getCode());
    form.setDeliveryFee(DeliveryFeeType.fromDisplayName(dto.getDeliveryFee()).getCode());
    form.setPrefecture(PrefectureType.fromDisplayName(dto.getPrefecture()).getCode());
    form.setUntilDelivery(UntilDelivery.fromDisplayName(dto.getUntilDelivery()).getCode());
    form.setPrice(dto.getPrice());

    return form;
  }

// 商品更新
  @Transactional
  public int updateByProductId(Long id, ProductForm productForm, Long userId, String image) throws IOException {

    if (!productRepository.existsByIdANDUserId(id, userId)) {
      throw new RuntimeException("所有者ではありませんので編集できません");
    }

    MultipartFile imgFile = productForm.getImg();
    String imageName = (imgFile != null && !imgFile.isEmpty())
        ? storageService.storeFile(imgFile)
        : image;

    ProductEditForm product = new ProductEditForm();
    product.setId(id);
    product.setImg(imageName);
    product.setName(productForm.getName());
    product.setDescription(productForm.getDescription());
    product.setCategory(productForm.getCategory());
    product.setCondition(productForm.getCondition());
    product.setDeliveryFee(productForm.getDeliveryFee());
    product.setPrefecture(productForm.getPrefecture());
    product.setUntilDelivery(productForm.getUntilDelivery());
    product.setPrice(productForm.getPrice());

    int result = productRepository.updateByProductId(product);

    if (result <= 0) {
      throw new RuntimeException("編集できませんでした");
    } else if (result >= 2) {
      throw new RuntimeException("予想された挙動とは異なったため編集できませんでした");
    }

    return result;
  }
}