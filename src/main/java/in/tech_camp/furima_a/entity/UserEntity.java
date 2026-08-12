package in.tech_camp.furima_a.entity;

import java.time.LocalDate;

import lombok.Data;

@Data
public class UserEntity {
  private Long id;
  private String nickname;
  private String email;
  private String password;
  private String lastName;
  private String firstName;
  private String lastNameKana;
  private String firstNameKana;
  private LocalDate birthday;
}
