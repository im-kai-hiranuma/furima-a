package in.tech_camp.furima_a.dto.repository;

import lombok.Data;

@Data
public class ProductDetailQueryResult {

  private Long id;
  private String name;
  private String img;
  private Long price;
  private int deliveryFee;
  private String description;
  private String nickname;
  private int category;
  private int condition;
  private int prefecture;
  private int untilDelivery;
  private boolean soldout;
  private Long userId;

}