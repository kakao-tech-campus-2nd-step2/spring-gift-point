package gift.domain;

import jakarta.persistence.*;

@Entity
@Table(name="options")
public class Option extends BaseEntity {
    @Column(name="name", nullable = false)
    private String name;
    @Column(name="quantity", nullable = false)
    private int quantity;
  
    protected Option() {
        super();
    }

    public Option(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
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
