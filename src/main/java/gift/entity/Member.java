package gift.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    public Member() {}

    @ConstructorProperties({"email", "password"})
    public Member(String email, String password) {
        this.email = email;
        this.password = password;
        this.type = MemberType.NORMAL_USER;
    }

    public Member(String email, String password, MemberType type) {
        this.email = email;
        this.password = password;
        this.type = type;
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
}
