package in.tech_camp.furima_a.form;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserForm {
  @NotBlank(message = "Nickname can't be blank")
  private String nickname;

  @NotBlank(message = "Email can't be blank")
  @Email(message = "Email should be valid")
  private String email;

  @NotBlank(message = "Password can't be blank")
  @Length(min = 6, message = "Password is too short (minimum is 6 characters)")
  @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]+$", message = "Password must include both letters and numbers")
  private String password;

  @NotBlank(message = "Password confirmation can't be blank")
  @Length(min = 6, message = "Password confirmation is too short (minimum is 6 characters)")
  private String passwordConfirmation;

  @NotBlank(message = "Last name can't be blank")
  @Pattern(regexp = "^[ぁ-んァ-ヶ一-龥々ー]+$", message = "Last name is invalid. Input full-width characters")
  private String lastName;

  @NotBlank(message = "First name can't be blank")
  @Pattern(regexp = "^[ぁ-んァ-ヶ一-龥々ー]+$", message = "First name is invalid. Input full-width characters")
  private String firstName;

  @NotBlank(message = "Last name kana can't be blank")
  @Pattern(regexp = "^[ァ-ヶー]+$", message = "Last name kana is invalid. Input full-width katakana characters")
  private String lastNameKana;

  @NotBlank(message = "First name kana can't be blank")
  @Pattern(regexp = "^[ァ-ヶー]+$", message = "First name kana is invalid. Input full-width katakana characters")
  private String firstNameKana;

  @NotNull(message = "Birth year can't be blank")
    private Integer birthYear;

  @NotNull(message = "Birth month can't be blank")
    private Integer birthMonth;

  @NotNull(message = "Birth day can't be blank")
    private Integer birthDay;

}
