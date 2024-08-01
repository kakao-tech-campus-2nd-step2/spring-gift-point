package gift.domain.member;

import gift.domain.wish.Wish;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Entity
@DynamicInsert
public class Member{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @ColumnDefault("'user'")
    private String role;

    @OneToMany(mappedBy = "member")
    private List<Wish> wishList;

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
    }

    protected Member() {

    }

    public void updateMember(String email, String password) {
        this.email = email;
        this.password = password;
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

    public String getRole() {
        return role;
    }

    public List<Wish> getWishList() {
        return wishList;
    }

    public void addWish(Wish wish) {
        wishList.add(wish);
        wish.setMember(this);
    }
}
