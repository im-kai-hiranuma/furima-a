package in.tech_camp.furima_a.dto;

import lombok.Data;

@Data
public class ProductListDto {

  private Long id;
  private String img;
  private String name;
  private Long price;
  private String deliveryFee;
  private boolean soldout;

}