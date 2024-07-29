package gift.entity;

import gift.dto.user.SignUpDTO;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_tb")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String email;
    private String password;

    @OneToMany(mappedBy = "user")
    List<WishList> wishlist = new ArrayList<>();
    @OneToMany(mappedBy = "user")
    List<Order> orders = new ArrayList<>();

    public User() {
    }

    public User(SignUpDTO signUpDTO) {
        this.email = signUpDTO.email();
        this.password = signUpDTO.password();
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void addWishlist(WishList wishlist) {
        this.wishlist.add(wishlist);
    }

    public void deleteWishlist(WishList wishList) {
        this.wishlist.remove(wishList);
    }

    public void addOrder(Order order) {
        this.orders.add(order);
    }

    public void deleteOrder(Order order) {
        this.orders.remove(order);
    }

}
