package in.tech_camp.furima_a.controller;

import in.tech_camp.furima_a.dto.OrderForm;
import in.tech_camp.furima_a.enums.PrefectureType;
import in.tech_camp.furima_a.repository.OrderRepository;
import in.tech_camp.furima_a.service.OrderService;
import in.tech_camp.furima_a.dto.repository.ProductQueryResult; 
import in.tech_camp.furima_a.repository.ProductRepository; 
import in.tech_camp.furima_a.entity.User;
import in.tech_camp.furima_a.repository.UserRepository; 

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/items/{id}/orders") 
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Value("${payjp.public-key}")
    private String payjpPublicKey;

    @GetMapping
    public String index(@PathVariable("id") Long productId,
                        Model model,
                        @AuthenticationPrincipal UserDetails userDetails) {
        
        ProductQueryResult product = productRepository.findById(productId);
        User currentUser = userRepository.findByEmail(userDetails.getUsername());

        // 悪意のあるユーザーがURLを直接入力してアクセスしてきた場合を防ぐ
        // 商品が存在しない、既に売り切れている、またはアクセス者が自身の出品した商品の場合はトップへリダイレクト
        if (product == null || orderRepository.isSoldOut(productId) || currentUser.getId().equals(product.getUserId())) {
            return "redirect:/";
        }

        model.addAttribute("product", product);
        model.addAttribute("orderForm", new OrderForm());
        model.addAttribute("prefectures", PrefectureType.values()); 
        model.addAttribute("payjpPublicKey", payjpPublicKey);

        return "orders/index";
    }

    @PostMapping
    public String create(@PathVariable("id") Long productId,
                         @Valid @ModelAttribute("orderForm") OrderForm orderForm,
                         BindingResult bindingResult,
                         Model model,
                         @AuthenticationPrincipal UserDetails userDetails) {

        ProductQueryResult product = productRepository.findById(productId);
        User currentUser = userRepository.findByEmail(userDetails.getUsername());

        // 外部ツール（Postmanなど）を使ってPOSTリクエストを直接送信された場合に対する防御
        if (product == null || orderRepository.isSoldOut(productId) || currentUser.getId().equals(product.getUserId())) {
            return "redirect:/";
        }

        // バリデーションエラーがあった場合、redirect ではなく return "orders/index" を使う
        // これにより、ユーザーが入力した情報が消えずに画面に保持
        if (bindingResult.hasErrors()) {
            model.addAttribute("product", product);
            model.addAttribute("prefectures", PrefectureType.values());
            model.addAttribute("payjpPublicKey", payjpPublicKey);
            return "orders/index";
        }

        try {
            orderService.processOrder(orderForm, currentUser.getId(), productId, product.getPrice());
        } catch (Exception e) {
            // カード残高不足などでPAY.JP側からエラーが返ってきた場合、フォームの内容を保ったままエラーメッセージを表示
            model.addAttribute("product", product);
            model.addAttribute("prefectures", PrefectureType.values());
            model.addAttribute("payjpPublicKey", payjpPublicKey);
            model.addAttribute("errorMessage", "クレジットカード決済に失敗しました。カード情報をご確認ください。");
            return "orders/index";
        }

        return "redirect:/";
    }
}