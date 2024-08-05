package gift.domain.order;

import gift.domain.BaseTimeEntity;
import gift.domain.member.Member;
import gift.domain.order.dto.response.OrderResponse;
import gift.domain.product.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private String optionName;

    private Long quantity;

    private LocalDateTime date;

    private int price;

    private String imageUrl;

    private Boolean hasCashReceipt;
    private ReceiptType cashReceiptType;
    private String cashReceiptNumber;
    private String message;

    protected Order() {
    }

    public Order(Member member, Product product, String optionName, Long quantity,
        LocalDateTime date,
        int price,
        String imageUrl,
        Boolean hasCashReceipt,
        ReceiptType cashReceiptType,
        String cashReceiptNumber,
        String message
    ) {
        this.member = member;
        this.product = product;
        this.optionName = optionName;
        this.quantity = quantity;
        this.date = date;
        this.price = price;
        this.imageUrl = imageUrl;
        this.hasCashReceipt = hasCashReceipt;
        this.cashReceiptType = cashReceiptType;
        this.cashReceiptNumber = cashReceiptNumber;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public String getOptionName() {
        return optionName;
    }

    public Long getQuantity() {
        return quantity;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Boolean getHasCashReceipt() {
        return hasCashReceipt;
    }

    public ReceiptType getCashReceiptType() {
        return cashReceiptType;
    }

    public String getCashReceiptNumber() {
        return cashReceiptNumber;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Order{" +
               "id=" + id +
               ", product=" + product +
               ", optionName='" + optionName + '\'' +
               ", quantity=" + quantity +
               ", date=" + date +
               ", price=" + price +
               ", imageUrl='" + imageUrl + '\'' +
               ", hasCashReceipt=" + hasCashReceipt +
               ", cashReceiptType='" + cashReceiptType + '\'' +
               ", cashReceiptNumber='" + cashReceiptNumber + '\'' +
               ", message='" + message + '\'' +
               '}';
    }

    public OrderResponse toOrderResponse() {
        return new OrderResponse(
            this.id,
            this.product.getId(),
            this.product.getName(),
            this.optionName,
            this.quantity,
            this.date,
            this.price,
            this.imageUrl
        );
    }
}