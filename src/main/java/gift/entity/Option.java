package gift.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import gift.dto.option.OptionResponseDTO;
import gift.dto.option.SaveOptionDTO;
import gift.exception.exception.BadRequestException;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Entity
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ManyToOne(fetch = FetchType.LAZY)
    Product product;

    @Pattern(regexp = "^[a-zA-Z0-9()\\[\\]+\\-&/_]+$", message = "특수기호 안됨")
    String name;

    @Min(0)
    int quantity;

    public Product getProduct() {
        return product;
    }

    public Option(Product product, String option) {
        this.product = product;
        product.addOptions(this);
        this.name = option;
        this.quantity = 0;
    }
    public OptionResponseDTO toResponseDTO(){
        return new OptionResponseDTO(this.id, this.name, this.quantity, this.product.id);
    }

    public int getId() {
        return this.id;
    }

    public String getOption() {
        return this.name;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public Option() {
    }

    public Option addQuantity(int num) {
        this.quantity += num;
        return this;
    }

    public void subQuantity(int num) {
        if (this.quantity < num) throw new BadRequestException("재고보다 많은 물건 주문 불가능");
        this.quantity -= num;
    }

    public Option changeOption(SaveOptionDTO saveOptionDTO) {
        this.name = saveOptionDTO.name();
        return this;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}