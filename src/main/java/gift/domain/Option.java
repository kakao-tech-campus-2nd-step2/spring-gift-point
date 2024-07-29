package gift.domain;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    Long quantity;

    @ManyToOne
    Menu menu;

    public Option(Long id, String name, Long quantity,Menu menu) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.menu = menu;
    }

    public Option() {

    }

    public String getName() {
        return this.name;
    }

    public Long getId() {
        return this.id;
    }

    public Long getQuantity() {
        return quantity;
    }

    public Menu getMenu() {
        return menu;
    }

    public void subtract(Long subtractNumber) throws IllegalAccessException {
        quantity -= subtractNumber;
        if(quantity <= 0 || quantity > 1_000_000_000){
            throw new IllegalAccessException("옵션의 수량은 1이상이거나, 1억 이하여야 합니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Option option = (Option) o;
        return Objects.equals(name, option.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}
