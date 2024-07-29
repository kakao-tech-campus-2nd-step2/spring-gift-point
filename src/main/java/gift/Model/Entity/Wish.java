package gift.Model.Entity;

import gift.Model.Value.Count;
import jakarta.persistence.*;

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

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "count"))
    private Count count;

    protected Wish(){}

    public Wish(Member member, Product product, Count count) {
        this.member = member;
        this.product = product;
        this.count = count;
    }

    public Wish(Member member, Product product, int count) {
        Count countObj = new Count(count);

        this.member = member;
        this.product = product;
        this.count = countObj;
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

    public Count getCount() {
        return count;
    }

    public void update(Count count){
        this.count = count;
    }

    public void update(int count){
        this.count = new Count(count);
    }
}
