package gift.DTO;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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

  @Embedded
  private PointVo pointVo;

  protected Member() {

  }

  public Member(Long id, String email, String password, PointVo pointVo) {
    this.id = id;
    this.email = email;
    this.password = password;
    this.pointVo = pointVo;
  }

  public Member(String email, String password, PointVo pointVo) {
    this.email = email;
    this.password = password;
    this.pointVo = pointVo;
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

  public PointVo getPointVo() {
    return this.pointVo;
  }

  public boolean matchLoginInfo(MemberDto memberDtoByEmail) {
    return this.email.equals(memberDtoByEmail.getEmail()) && this.password.equals(
      memberDtoByEmail.getPassword());
  }

  public PointVo subtractPoint(PointVo pointVo) throws IllegalAccessException {
    if (pointVo.getPoint() > this.pointVo.getPoint()) {
      throw new IllegalAccessException("보유 포인트가 차감 포인트보다 적습니다.");
    }
    return new PointVo(this.pointVo.getPoint()-pointVo.getPoint());
  }

  public PointVo addPoint(PointVo pointVo) throws IllegalAccessException {
    if(pointVo.getPoint()<0){
      throw new IllegalAccessException("point는 1원 이상 충전 가능합니다");
    }
    return new PointVo(this.pointVo.getPoint()+pointVo.getPoint());
  }

}
