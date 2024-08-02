package gift.entity;

import gift.dto.user.SignUpDTO;
import gift.exception.exception.BadRequestException;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

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
    @Min(0)
    int point;

    @OneToMany(mappedBy = "user")
    List<WishList> wishlist = new ArrayList<>();
    @OneToMany(mappedBy = "user")
    List<Order> orders = new ArrayList<>();


    public User() {
    }

    public User(SignUpDTO signUpDTO) {
        this.email = signUpDTO.email();
        this.password = signUpDTO.password();
        this.point = 0;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.point = 0;
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

    public int getPoint() {
        return point;
    }

    public void addWishlist(WishList wishlist) {
        this.wishlist.add(wishlist);
    }

    public void deleteWishlist(WishList wishList) {
        this.wishlist.remove(wishList);
    }

    public void addOrderAndPoint(Order order) {
        this.orders.add(order);
        this.point += (int) (order.getPrice() * 0.1);
    }

    public void deleteOrder(Order order) {
        this.orders.remove(order);
    }

    public void subPoint(int point) {
        if (this.point < point) throw new BadRequestException("포인트 부족");
        this.point -= point;
    }
}
