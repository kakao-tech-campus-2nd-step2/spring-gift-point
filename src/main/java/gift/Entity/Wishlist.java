package gift.Entity;

import jakarta.persistence.*;

@Entity
public class Wishlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "memberId", referencedColumnName = "id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "productId", referencedColumnName = "id")
    private Product product;

    private String productName;
    private int quantity;
    private int price;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "optionId", referencedColumnName = "id")
    private Option option;

    protected Wishlist() {
    }

    public Wishlist(long id, Member member, Product product, String productName, int quantity, int price, Option option) {
        this.id = id;
        this.member = member;
        this.product = product;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.option = option;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setOption(Option option) {
        this.option = option;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getCount() {
        return quantity;
    }

    public void setCount(int count) {
        this.quantity = count;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Option getOption() {
        return option;
    }
}
