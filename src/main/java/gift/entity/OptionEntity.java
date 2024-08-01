package gift.entity;

import jakarta.persistence.*;

@Entity
@Table(name="options")
public class OptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    public OptionEntity() {}

    public OptionEntity(String name, int quantity, ProductEntity product) {
        this.name = name;
        this.quantity = quantity;
        this.product = product;
    }

    public OptionEntity(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public OptionEntity(Long id, String name, int quantity, ProductEntity product) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public void update(String name, int quantity) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("옵션 이름이 적합하지 않습니다.");
        }

        if (name.length() > 50) {
            throw new IllegalArgumentException("옵션명은 50자를 초과할 수 없습니다.");
        }

        if(quantity < 1 || quantity > 100_000_000) {
            throw new IllegalArgumentException("수량은 1개 이상 1억개 미만이어야 합니다.");
        }
        this.name = name;
        this.quantity = quantity;
    }

    public void subtractQuantity(int amountToSubtract) {
        if(amountToSubtract < 0) {
            throw new IllegalArgumentException("수량은 음수일 수 없습니다.");
        }

        if(this.quantity < amountToSubtract) {
            throw new IllegalArgumentException("해당 옵션의 재고가 사용하려는 수량보다 부족합니다.");
        }
        this.quantity -= amountToSubtract;
    }
}