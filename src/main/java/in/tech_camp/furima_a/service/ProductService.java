package in.tech_camp.furima_a.service;

import java.util.List;

import org.springframework.stereotype.Service;

import in.tech_camp.furima_a.dto.ProductListDto;
import in.tech_camp.furima_a.repository.ProductRepository;

@Service
public class ProductService {

  private final ProductRepository productRepository;

  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

}