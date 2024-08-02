package gift.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity(name = "member")
public class MemberEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean delete = false;

    @JsonIgnore
    @OneToMany(mappedBy = "memberEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WishListEntity> wishList;

    public MemberEntity() {
    }

    public List<WishListEntity> getWishList() {
        return wishList;
    }

    public void setWishList(List<WishListEntity> wishList) {
        this.wishList = wishList;
    }

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

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }
}
