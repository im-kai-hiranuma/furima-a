package in.tech_camp.furima_a.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import in.tech_camp.furima_a.entity.UserEntity;
import lombok.Data;

@Data
public class CustomUserDetails implements UserDetails {

    // データベースから取得したユーザー情報を格納する変数
    private final UserEntity user;

    public CustomUserDetails(UserEntity user) {
        this.user = user;
    }

    // 後でログイン中のユーザー情報（ニックネームなど）を取り出すためのメソッド
    public UserEntity getUser() {
        return user;
    }

    @Override
    public String getUsername() {
        // メールアドレスをログインIDとして使うため、emailを返す
        return user.getEmail();
    }

    @Override
    public String getPassword() {
        // 暗号化されたパスワードを返します（Spring Securityが自動で照合してくれます）
        return user.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    public Long getId(){
        return user.getId();
    }
    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }
}