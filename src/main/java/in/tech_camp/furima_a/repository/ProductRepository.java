package in.tech_camp.furima_a.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.furima_a.dto.repository.ProductDetailQueryResult;
import in.tech_camp.furima_a.dto.repository.ProductQueryResult;

@Mapper
public interface ProductRepository {

  // 商品一覧表示機能
  @Select("""
      SELECT p.id, p.img, p.name, p.price, p.delivery_fee, b.product_id
      FROM products p
      LEFT JOIN purchases b
      ON p.id = b.product_id
      ORDER BY p.id DESC
      """)
  List<ProductQueryResult> findAll();

  // 商品詳細表示
  @Select("""
      SELECT
        p.id,
        p.name, p.img, p.price, p.delivery_fee, p.description, p.user_id,
        u.nickname, p.category, p.condition, p.prefecture, p.until_delivery, b.product_id,
        CASE WHEN b.product_id IS NOT NULL THEN 1 ELSE 0 END AS soldout
      FROM products p
      LEFT JOIN users u ON p.user_id = u.id
      LEFT JOIN purchases b ON p.id = b.product_id
      WHERE p.id = #{id}
      """)
  ProductDetailQueryResult selectByProductId(Long id);
  
}