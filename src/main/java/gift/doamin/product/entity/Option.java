package gift.doamin.product.entity;

import gift.doamin.product.dto.OptionForm;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Product product;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer quantity;

    protected Option() {
    }

    public Option(Product product, String name, Integer quantity) {
        this.product = product;
        this.name = name;
        this.quantity = quantity;
    }

    public Option(String name, Integer quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public String getName() {
        return name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void update(OptionForm optionForm) {
        this.name = optionForm.getName();
        this.quantity = optionForm.getQuantity();
    }

    public void subtract(int quantity) {
        if (this.quantity < quantity) {
            throw new IllegalArgumentException("차감할 상품 옵션의 수량이 부족합니다.");
        }
        this.quantity -= quantity;
    }
}
