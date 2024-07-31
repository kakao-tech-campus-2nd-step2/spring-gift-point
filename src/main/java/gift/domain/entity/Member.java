package gift.domain.entity;

import gift.global.WebConfig.Constants.Domain.Member.Permission;
import gift.global.WebConfig.Constants.Domain.Member.Type;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 64)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Type userType;

    @Column(nullable = false, length = 64)
    @Enumerated(EnumType.STRING)
    private Permission permission;

    @OneToOne(mappedBy = "member")
    private LocalMember localMember;

    @OneToOne(mappedBy = "member")
    private KakaoOauthMember kakaoOauthMember;

    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Order> orders;


    public Member(String email, Permission permission, Type userType) {
        this.email = email;
        this.permission = permission;
        this.userType = userType;
        this.orders = new ArrayList<>();
    }

    protected Member() {
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Type getUserType() {
        return userType;
    }

    public Permission getPermission() {
        return permission;
    }

    public KakaoOauthMember getKakaoOauthMember() {
        return kakaoOauthMember;
    }

    public LocalMember getLocalMember() {
        return localMember;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserType(Type userType) {
        this.userType = userType;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public void setKakaoOauthMember(KakaoOauthMember kakaoOauthMember) {
        this.kakaoOauthMember = kakaoOauthMember;
    }

    public void setLocalMember(LocalMember localMember) {
        this.localMember = localMember;
    }

    @Override
    public String toString() {
        return "Member{" +
            "id=" + id +
            ", email='" + email + '\'' +
            ", userType='" + userType + '\'' +
            ", permission='" + permission + '\'' +
            ", localMember=" + localMember +
            ", kakaoOauthMember=" + kakaoOauthMember +
            '}';
    }
}
