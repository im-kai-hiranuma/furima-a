package in.tech_camp.furima_a.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 画面の入力項目を受け取るための専用クラス
 * 不正なデータがシステムに混入するのを防ぐ
 */
@Data
public class OrderForm {

    @NotBlank(message = "クレジットカード情報を正しく入力してください")
    private String token;

    //「3桁ハイフン4桁」のフォーマットを指定する
    @NotBlank(message = "郵便番号を入力してください")
    @Pattern(regexp = "^\\d{3}-\\d{4}$", message = "郵便番号は「3桁ハイフン4桁」の半角文字列のみ保存可能なこと")
    private String postNumber;

    // 都道府県が選択されていることを必須にする
    @NotNull(message = "都道府県を選択してください")
    @Min(value = 1, message = "都道府県を選択してください")
    private Integer prefecture;

    @NotBlank(message = "市区町村を入力してください")
    private String city;

    @NotBlank(message = "番地を入力してください")
    private String block;

    // 実装要件で「建物名は任意」と指定されているため、バリデーションは不要
    private String building;

    // ハイフンなし、10桁または11桁の半角数字を指定する
    @NotBlank(message = "電話番号を入力してください")
    @Pattern(regexp = "^\\d{10,11}$", message = "電話番号は10桁以上11桁以内の半角数値のみ保存可能なこと")
    private String phone;
}