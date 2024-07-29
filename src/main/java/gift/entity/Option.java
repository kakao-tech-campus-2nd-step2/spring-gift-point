package gift.entity;

import org.springframework.http.HttpStatus;

import gift.exception.CustomException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "option")
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false, unique = true)
    private String name;
    private int quantity;

    public Option(){}
    
    public Option(Product product, String name, int quantity) {
        this.product = product;
        this.name = name;
        this.quantity = quantity;
    }
    
    public Long getId() {
        return id;
    }

    public Product getProduct(){
        return product;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public Option substract(int substractQuantity){
        if(this.quantity <= substractQuantity){
            throw new CustomException("substract quantity is too big", HttpStatus.BAD_REQUEST);
        }
        return new Option(this.product, this.name, this.quantity - substractQuantity);
    }
}
