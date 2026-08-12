package in.tech_camp.furima_a.form; 

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductForm {

    @NotBlank(message = "商品名を入力してください")
    @Size(max = 40, message = "商品の名称は40文字以内で入力してください")
    private String name;

    @NotBlank(message = "商品名を入力してください")
    @Size(max = 1000, message = "商品の名称は1000文字以内で入力してください")
    private String description;
    
    @NotNull(message = "カテゴリーを選択してください")
    @Min(value = 1, message = "カテゴリーを選択してください")
    private Integer category;

    @NotNull(message = "商品の状態を選択してください")
    @Min(value = 1, message = "商品の状態を選択してください")
    private Integer condition;

    @NotNull(message = "配送料の負担を選択してください")
    @Min(value = 1, message = "配送料の負担を選択してください")
    private Integer deliveryFee;

    @NotNull(message = "発送元の地域を選択してください")
    @Min(value = 1, message = "発送元の地域を選択してください")
    private Integer prefecture;

    @NotNull(message = "発送までの日数を選択してください")
    @Min(value = 1, message = "発送までの日数を選択してください")
    private Integer untilDelivery;

    @NotNull(message = "価格を入力してください")
    @Min(value = 300, message = "価格は300円以上に設定してください")
    @Max(value = 9999999, message = "価格は9,999,999円以下に設定してください")
    private Long price;

    private MultipartFile img;
}
