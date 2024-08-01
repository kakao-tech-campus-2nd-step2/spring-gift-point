package gift.Model.Entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Wish {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Product product;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    protected Wish(){}

    public Wish(Member member, Product product, LocalDateTime createdDate) {
        validateCreatedDate(createdDate);

        this.member = member;
        this.product = product;
        this.createdDate = createdDate;
    }

    private void validateCreatedDate(LocalDateTime createdDate) {
        if (createdDate== null)
            throw new IllegalArgumentException("찜에서 createTime이 null일 수는 없습니다");
    }


    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Product getProduct() {
        return product;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
