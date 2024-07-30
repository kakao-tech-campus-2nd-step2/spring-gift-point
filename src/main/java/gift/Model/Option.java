package gift.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.beans.ConstructorProperties;

@Entity
@Table(name = "option")
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonBackReference
    private Product product;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "quantity")
    private int quantity;

    protected Option(){

    }

    @ConstructorProperties({"id","product","name","quantity"})
    public Option(Long id, Product product, String name, int quantity) {
        this.id = id;
        this.product = product;
        this.name = name;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
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

    public int subtract(int count) {
        if (count <= 0 ){
            throw new IllegalArgumentException("0이하는 뺼수 없습니다.");
        }
        if(quantity < count){
            throw new IllegalArgumentException("빼려는 수보다 옵션의 수량이 작습니다.");
        }
        if(quantity == count ){
            return 0; // option의 quantity가 0이되면 validation에 걸리므로 0을 리턴 후 service에서 삭제
        }
        return quantity -= count;
    }
}
