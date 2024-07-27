package gift.wish.model;

import gift.member.model.Member;
import gift.product.model.Product;
import jakarta.persistence.*;

@Entity
@Table(name = "wish")
public class Wish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    // 지연 로딩의 이유? 연관된 엔티티를 실제로 사용하기 전까지, 즉 영속성 컨텍스트가 활성화되기 전까지는 로딩을 지연시킨다. 이를 통해 성능 최적화와 메모리 사용량을 줄일 수 있따.
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // 활용 메서드들
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Wish)) return false;
        return id != null && id.equals(((Wish) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }


    // Constructors, Getters, and Setters
    public Wish() {}

    public Wish(Member member, Product product) {
        this.member = member;
        this.product = product;
    }

    // Getters and setters
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
}
