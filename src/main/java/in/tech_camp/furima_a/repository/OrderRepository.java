package in.tech_camp.furima_a.repository;

import in.tech_camp.furima_a.entity.Address;
import in.tech_camp.furima_a.entity.Purchase;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OrderRepository {

    /**
     * 対象のproduct_idがpurchasesテーブルに存在するかどうかをEXISTSで確認
     * 1件でも見つかれば即座に判定が完了するため、高速な売り切れチェックが可能
     */
    @Select("SELECT EXISTS(SELECT 1 FROM purchases WHERE product_id = #{productId})")
    boolean isSoldOut(Long productId);

    /**
     * 複数テーブル保存
     * @Options　を指定することで、INSERT実行直後にPostgreSQLが自動生成したIDが、
     * 引数で渡したpurchaseオブジェクトのidフィールドに自動的に格納
     */
    @Insert("INSERT INTO purchases (user_id, product_id) VALUES (#{userId}, #{productId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertPurchase(Purchase purchase);

    /**
     * 上で取得したpurchase_idを使って、配送先住所を保存
     */
    @Insert("INSERT INTO addresses (purchase_id, post_number, prefecture, city, block, building, phone) " +
            "VALUES (#{purchaseId}, #{postNumber}, #{prefecture}, #{city}, #{block}, #{building}, #{phone})")
    void insertAddress(Address address);
}