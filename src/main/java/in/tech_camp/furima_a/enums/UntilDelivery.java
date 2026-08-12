package in.tech_camp.furima_a.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UntilDelivery {
  SELECT(0, "---"),
  FAST(1, "1〜2日で発送"),
  NORMAL(2, "2〜3日で発送"),
  SLOW(3, "4〜7日で発送");

  private final int code;
  private final String displayName;

  public static UntilDelivery fromCode(int code) {
    for (UntilDelivery until : values()) {
      if (until.getCode() == code) {
        return until;
      }
    }
    return SELECT;
  }

  public static UntilDelivery fromDisplayName(String displayName) {
    for (UntilDelivery until : values()) {
      if (until.getDisplayName().equals(displayName)) {
        return until;
      }
    }
    return SELECT;
  }
}