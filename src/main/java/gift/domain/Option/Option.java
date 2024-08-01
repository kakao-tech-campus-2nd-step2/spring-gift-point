package gift.domain.Option;

import gift.domain.Menu.Menu;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private OptionName name;
    @Embedded
    private OptionQuantity quantity;

    @ManyToOne
    Menu menu;

    public Option(Long id, String name, Long quantity,Menu menu) {
        this.id = id;
        this.name = new OptionName(name);
        this.quantity = new OptionQuantity(quantity);
        this.menu = menu;
    }

    public Option() {

    }

    public String getName() {
        return this.name.getoptionName();
    }

    public Long getId() {
        return this.id;
    }

    public Long getQuantity() {
        return quantity.getoptionQuantity();
    }

    public Menu getMenu() {
        return menu;
    }

    public void subtract(Long subtractNumber) throws IllegalAccessException {
        quantity.subtract(subtractNumber);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Option option = (Option) o;
        return Objects.equals(name.getoptionName(), option.name.getoptionName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name.getoptionName());
    }

}
