package in.tech_camp.furima_a.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import in.tech_camp.furima_a.enums.Category;
import in.tech_camp.furima_a.enums.Condition;
import in.tech_camp.furima_a.enums.DeliveryFeeType;
import in.tech_camp.furima_a.enums.PrefectureType;
import in.tech_camp.furima_a.enums.UntilDelivery;
import in.tech_camp.furima_a.form.ProductForm;
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
    public String showProductForm(Model model) {
        model.addAttribute("productForm", new ProductForm());
        return "items/new";
    }

    // 出品処理（POST）
    @PostMapping("/post")
    public String createProduct(
            @ModelAttribute("productForm") @Validated ProductForm productForm,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "items/new";
        }

        Long testUserId = 1L;

        try {
            productService.createProduct(productForm, testUserId);
        } catch (Exception e) { 
            model.addAttribute("errorMessage", "商品の登録に失敗しました。");
            return "items/new";
        }
        return "redirect:/";
    }

    // --- プルダウン用 Enum 共通データ設定 ---

    @ModelAttribute("categories")
    public Category[] getCategories() {
        return Category.values(); 
    }

    @ModelAttribute("conditions")
    public Condition[] getConditions() {
        return Condition.values(); 
    }

    @ModelAttribute("deliveryFees")
    public DeliveryFeeType[] getDeliveryFees() {
        return DeliveryFeeType.values();
    }

    @ModelAttribute("prefectures")
    public PrefectureType[] getPrefectures() {
        return PrefectureType.values();
    }

    @ModelAttribute("untilDeliveries")
    public UntilDelivery[] getUntilDeliveries() {
        return UntilDelivery.values();
    }
}