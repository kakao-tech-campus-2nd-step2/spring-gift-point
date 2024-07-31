package gift.entity;

import gift.dto.ProductInfoDto;
import gift.dto.WishlistResponseDto;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "wish")
public class Wish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private LocalDateTime createdDate;


    public Wish() {
        this.createdDate = LocalDateTime.now();
    }

    public Wish(Member member, Product product) {
        this.member = member;
        this.product = product;
        this.createdDate = LocalDateTime.now();
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

    public WishlistResponseDto toDto() {
        ProductInfoDto productInfoDto = new ProductInfoDto(
            this.product.getId(),
            this.product.getName(),
            this.product.getPrice(),
            this.product.getImageUrl()
        );
        return new WishlistResponseDto(this.id, productInfoDto);
    }
}
