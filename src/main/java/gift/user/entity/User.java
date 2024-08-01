package gift.user.entity;

import gift.wish.entity.Wish;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Email(message = "유효하지 않은 이메일 형식입니다.")
  @NotBlank(message = "이메일을 입력해야 합니다.")
  @Column(nullable = false, unique = true)
  private String email;

//  @NotBlank(message = "비밀번호를 입력해야 합니다.")
  @Column(nullable = true)
  private String password;

  @Column(nullable = true)
  private String kakaoAccessToken;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private UserRole role;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Wish> wishs;

  @PrePersist
  protected void onCreate() {
    if (this.role == null) {
      this.role = UserRole.ROLE_USER;
    }
  }

  // Getters and setters
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

  public UserRole getRole() {
    return role;
  }

  public void setRole(UserRole role) {
    this.role = role;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }


  public String getKakaoAccessToken() {
    return kakaoAccessToken;
  }

  public void setKakaoAccessToken(String kakaoAccessToken) {
    this.kakaoAccessToken = kakaoAccessToken;
  }

  public List<Wish> getWishs() {
    return wishs;
  }

  public void setWishs(List<Wish> wishs) {
    this.wishs = wishs;
  }

}
