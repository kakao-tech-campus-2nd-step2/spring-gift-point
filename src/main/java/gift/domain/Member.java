package gift.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    private String accessToken;

    private Long memberReferencedId;
    private MemberType memberType;
    public enum MemberType {
        KAKAO,
        DEFAULT
    }
    private Integer points = 0;

    protected Member() {
    }

    public Member(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Member(String email, String password, String accessToken, Long memberReferencedId, MemberType memberType) {
        this.email = email;
        this.password = password;
        this.accessToken = accessToken;
        this.memberReferencedId = (memberType == MemberType.DEFAULT) ? null : memberReferencedId; // 기본 회원의 경우, 이 필드는 사용되지 않음
        this.memberType = memberType;
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

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getMemberReferencedId() {
        return memberReferencedId;
    }

    public MemberType getMemberType() {
        return memberType;
    }

    public Integer getPoints() {
        return points;
    }

    public void addPoints(int amount) {
        if (this.points == null) {
            this.points = 0;
        }
        this.points += amount;
    }

    public void subtractPoints(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("사용할 포인트 값은 0, 양수만 가능하다.");
        }
        if (points < amount) {
            throw new IllegalStateException("사용할 포인트 값은 기존 포인트보다 작아야 한다.");
        }
        this.points -= amount;
    }
}
