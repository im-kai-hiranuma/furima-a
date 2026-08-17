package in.tech_camp.furima_a.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import in.tech_camp.furima_a.dto.OrderForm;
import in.tech_camp.furima_a.entity.Address;
import in.tech_camp.furima_a.entity.Purchase;
import in.tech_camp.furima_a.repository.OrderRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @Value("${payjp.secret-key}")
    private String secretKey;

    /**
     * @Transactional を使ってPAY.JPでの決済、履歴保存、住所保存のいずれかで
     * 例外が発生した場合、すべてのデータベース操作をロールバックにし、
     * 「お金だけ引かれて商品は買えていない」というデータ不整合を防ぐ
     */
    @Transactional
    public void processOrder(OrderForm form, Long userId, Long productId, Long price) {
        // PAY.JP APIを呼び出してクレジットカード決済を実行
        charge(price, form.getToken());

        // purchases テーブルへ保存
        Purchase purchase = new Purchase();
        purchase.setUserId(userId);
        purchase.setProductId(productId);
        orderRepository.insertPurchase(purchase);

        // addresses テーブルへ保存
        Address address = new Address();
        address.setPurchaseId(purchase.getId()); // @Options により自動取得されたIDをセット
        address.setPostNumber(form.getPostNumber());
        address.setPrefecture(form.getPrefecture());
        address.setCity(form.getCity());
        address.setBlock(form.getBlock());
        address.setBuilding(form.getBuilding());
        address.setPhone(form.getPhone());
        
        orderRepository.insertAddress(address);
    }

    /**
     * RestTemplateを利用してPAY.JPのサーバーへ POST リクエストを送る
     * Basic認証の仕様に基づき、シークレットキーをエンコードして送信
     */
    private void charge(Long price, String token) {
        RestTemplate restTemplate = new RestTemplate();
        // Basic認証ヘッダーの作成（秘密鍵:空文字）
        String auth = secretKey + ":";
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + encodedAuth);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // リクエストボディの作成
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("amount", String.valueOf(price));
        body.add("card", token);
        body.add("currency", "jpy");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        // PAY.JP APIに決済リクエストを送信
        try {
            restTemplate.postForEntity("https://api.pay.jp/v1/charges", request, String.class);
        } catch (Exception e) {
            throw new RuntimeException("決済に失敗しました: " + e.getMessage(), e);
        }
    }
}