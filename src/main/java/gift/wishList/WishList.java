package gift.wishList;

import gift.option.Option;
import gift.option.OptionReadResponse;
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

    public WishList() {

    }

    public WishList(long id, User user, Option option) {
        this.id = id;
        this.user = user;
        this.option = option;
    }

    public WishList(User user, Option option) {
        this.user = user;
        this.option = option;
    }

    public WishListResponse toWishListReadResponse(){
        return new WishListResponse(id, new OptionReadResponse(option));
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

    public void setKakaouser(KakaoUser kakaouser) {
        this.kakaouser = kakaouser;
    }
}
