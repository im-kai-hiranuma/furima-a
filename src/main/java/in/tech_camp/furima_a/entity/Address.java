package in.tech_camp.furima_a.entity;

import lombok.Data;

@Data
public class Address {
  private Long id;
  private Long purchaseId;
  private String postNumber;
  private Integer prefecture;
  private String city;
  private String block;
  private String building;
  private String phone;
}
