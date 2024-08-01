package gift.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Wishlist", uniqueConstraints = { @UniqueConstraint(columnNames = { "member_id", "product_id" })})
public class Wishlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "count_product", nullable = false)
    private int countProduct;

    protected Wishlist() {}

    public Wishlist(Member member, Product product, int countProduct) {
        this.member = member;
        this.product = product;
        this.countProduct = countProduct;
    }

    public int getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Product getProduct() {
        return product;
    }

    public int getCountProduct() {
        return countProduct;
    }

    public void setCountProduct(int countProduct) {
        this.countProduct = countProduct;
    }


}
