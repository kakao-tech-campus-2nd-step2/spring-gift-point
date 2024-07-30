package gift.domain.order.entity;

import gift.domain.product.entity.Option;
import gift.domain.product.entity.Product;
import gift.exception.InvalidOptionInfoException;
import gift.exception.InvalidOrderException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id", nullable = false)
    private Option option;

    @Column(nullable = false)
    private int quantity;

    private static final int OPTION_QUANTITY_MIN = 0;
    private static final int OPTION_QUANTITY_MAX = 100000000;

    @PrePersist
    public void prePersist() {
        validateProduct();
        validateOption();
        validateQuantity();
    }

    @PreUpdate
    public void preUpdate() {
        validateProduct();
        validateOption();
        validateQuantity();
    }

    protected OrderItem() {
    }

    public OrderItem(Long id, Order order, Product product, Option option, int quantity) {
        this.id = id;
        this.order = order;
        this.product = product;
        this.option = option;
        this.quantity = quantity;
    }

    private void validateProduct() {
        if (product == null) {
            throw new InvalidOrderException("error.invalid.order.product");
        }
    }

    private void validateOption() {
        if ((option == null) || (!product.hasOption(option.getId()))) {
            throw new InvalidOrderException("error.invalid.order.option");
        }
    }

    private void validateQuantity() {
        if (quantity < OPTION_QUANTITY_MIN || quantity > OPTION_QUANTITY_MAX) {
            throw new InvalidOptionInfoException("error.invalid.option.quantity");
        }
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Long getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public Product getProduct() {
        return product;
    }

    public Option getOption() {
        return option;
    }

    public int getQuantity() {
        return quantity;
    }
}
