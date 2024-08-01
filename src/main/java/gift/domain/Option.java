package gift.domain;

import jakarta.persistence.*;

@Entity
@Table(name="options")
public class Option extends BaseEntity {
    @Column(name="name", nullable = false)
    private String name;
    @Column(name="quantity", nullable = false)
    private int quantity;
    @ManyToOne
    @JoinColumn(
            name = "product_id",
            foreignKey = @ForeignKey(name = "fk_option_product_id_ref_product_id"),
            nullable = false)
    Product product;
  
    protected Option() {
        super();
    }

    public Option(String name, int quantity, Product product) {
        this.name = name;
        this.quantity = quantity;
        this.product = product;
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

    public void subtract(int minus){
        if(this.quantity - minus < 0) {
            return;
        }
        this.quantity -= minus;
    }
}
