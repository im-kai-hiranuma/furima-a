package in.tech_camp.furima_a.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeliveryFeeType {
  SELECT(0, "---"),
  INCLUDED(1, "着払い(購入者負担)"),
  EXCLUDED(2, "送料込み(出品者負担)");

  private final int code;
  private final String label;

  public static DeliveryFeeType fromCode(int code) {
    for (DeliveryFeeType type : values()) {
      if (type.getCode() == code) {
        return type;
      }
    }
    return SELECT;
  }
}