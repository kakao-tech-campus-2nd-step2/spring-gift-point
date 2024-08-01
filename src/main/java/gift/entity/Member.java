package gift.entity;

import gift.constants.ErrorMessage;
import gift.dto.MemberDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Member extends BaseEntity {

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Wishlist> wishlist = new ArrayList<>();

    protected Member() {
    }

    public Member(MemberDto memberDto) {
        this(memberDto.getEmail(), memberDto.getPassword());
    }

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public List<Wishlist> getWishlist() {
        return wishlist;
    }

    public boolean isCorrectPassword(String password) {
        return this.password.equals(password);
    }

    public void addWishlist(Wishlist wishlist) {
        if (wishlist == null) {
            throw new NullPointerException(ErrorMessage.NULL_POINTER_EXCEPTION_MSG);
        }
        this.wishlist.add(wishlist);
    }

    public boolean containsWish(Long productId) {
        return wishlist.stream().anyMatch(product ->
            product.getId().equals(productId)
        );
    }
}
