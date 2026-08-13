package in.tech_camp.furima_a.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import in.tech_camp.furima_a.dto.ProductDetailDto;
import in.tech_camp.furima_a.entity.ProductEntity;
import in.tech_camp.furima_a.enums.Category;
import in.tech_camp.furima_a.enums.Condition;
import in.tech_camp.furima_a.enums.DeliveryFeeType;
import in.tech_camp.furima_a.enums.PrefectureType;
import in.tech_camp.furima_a.enums.UntilDelivery;
import in.tech_camp.furima_a.form.ProductForm;
import in.tech_camp.furima_a.security.CustomUserDetails; 
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

    // 出品画面の表示（GET）
    @GetMapping("/items/new")
    public String showProductForm(@AuthenticationPrincipal CustomUserDetails currentUser, 
    Model model) {

    if (currentUser == null) {
        return "redirect:/users/sign_in";
    }

        model.addAttribute("productForm", new ProductForm());
        return "items/new";
    }

    // 出品処理（POST）
    @PostMapping("/post") 
    public String createProduct(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @ModelAttribute("productForm") @Validated ProductForm productForm,
            BindingResult bindingResult,
            Model model)throws IOException {

        if (currentUser == null) {
            return "redirect:/users/sign_in";
        }

        // 1. バリデーションエラーがある場合は出品画面に戻す
        if (bindingResult.hasErrors()) {
            return "items/new";
        }
        // 2. 登録処理
        try {
            productService.createProduct(productForm, currentUser.getUser().getId());
            return "redirect:/";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", "画像の保存に失敗しました。");
            return "items/new";
        }
    }

    // --- プルダウン用 Enum 共通データ設定 ---
    @ModelAttribute
    public void addEnumAttributes(Model model) {
        model.addAttribute("categories", Category.values());
        model.addAttribute("conditions", Condition.values());
        model.addAttribute("deliveryFees", DeliveryFeeType.values());
        model.addAttribute("prefectures", PrefectureType.values());
        model.addAttribute("untilDeliveries", UntilDelivery.values());
    }

    // 商品詳細表示
    @GetMapping("/items/{id}")
    public String showProductDetail(@PathVariable Long id, Model model) {
        ProductDetailDto dto = productService.selectByProductId(id);

        if (dto == null) {
        return "redirect:/";
    }

        model.addAttribute("item", dto);
        return "items/show";
    }

  // 商品削除機能
  @PostMapping("/items/{id}/delete")
  public String deleteProduct(
      @PathVariable Long id, 
      @AuthenticationPrincipal CustomUserDetails currentUser
  ) {
    if (currentUser != null) {
      productService.deleteProduct(id, currentUser.getUser().getId());
    }

    return "redirect:/";
  }

  //商品編集
    @GetMapping("/items/{id}/edit")
    public String showEditForm(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails currentUser, Model model) {
    ProductDetailDto dto = productService.selectByProductId(id);

    if (currentUser == null) {
        return "redirect:/users/sign_in";
    }

    if (dto == null || !dto.getUserId().equals(currentUser.getUser().getId())) {
        return "redirect:/";
    }

    ProductForm form = new ProductForm();
    form.setName(dto.getName());
    form.setDescription(dto.getDescription());

    model.addAttribute("productForm", form);
    model.addAttribute("item", dto);
    return "items/edit";
    }
    
    @PostMapping("/items/{id}/update")
    public String updateProduct(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @ModelAttribute("productForm") @Validated ProductForm productForm,
            BindingResult bindingResult,
            Model model) {

        if (currentUser == null) {
            return "redirect:/users/sign_in";
        }

        if (bindingResult.hasErrors()) {
            ProductDetailDto dto = productService.selectByProductId(id);
            model.addAttribute("item", dto);
            return "items/edit";
        }

        try {
            productService.updateProduct(id, productForm, currentUser.getUser().getId());
        } catch (SecurityException e) {
            return "redirect:/";
        } catch (IllegalStateException e) {
            return "redirect:/";
        } catch (IOException e) {
            bindingResult.reject("error.image.upload", "画像の保存に失敗しました。");
            ProductDetailDto dto = productService.selectByProductId(id);
            model.addAttribute("item", dto);
            return "items/edit";
        }
    }
}

