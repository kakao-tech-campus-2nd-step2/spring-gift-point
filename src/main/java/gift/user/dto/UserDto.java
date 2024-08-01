package gift.user.dto;

import gift.user.entity.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public class UserDto {
  private Long id;

  @Email(message = "유효하지 않은 이메일 형식입니다.")
  @NotBlank(message = "이메일을 입력해야 합니다.")
  private String email;

//  @NotBlank(message = "비밀번호를 입력해야 합니다.")
  private String password;

  private UserRole userRole = UserRole.ROLE_USER;


  public UserDto(Long id, String email, String password, UserRole userRole) {
    this.id = id;
    this.email = email;
    this.password = password;
    this.userRole = userRole;
  }

  public UserDto() {

  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public UserRole getUserRole() {
    return userRole;
  }

  public void setUserRole(UserRole userRole) {
    this.userRole = userRole;
  }
}
