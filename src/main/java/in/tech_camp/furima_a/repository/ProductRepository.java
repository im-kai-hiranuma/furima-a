package in.tech_camp.furima_a.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.furima_a.dto.repository.ProductQueryResult;
import in.tech_camp.furima_a.entity.ProductEntity;

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

  @Insert("""
      INSERT INTO products (user_id, name, description, category, condition, delivery_fee, prefecture, until_delivery, price, img)
      VALUES (#{userId}, #{name}, #{description}, #{category}, #{condition}, #{deliveryFee}, #{prefecture}, #{untilDelivery}, #{price}, #{img})
      """)
  void insert(ProductEntity product);
}