package gift.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;

import org.springframework.http.HttpStatus;

import gift.exception.CustomException;

@Entity
@Table(name="member")
public class Member {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;
    
    private int point;

    private String role;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<WishList> wishList;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Order> orders;
    
    protected Member() {

    }

    public Member(String password, String email, String role) {
        this.password = password;
        this.email = email;
        this.role = role;
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

    public int getPoint() {
        return point;
    }

    public String getRole(){
        return role;
    }

    public List<WishList> getWishList(){
        return wishList;
    }

    public List<Order> getOrders(){
        return orders;
    }

    public void subtractPoint(int pointAmount){
        if(this.point < pointAmount){
            throw new CustomException("point amount is larger than remainer", HttpStatus.BAD_REQUEST, -40001);
        }else{
            this.point -= pointAmount;
        }
    }

    public void chargePoint(int pointAmount){
        this.point += pointAmount;
    }
    
}