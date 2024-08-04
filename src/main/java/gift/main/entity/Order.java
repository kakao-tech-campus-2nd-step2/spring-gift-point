package gift.main.entity;

import gift.main.dto.OrderRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Min(1)
    @Max(100000000)
    private int quantity;

    private String message;

    private int totalPrice;

    private int usingPoint;

    private int payPrice;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User buyer;

    @ManyToOne
    @JoinColumn(name = "option_id")
    private Option option;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public Order() {
    }


    //buyer, option, purchasedProduct)
    public Order(OrderRequest orderRequest, User buyer, Option option, Product product) {
        this.quantity = orderRequest.quantity();
        this.message = orderRequest.message();
        this.totalPrice = product.getPrice() * orderRequest.quantity();
        this.usingPoint = orderRequest.usingPoint();
        this.payPrice = totalPrice - usingPoint;
        this.buyer = buyer;
        this.option = option;
        this.product = product;

    }

    public long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getMessage() {
        return message;
    }

    public User getBuyer() {
        return buyer;
    }

    public Option getOption() {
        return option;
    }

    public Product getProduct() {
        return product;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getUsingPoint() {
        return usingPoint;
    }

    public int getPayPrice() {
        return payPrice;
    }
}
