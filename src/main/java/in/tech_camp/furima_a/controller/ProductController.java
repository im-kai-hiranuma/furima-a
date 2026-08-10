package in.tech_camp.furima_a.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import in.tech_camp.furima_a.dto.ProductDetailDto;
import in.tech_camp.furima_a.service.ProductService;

@Controller
public class ProductController {

  private final ProductService productService;

  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  // 商品一覧表示
  @GetMapping({ "/", "", "items" })
  public String showAllProduct(Model model) {
    model.addAttribute("items", productService.allProduct());
    return "items/index";
  }

  // 商品詳細表示
  @GetMapping("/items/{id}")
  public String showProductDetail(@PathVariable Long id, Model model) {

    ProductDetailDto dto = productService.selectByProductId(id);

    model.addAttribute("item", dto);

    return "items/show";
  }

}