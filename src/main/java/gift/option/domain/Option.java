package gift.option.domain;

import gift.option.exception.OptionNotEnoughException;
import gift.product.domain.Product;
import jakarta.persistence.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity
@Table(name = "options")
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private OptionName name;
    @Embedded
    private OptionQuantity quantity;

    @ManyToOne(fetch = FetchType.EAGER)
    private Product product;

    // JDBC 에서 엔티티 클래스를 인스턴스화할 때 반드시 기본 생성자와 파라미터 생성자가 필요하다
    public Option() {
    }

    public Option(Long id, OptionName name, OptionQuantity quantity, Product product) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public OptionName getName() {
        return name;
    }

    public OptionQuantity getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }

    public boolean checkNew() {
        return id == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        Option item = (Option) o;
        return Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void subtract(Long quantity) {
        Long stock = this.quantity.getOptionCountValue();
        if (stock < quantity) {
            throw new OptionNotEnoughException();
        }
        this.quantity = new OptionQuantity(stock - quantity);
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
