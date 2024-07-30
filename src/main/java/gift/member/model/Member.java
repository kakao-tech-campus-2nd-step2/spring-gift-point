package gift.member.model;

import gift.wish.model.Wish;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// 1. 연관관계 : Member(1) : Wish(N)
// 2. 부모 엔티티 : Member
// 3. 주인 텐티티 : Wish (Wish 엔티티가 member 필드에 @JoinColumn을 붙여 외래키를 관리)


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

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Wish> wishList = new ArrayList<>();

    // 활용 메서드들
    public void addWish(Wish wish) {
        this.wishList.add(wish);
        wish.setMember(this);
    }

    public void removeWish(Wish wish) {
        wish.setMember(null);
        this.wishList.remove(wish);
    }

    public void clearWishList() {
        Iterator<Wish> iterator = wishList.iterator();

        while (iterator.hasNext()) {
            Wish wish = iterator.next();

            wish.setMember(null);
            iterator.remove();
        }
    }

    // Constructors, Getters, and Setters
    public Member() {}

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters and setters
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

    public List<Wish> getWishList() {
        return wishList;
    }

    public void setWishList(List<Wish> wishList) {
        this.wishList = wishList;
    }
}
