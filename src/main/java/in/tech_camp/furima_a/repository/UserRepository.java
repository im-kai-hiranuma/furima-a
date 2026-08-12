package in.tech_camp.furima_a.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.furima_a.entity.UserEntity;

@Mapper
public interface UserRepository {
  
    // 1. 新規ユーザーの保存処理
    @Insert("INSERT INTO users (nickname, email, password, last_name, first_name, last_name_kana, first_name_kana, birthday) " +
            "VALUES (#{nickname}, #{email}, #{password}, #{lastName}, #{firstName}, #{lastNameKana}, #{firstNameKana}, #{birthday})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(UserEntity user);

    // 2. メールアドレスによるユーザー検索（ログイン時や重複チェックで使用）
    @Select("SELECT * FROM users WHERE email = #{email}")
    UserEntity findByEmail(String email);
}
