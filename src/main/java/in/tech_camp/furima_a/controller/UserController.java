package in.tech_camp.furima_a.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import in.tech_camp.furima_a.form.UserForm;
import in.tech_camp.furima_a.service.UserService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 画面を表示する処理（GET）
    @GetMapping("/users/sign_up")
    public String showSignUp(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "users/sign_up";
    }

    // 登録ボタンが押された時の処理（POST）
    @PostMapping("/users/sign_up")
    public String registerUser(@Validated @ModelAttribute UserForm userForm, BindingResult bindingResult) {

        // 1. パスワードとパスワード（確認）の一致チェック
        if (userForm.getPassword() != null && userForm.getPasswordConfirmation() != null) {
            if (!userForm.getPassword().equals(userForm.getPasswordConfirmation())) {
                bindingResult.rejectValue("passwordConfirmation", "", "Password confirmation doesn't match Password");
            }
        }

        // 2. メールアドレスの重複チェック
        if (userForm.getEmail() != null && userService.existsByEmail(userForm.getEmail())) {
            bindingResult.rejectValue("email", "", "Email has already been taken");
        }

        // 3. バリデーションエラーまたは上記チェックでエラーがある場合
        if (bindingResult.hasErrors()) {
            return "users/sign_up";
        }

        // 4. エラーがなければデータベースに保存
        userService.registerUser(userForm);

        // 5. 登録完了後はトップページへリダイレクト
        return "redirect:/";
    }
  
    // ログイン画面を表示する処理
    @GetMapping("/users/sign_in")
    public String showSignIn() {
        return "users/sign_in";
    }
}