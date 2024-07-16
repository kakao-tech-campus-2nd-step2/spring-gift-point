package gift.entity;

import jakarta.persistence.*;

@Entity
public class Wish {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

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

    // 추가: member의 id를 반환하는 메서드
    public Long getMemberId() {
        return member != null ? member.getId() : null;
    }

    // 추가: product의 id를 반환하는 메서드
    public Long getProductId() {
        return product != null ? product.getId() : null;
    }

    // 아래 id를 반환하는 메서드를 쓴 이유: 객체 지향적으로는 참조한 객체 자체를 반환하는게 맞지만,
        // 외래 키 값 자체가 필요하게 될 수도 있을 것 같아서.
}
