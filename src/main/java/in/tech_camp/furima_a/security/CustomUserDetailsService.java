package in.tech_camp.furima_a.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import in.tech_camp.furima_a.entity.UserEntity;
import in.tech_camp.furima_a.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. 先ほど作った UserRepository を使って、メールアドレスで検索
        UserEntity user = userRepository.findByEmail(email);

        // 2. もしデータベースに見つからなかったらエラーを投げる
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        // 3. 見つかったら、先ほど作った CustomUserDetails の箱に入れてSpring Securityに渡す
        return new CustomUserDetails(user);
    }
}