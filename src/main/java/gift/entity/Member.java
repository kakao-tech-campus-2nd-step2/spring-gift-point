package gift.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import gift.exception.orderException.deductPointException;
import jakarta.persistence.*;
import org.hibernate.usertype.UserType;

import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = true)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private MemberType type;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "member", orphanRemoval = true)
    @JsonManagedReference
    private List<Wish> wishes = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "member", orphanRemoval = true)
    @JsonManagedReference
    private List<Order> orders = new ArrayList<>();

    @Column(nullable = true, name="kakao-token")
    private String kakaoToken;

    @Column(name="point")
    private Long point;


    public Member() {}

    @ConstructorProperties({"email", "password"})
    public Member(String email, String password) {
        this.email = email;
        this.password = password;
        this.type = MemberType.NORMAL_USER;
    }


    public Member(MemberType type, String email, String kakaoToken) {
        this.type = type;
        this.email = email;
        this.kakaoToken = kakaoToken;
        this.point = 0L;
    }

    public Member(String email, MemberType type) {
        this.email = email;
        this.type = type;
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

    public MemberType getType() {
        return type;
    }

    public List<Wish> getWishes() {
        return wishes;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public String getKakaoToken() { return kakaoToken; }

    public Long getPoint() { return point; }

    public void deductPoints(Long point){
        if(this.point - point <0){
            throw new deductPointException("가지고 있는 포인트보다 많이 입력했습니다. 다시 입력해주세요");
        }
        this.point -= point;
    }

}
