package gift.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
@Table(name = "option", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "product_id"})})
public class OptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(max = 50, message = "옵션 이름은 공백을 포함하여 최대 50자까지 입력할 수 있습니다.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣 ()\\[\\]+\\-\\&/_]*$", message = "(),[],+,-,&,/,_를 제외한 특수 문자 사용은 불가능합니다.")
    private String name;

    @Column(nullable = false)
    @Min(value = 1, message = "옵션 수량은 최소 1개 이상이어야 합니다.")
    @Max(value = 99999999, message = "옵션 수량은 1억 개 미만이어야 합니다.")
    private Long quantity;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @OneToMany(mappedBy = "option", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderEntity> orders;

    public OptionEntity() {}

    public OptionEntity(String name, Long quantity, ProductEntity product) {
        this.name = name;
        this.quantity = quantity;
        this.product = product;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getQuantity() {
        return quantity;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
