package gift.domain;

import gift.dto.MemberDto;
import gift.dto.ProductDto;
import gift.dto.WishDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "wishlist")
public class Wish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    protected Wish() {
    }

    public Wish(Member member, Product product) {
        this.member = member;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public MemberDto getMemberDto() {
        return new MemberDto(this.member.getId(), this.member.getEmail());
    }

    public ProductDto getProductDto() {
        return new ProductDto(this.product.getId(), this.product.getName(), this.product.getPrice(),
            this.product.getImageUrl(), this.product.getCategoryDto().getName());
    }

    public WishDto toWishDto() {
        return new WishDto(this.id, this.getMemberDto(), this.getProductDto());
    }
}
