package in.tech_camp.furima_a.entity;

import lombok.Data;

@Data
public class Purchase {
  private Long id;
  private Long userId;
  private Long productId;
}
