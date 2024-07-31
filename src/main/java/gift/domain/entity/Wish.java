package gift.domain.entity;

import gift.domain.dto.request.WishRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

//TODO: 상품에 대해 위시를 정하지 말고 상품 옵션에 대해 위시 수량을 정한뒤, 그 수량을 감소시킬 필요가 있음
@Entity
public class Wish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Member member;

    @Column(nullable = false)
    private Long quantity;

    public Wish(Product product, Member member, Long quantity) {
        this.product = product;
        this.member = member;
        this.quantity = quantity;
    }

    protected Wish() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public void set(WishRequest request) {
        this.quantity = request.quantity();
    }

    @Override
    public String toString() {
        return "Wish{" +
            "id=" + id +
            ", product=" + product +
            ", member=" + member +
            ", quantity=" + quantity +
            '}';
    }
}
