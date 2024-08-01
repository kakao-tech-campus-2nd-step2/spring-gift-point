package gift.entity;

import gift.domain.OptionDTO;
import jakarta.persistence.*;

@Entity
@Table(name = "option")
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Version
    private int version;

    protected Option() {}

    public Option(OptionDTO optionDTO) {
        this.name = optionDTO.name();
        this.quantity = optionDTO.quantity();
    }

    public Option(int id, OptionDTO optionDTO) {
        this.id = id;
        this.name = optionDTO.name();
        this.quantity = optionDTO.quantity();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setId(int id) {
        this.id = id;
    }
}
