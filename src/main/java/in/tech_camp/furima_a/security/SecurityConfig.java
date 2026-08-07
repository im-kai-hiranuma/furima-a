package in.tech_camp.furima_a.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 開発時：全てのURLへのアクセスを許可
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            )
            // 開発時：POST送信等で403エラーが出ないようCSRFを無効化
            .csrf(csrf -> csrf.disable());

        return http.build();
    }
}