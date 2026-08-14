package in.tech_camp.furima_a.dto.repository;

import lombok.Data;

@Data
public class ProductEditForm {

  private Long id;
  private String img; // 画像
  private String name; // 商品名
  private String description; // 商品説明
  private int category; // カテゴリー
  private int condition; // 商品の状態
  private int deliveryFee; // 配送料の負担
  private int prefecture; // 発送元の地域
  private int untilDelivery; // 発送までの日数
  private Long price; // 価格
}