package gift.domain.member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private MemberType memberType;

    @Column(unique = true)
    private String email;

    @Column
    private String password;

    @Column(unique = true)
    private Long kakaoId;

    @Column
    private String kakaoToken;

    @Column
    private Long point = 0L;

    public Member() {

    }

    public Member(Long id) {
        this.id = id;
    }

    public Member(MemberType memberType, String email, String password) {
        this.memberType = memberType;
        this.email = email;
        this.password = password;
    }

    public Member(MemberType memberType, Long kakaoId) {
        this.memberType = memberType;
        this.kakaoId = kakaoId;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public MemberType getMemberType() {
        return memberType;
    }

    public String getKakaoToken() {
        return kakaoToken;
    }

    public void setKakaoToken(String kakaoToken) {
        this.kakaoToken = kakaoToken;
    }

    public Long getPoint() {
        return point;
    }
    public void addPoint(Long point) {
        if (point < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "사용할 포인트는 0 이상이여야 합니다");
        }
        this.point += point;
    }

    public void subPoint(Long point) {
        if (point < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "사용할 포인트는 0 이상이여야 합니다");
        }
        else if (this.point < point) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "현재 보유중인 포인트 보다 많은 포인트를 사용할 수 없습니다");
        }
        this.point -= point;
    }
}
