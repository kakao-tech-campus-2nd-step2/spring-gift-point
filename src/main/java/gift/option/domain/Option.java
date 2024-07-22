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
    private OptionCount count;

    @ManyToOne(fetch = FetchType.EAGER)
    private Product product;

    // JDBC 에서 엔티티 클래스를 인스턴스화할 때 반드시 기본 생성자와 파라미터 생성자가 필요하다
    public Option() {
    }

    public Option(Long id, OptionName name, OptionCount count, Product product) {
        this.id = id;
        this.name = name;
        this.count = count;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public OptionName getName() {
        return name;
    }

    public OptionCount getCount() {
        return count;
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

    public void subtract(int quantity) {
        Long stock = this.count.getOptionCountValue();
        if (stock < quantity) {
            throw new OptionNotEnoughException();
        }
        this.count = new OptionCount(stock - quantity);
    }
}
