package gift.model;


import gift.exception.InputException;
import gift.exception.member.InvalidPointException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.util.StringUtils;

@Entity
@Table(name = "member")
public class Member extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(nullable = false)
    private Integer point;

    private static final int DISCOUNT_RATE = 10;

    protected Member() {
    }

    public Member(Long id, String email, String password, Role role, Integer point) {
        validEmail(email);
        validPassword(password);
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.point = point;
    }

    public Member(String email, String password, Role role, Integer point) {
        validEmail(email);
        validPassword(password);
        validPoint(point);
        this.email = email;
        this.password = password;
        this.role = role;
        this.point = point;
    }

    //oAuth2 가입
    public Member(String email) {
        validEmail(email);
        this.email = email;
        this.password = "OAUTH2";
        this.role = Role.ROLE_USER;
        this.point = 0;
    }

    // 회원가입 시 ROLE 기본값은 일반 유저로 한다.
    public Member(String email, String password) {
        validEmail(email);
        validPassword(password);
        this.email = email;
        this.password = password;
        this.role = Role.ROLE_USER;
        this.point = 0;
    }

    public Member(String email, String password, Role role) {
        validEmail(email);
        validPassword(password);
        this.email = email;
        this.password = password;
        this.role = role;
        this.point = 0;
    }

    public void makeAdminMember() {
        this.role = Role.ROLE_ADMIN;
    }

    public void addPoint(Integer price) {
        int addedPoint = price / DISCOUNT_RATE;
        validPoint(addedPoint);
        this.point += addedPoint;
    }

    public void subPoint(Integer subPoint) {
        validPoint(subPoint);
        validUsingPoint(subPoint);
        this.point -= subPoint;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public Integer getPoint() {
        return point;
    }

    public boolean validatingMember(String email, String password) {
        return this.email.equals(email) && this.password.equals(password);
    }

    private void validEmail(String email) {
        if(!StringUtils.hasText(email)) {
            throw new InputException("알 수 없는 오류가 발생하였습니다.");
        }
    }

    private void validPassword(String password) {
        if(!StringUtils.hasText(password)) {
            throw new InputException("알 수 없는 오류가 발생하였습니다.");
        }
    }

    private void validPoint(Integer point) {
        if(point == null || point < 0) {
            throw new InputException("알 수 없는 오류가 발생하였습니다.");
        }
    }

    private void validUsingPoint(Integer subPoint) {
        if (point - subPoint < 0) {
            throw new InvalidPointException();
        }
    }
}
