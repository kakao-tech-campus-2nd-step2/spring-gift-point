package gift.domain.option;

import gift.domain.product.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.Objects;

@Entity
@Table(name = "option", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name", "product_id"})
})
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private Long quantity;

    @ManyToOne(fetch = FetchType.LAZY) // 옵션은 해당 상품에서 직접 추가됨
    @JoinColumn(name = "product_id")
    private Product product;

    public Option() {
    }

    public Option(String name, Long quantity, Product product) {
        this.name = name;
        this.quantity = quantity;
        this.product = product;

        this.product.getOptions().add(this); // 양방향 연관관계 편의 메서드
    }

    public Option(Long id, String name, Long quantity, Product product) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.product = product;
    }

    public Product getProduct() {
        return product;
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

    public void update(String name, Long quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Option{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", quantity=" + quantity +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Option option = (Option) o;
        return Objects.equals(getId(), option.getId()) && Objects.equals(getName(),
            option.getName()) && Objects.equals(getQuantity(), option.getQuantity())
               && Objects.equals(getProduct(), option.getProduct());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getQuantity(), getProduct());
    }

    public void decrease(Long quantity) {
        this.quantity -= quantity;
    }
}
