package gift.model.entity;

import gift.model.valueObject.OptionName;
import gift.model.valueObject.Quantity;
import jakarta.persistence.*;

@Entity
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "quantity", columnDefinition = "integer", nullable = false)
    private long quantity;

    @Column(name = "name", columnDefinition = "varchar(255)", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "fk_option_product"), nullable = false)
    private Product product;

    protected Option() {
    }

    public Option(String name, long quantity, Product product){
        this.updateName(name);
        this.updateQuantity(quantity);
        this.product = product;
    }

    public Option(Long id, String name, long quantity, Product product){
        this.id = id;
        this.updateName(name);
        this.updateQuantity(quantity);
        this.product = product;
    }


    public void updateName(String name){
        OptionName optionName = new OptionName(name);
        this.name = optionName.getName();
    }
    public Option update(String name){
        this.updateName(name);
        return this;
    }
    public void updateQuantity(long quantity){
        Quantity OptionQuantity = new Quantity(quantity);
        this.quantity = OptionQuantity.getQuantity();
    }

    public Option quantityUpdate(int num){
        this.updateQuantity(this.quantity + num);
        return this;
    }

    public long getId() {
        return id;
    }

    public long getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }

    public Long getProductID() {
        return product.getId();
    }

    @Override
    public String toString() {
        return "Option{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", name='" + name + '\'' +
                ", product=" + product +
                '}';
    }
}
