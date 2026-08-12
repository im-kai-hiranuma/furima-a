package in.tech_camp.furima_a.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 開発時：全てのURLへのアクセスを許可
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/items/*/orders").authenticated() //購入ページにアクセスためにログインが必要
                .anyRequest().permitAll()
            )

            .formLogin(login -> login
                .loginPage("/users/sign_in")            // ログイン画面のURL（GET）
                .loginProcessingUrl("/users/sign_in")   // ログイン処理を実行するURL（POST）
                .usernameParameter("email")             // ログインIDとして使う項目（メールアドレス）
                .passwordParameter("password")          // パスワードとして使う項目
                .defaultSuccessUrl("/", true)           // ログイン成功時の移動先（トップページ）
                .failureUrl("/users/sign_in?error")     // ログイン失敗時の移動先（エラー表示付き）
                .permitAll()
            )

            .logout(logout -> logout
                .logoutUrl("/users/sign_out")           // ログアウト処理を実行するURL（POST）
                .logoutSuccessUrl("/")                  // ログアウト成功時の移動先（トップページ）
                .permitAll()
            )
            
            // 開発時：POST送信等で403エラーが出ないようCSRFを無効化
            .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}