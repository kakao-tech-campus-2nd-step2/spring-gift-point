package gift.domain;

import jakarta.persistence.*;

@Entity
@Table(name="options")
public class Option extends BaseEntity {
    @Column(name="name", nullable = false)
    private String name;
    @Column(name="quantity", nullable = false)
    private Long quantity;
  
    protected Option() {
        super();
    }

    public Option(String name, Long quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void update(String name, Long quantity){
        this.name = name;
        this.quantity = quantity;
    }

    public void subtract(Long minus){
        if(this.quantity - minus < 0L) {
            return;
        }
        this.quantity -= minus;
    }
}
