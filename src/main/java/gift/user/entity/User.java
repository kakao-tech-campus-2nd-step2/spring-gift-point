package gift.user.entity;

import gift.util.BaseEntity;
import gift.wish.entity.Wish;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User extends BaseEntity {

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

  @Column
  private Integer point;

  @PrePersist
  protected void onCreate() {
    if (this.role == null) {
      this.role = UserRole.ROLE_USER;
    }

    if (this.point == null){
      this.point = 0;
    }
  }
}
