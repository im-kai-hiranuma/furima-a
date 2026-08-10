package in.tech_camp.furima_a.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Category {
  SELECT(0, "---"),
  LADIES(1, "レディース"),
  MENS(2, "メンズ"),
  BABY_KIDS(3, "ベビー・キッズ"),
  INTERIOR(4, "インテリア・住まい・小物"),
  BOOKS(5, "本・音楽・ゲーム"),
  HOBBY(6, "おもちゃ・ホビー・グッズ"),
  COSMETICS(7, "コスメ・香水・美容"),
  ELECTRONICS(8, "家電・スマホ・カメラ"),
  SPORTS(9, "スポーツ・レジャー"),
  HANDMADE(10, "ハンドメイド"),
  OTHER(11, "その他");

  private final int code;
  private final String displayName;

  public static Category fromCode(int code) {
    for (Category category : values()) {
      if (category.getCode() == code) {
        return category;
      }
    }
    return SELECT;
  }

  public static Category fromDisplayName(String displayName) {
    for (Category category : values()) {
      if (category.getDisplayName().equals(displayName)) {
        return category;
      }
    }
    return SELECT;
  }
}