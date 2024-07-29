package gift.option;

import gift.product.Product;
import gift.wishList.WishList;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "OPTIONS")
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "quantity")
    private Long quantity;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "option", orphanRemoval = true)
    private List<WishList> wishLists = new ArrayList<>();

    public Option() {
    }

    public Option(OptionRequest optionRequest, Product product) {
        this.name = optionRequest.getOptionName();
        this.quantity = optionRequest.getQuantity();
        this.product = product;
    }
    public Option(String name, Long quantity, Product product) {
        this.name = name;
        this.quantity = quantity;
        this.product = product;
    }

    public void addWishList(WishList wishList) {
        this.wishLists.add(wishList);
        wishList.setOption(this);
    }

    public void removeWishList(WishList wishList) {
        wishList.setOption(null);
        this.wishLists.remove(wishList);
    }

    public void removeWishLists() {
        for (WishList wishList : wishLists) {
            removeWishList(wishList);
        }
    }

    public void updateOption(OptionRequest optionRequest){
        this.name = optionRequest.getOptionName();
        this.quantity = optionRequest.getQuantity();
    }

    public void subtractQuantity(Long quantity){
        this.quantity -= quantity;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
