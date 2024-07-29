package gift.model;

import jakarta.persistence.*;

@Entity
@Table(name = "options")
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false) // 최대 옵션 수량을 저장하는 필드 추가
    private int maxQuantity;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public Option() {
    }

    public Option(String name, int quantity, int price, Product product, int maxQuantity) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.product = product;
        this.maxQuantity = maxQuantity; // 최대 옵션 수량 초기화
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

    public int getPrice() {
        return price;
    }

    public Product getProduct() {
        return product;
    }

    public int getMaxQuantity() {
        return maxQuantity;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setMaxQuantity(int maxQuantity) {
        this.maxQuantity = maxQuantity;
    }
}
