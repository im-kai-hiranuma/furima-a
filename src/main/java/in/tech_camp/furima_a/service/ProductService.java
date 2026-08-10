package in.tech_camp.furima_a.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import in.tech_camp.furima_a.dto.ProductListDto;
import in.tech_camp.furima_a.enums.DeliveryFeeType;
import in.tech_camp.furima_a.repository.ProductRepository;

@Service
public class ProductService {

  private final ProductRepository productRepository;

  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
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
}