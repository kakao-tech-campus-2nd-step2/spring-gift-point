package gift.DTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

@Entity
@Table
public class Member {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @NotBlank(message = "Email은 필수입니다.")
  @Email(message = "Email 형식으로 제출해주십오.")
  @Column(nullable = false, unique = true)
  private String email;
  @NotBlank(message = "비밀번호는 필수입니다.")
  @Size(min = 5, max = 15)
  @Column(nullable = false)
  private String password;

  @PositiveOrZero
  private int point;

  protected Member() {

  }

  public Member(Long id, String email, String password) {
    this.id = id;
    this.email = email;
    this.password = password;
    this.point = 0;
  }

  public Member(String email, String password) {
    this.email = email;
    this.password = password;
    this.point = 0;
  }

  public Long getId() {
    return this.id;
  }

  public String getEmail() {
    return this.email;
  }

  public String getPassword() {
    return this.password;
  }

  public int getPoint() {
    return point;
  }

  public boolean matchLoginInfo(MemberDto memberDtoByEmail) {
    return this.email.equals(memberDtoByEmail.getEmail()) && this.password.equals(
      memberDtoByEmail.getPassword());
  }

  public void subtractPoint(int point) throws IllegalAccessException {
    if (point > this.point) {
      throw new IllegalAccessException();
    }
    this.point -= point;
  }

  public void addPoint(int point) {
    this.point += point;
  }
}
