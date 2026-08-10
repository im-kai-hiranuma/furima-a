package in.tech_camp.furima_a.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
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
                // 本番環境:全てのURLへのアクセスに認証を必須にする
                //　.anyRequest().authenticated()
            )
            // Basic認証を有効にする
                // .httpBasic(Customizer.withDefaults())

            // 開発時：POST送信等で403エラーが出ないようCSRFを無効化
            .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailService(PasswordEncoder encoder) {
      String username = System.getenv("BASIC_AUTH_USER");
      String password = System.getenv("BASIC_AUTH_PASSWORD");

      UserDetails user = User.withUsername(username)
          .password(encoder.encode(password))
          .roles("ADMIN")
          .build();
      return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
    }
}