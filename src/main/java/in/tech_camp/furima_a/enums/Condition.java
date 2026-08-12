package in.tech_camp.furima_a.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Condition {
  SELECT(0, "---"),
  NEW(1, "新品・未使用"),
  LIKE_NEW(2, "未使用に近い"),
  GOOD(3, "目立った傷や汚れなし"),
  FAIR(4, "やや傷や汚れあり"),
  POOR(5, "傷や汚れあり"),
  BAD(6, "全体的に状態が悪い");

  private final int code;
  private final String displayName;

  public static Condition fromCode(int code) {
    for (Condition condition : values()) {
      if (condition.getCode() == code) {
        return condition;
      }
    }
    return SELECT;
  }

  public static Condition fromDisplayName(String displayName) {
    for (Condition condition : values()) {
      if (condition.getDisplayName().equals(displayName)) {
        return condition;
      }
    }
    return SELECT;
  }
}