package gift.entity;

import gift.constants.ErrorMessage;
import gift.dto.MemberDto;
import gift.exception.NegativePointException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.ColumnDefault;

@Entity
public class Member extends BaseEntity {

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Wishlist> wishlist = new ArrayList<>();

    @ColumnDefault(value = "0")
    @Column(name = "point", nullable = false)
    private long point;

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

    public long getPoint() {
        return point;
    }

    public void addPoint(long amount) {
        if (amount < 0) {
            throw new NegativePointException(ErrorMessage.NEGATIVE_POINT_MSG);
        }
        point += amount;
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
