package in.tech_camp.furima_a.service;

import java.time.LocalDate;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import in.tech_camp.furima_a.entity.UserEntity;
import in.tech_camp.furima_a.form.UserForm;
import in.tech_camp.furima_a.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
  
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  
  // 1. メールアドレスの重複チェック用メソッド
    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email) != null;
    }

    // 2. ユーザー登録処理
    public void registerUser(UserForm userForm) {
        UserEntity user = new UserEntity();

        // フォームからの基本情報をセット
        user.setNickname(userForm.getNickname());
        user.setEmail(userForm.getEmail());
        
        // パスワードを暗号化（BCrypt）してセット
        String encodedPassword = passwordEncoder.encode(userForm.getPassword());
        user.setPassword(encodedPassword);

        user.setLastName(userForm.getLastName());
        user.setFirstName(userForm.getFirstName());
        user.setLastNameKana(userForm.getLastNameKana());
        user.setFirstNameKana(userForm.getFirstNameKana());

        // バラバラの年・月・日を1つの LocalDate に合体してセット
        LocalDate birthday = LocalDate.of(
            userForm.getBirthYear(),
            userForm.getBirthMonth(),
            userForm.getBirthDay()
        );
        user.setBirthday(birthday);

        // データベースへ保存
        userRepository.insert(user);
    }
} 
