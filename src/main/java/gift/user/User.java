package gift.user;


import gift.point.PointDTO;
import gift.wishList.WishList;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "USERS")
public class User implements IntegratedUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "password")
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false)
    private UserType userType;
    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "user", orphanRemoval = true)
    private List<WishList> wishLists = new ArrayList<>();
    @Column
    private Long point = 0L;

    public User() {
    }

    public User(String email, String password, UserType userType) {
        this.email = email;
        this.password = password;
        this.userType = userType;
        this.point = 0L;
    }

    public User(String email, String password, UserType userType, Long point) {
        this.email = email;
        this.password = password;
        this.userType = userType;
        this.point = point;
    }

    public void addWishList(WishList wishList) {
        this.wishLists.add(wishList);
        wishList.setUser(this);
    }

    public void removeWishList(WishList wishList) {
        wishList.setUser(null);
        this.wishLists.remove(wishList);
    }

    public void removeWishLists() {
        for (WishList wishList : wishLists) {
            removeWishList(wishList);
        }
    }

    public Long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public List<WishList> getWishLists() {
        return wishLists;
    }

    public Long getPoint() {
        return point;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setPoint(Long point) {
        this.point = point;
    }

    public void chargePoint(Long point){
        this.point += point;
    }
}
