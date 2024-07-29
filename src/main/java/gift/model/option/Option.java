package gift.model.option;

import gift.model.gift.Gift;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "gift_option")
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotNull
    private String name;

    @NotNull
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gift_id")
    private Gift gift;

    @Version
    @ColumnDefault("0")
    private Integer version;

    protected Option() {
    }

    public Option(Long id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public Option(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
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

    public Gift getGift() {
        return gift;
    }

    public void setGift(Gift gift) {
        this.gift = gift;
    }

    public void modify(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public void modify(String name) {
        this.name = name;
    }

    public void modify(int quantity) {
        this.quantity = quantity;
    }

    public void subtract(int quantity) {
        if (this.quantity < quantity) {
            throw new IllegalArgumentException("차감할 수량이 현재 수량보다 많습니다.");
        }
        this.quantity -= quantity;
    }
}
