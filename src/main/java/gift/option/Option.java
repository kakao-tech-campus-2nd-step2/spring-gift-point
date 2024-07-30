package gift.option;

import gift.product.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int quantity;

    /*@NotNull
    private Long productId;*/

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    protected Option(){

    }

    public Option(Long id, String name, int quantity, Product product){
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.product = product;
    }

    public Option(Long id, String name, int quantity) {
        this.id = id;
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

    public Product getProduct() {
        return product;
    }

    public void update(String name, int quantity){
        this.name = name;
        this.quantity = quantity;
    }

    public boolean subtract(int quantity){
        if(quantity >= this.quantity){
            //throw new IllegalArgumentException("옵션의 수량은 최소 1개 입니다.");
            return false;
        }
        this.quantity -= quantity;
        return true;
    }
}
