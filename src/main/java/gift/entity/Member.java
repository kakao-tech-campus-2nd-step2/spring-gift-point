package gift.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "member")
    private List<Wish> wishList = new ArrayList<>();

    @Column
    private String accessToken;

    @Column
    private int point;

    public Member(){};

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
        this.point = 0;
    }

    public Member(String email, String password, String accessToken) {
        this.email = email;
        this.password = password;
        this.accessToken = accessToken;
        this.point = 0;
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

}
