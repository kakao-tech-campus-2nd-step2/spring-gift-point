package gift.model.entity;

import gift.exception.ErrorCode;
import gift.exception.customException.CustomOutOfStockException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

@Entity
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column
    private String name;
    @NotNull
    @Column
    private Long quantity;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    protected Option() {
    }

    public Option(Long id, String name, Long quantity, Item item) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.item = item;
    }

    public Option(String name, Long quantity, Item item) {
        this(null, name, quantity, item);
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

    public Item getItem() {
        return item;
    }

    public void update(String name, Long quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public void decreaseQuantity(Long quantity) throws CustomOutOfStockException {
        if (this.quantity - quantity < 0) {
            throw new CustomOutOfStockException(ErrorCode.QUANTITY_EXCEEDS_AVAILABLE_STOCK);
        }
        this.quantity -= quantity;
    }
}