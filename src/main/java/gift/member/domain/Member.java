package gift.member.domain;

import gift.wish.domain.Wish;
import jakarta.persistence.*;
import org.hibernate.Hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "members")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private MemberType memberType = MemberType.USER;
    @Embedded
    private Email email;
    @Embedded
    private Password password;
    @Embedded
    private Nickname nickname;
    private String accessToken;
    private Long point = 0L;

    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Wish> wishes = new ArrayList<>();

    // JDBC 에서 엔티티 클래스를 인스턴스화할 때 반드시 기본 생성자와 파라미터 생성자가 필요하다
    public Member() {
    }

    public Member(Long id, MemberType memberType, Email email, Password password, Nickname nickname) {
        this.id = id;
        this.memberType = memberType;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.accessToken = accessToken;
    }

    public Member(Long id, MemberType memberType, Email email, Password password, Nickname nickname, String accessToken, Long point) {
        this.id = id;
        this.memberType = memberType;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.accessToken = accessToken;
        this.point = point;
    }

    public void updateAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getId() {
        return id;
    }

    public Email getEmail() {
        return email;
    }

    public Password getPassword() {
        return password;
    }

    public Nickname getNickname() {
        return nickname;
    }

    public MemberType getMemberType() {
        return memberType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public Long getPoint() {
        return point;
    }

    public boolean checkNew() {
        return id == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        Member item = (Member) o;
        return Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
