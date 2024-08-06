package gift.domain.member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

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
}
