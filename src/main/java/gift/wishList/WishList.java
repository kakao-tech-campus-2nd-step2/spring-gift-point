package gift.wishList;

import gift.option.Option;
import gift.product.Product;
import gift.user.KakaoUser;
import gift.user.User;
import jakarta.persistence.*;

@Entity
@Table(name = "WISHLISTS")
public class WishList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kakao_user_id")
    private KakaoUser kakaouser;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id")
    private Option option;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
    @Column(name = "count")
    private long count;

    public WishList(long count) {
        this.count = count;
    }


    public WishList() {

    }

    public WishList(long id, User user, Option option, long count) {
        this.id = id;
        this.user = user;
        this.option = option;
        this.count = count;
    }

    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Option getOption() {
        return option;
    }

    public Product getProduct() {
        return product;
    }

    public long getCount() {
        return count;
    }

    public KakaoUser getKakaouser() {
        return kakaouser;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setOption(Option option) {
        this.option = option;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public void setKakaouser(KakaoUser kakaouser) {
        this.kakaouser = kakaouser;
    }
}
