package in.tech_camp.furima_a.dto.repository;

import lombok.Data;

@Data
public class ProductQueryResult {

  private Long id;
  private String img;
  private String name;
  private Long price;
  private int deliveryFee;
  private Long productId;

}