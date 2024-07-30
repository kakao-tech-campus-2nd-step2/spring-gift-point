package gift.main.entity;

import gift.main.Exception.CustomException;
import gift.main.Exception.ErrorCode;
import gift.main.dto.OptionChangeQuantityRequest;
import gift.main.dto.OptionRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.util.Objects;

@Entity
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 20)
    private String optionName;

    @Min(1)
    @Max(100000000)
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public Option() {
    }

    public Option(String optionName, int quantity, Product product) {
        this.optionName = optionName;
        this.quantity = quantity;
        this.product = product;
    }

    public Option(OptionRequest optionRequest, Product product) {
        this.optionName = optionRequest.name();
        this.quantity = optionRequest.quantity();
        this.product = product;
    }

    public long getId() {
        return id;
    }

    public String getOptionName() {
        return optionName;
    }


    public Product getProduct() {
        return product;
    }

    public void isDuplicate(long optionId, OptionRequest optionRequest) {
        if (this.id == optionId) {
            return;
        }

        if (Objects.equals(this.optionName, optionRequest.name())) {
            throw new CustomException(ErrorCode.ALREADY_EXISTS_OPTION_NAME);
        }
    }

    public void isDuplicate(OptionRequest optionRequest) {
        if (Objects.equals(this.optionName, optionRequest.name())) {
            throw new CustomException(ErrorCode.ALREADY_EXISTS_OPTION_NAME);
        }
    }

    public void updateValue(OptionRequest optionRequest) {
        this.optionName = optionRequest.name();
        this.quantity = optionRequest.quantity();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Option option = (Option) o;
        return id == option.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void sellOption(int quantity) {
        if (this.quantity - quantity < 0) {
            throw new CustomException(ErrorCode.INVALID_OPTION_QUANTITY);
        }
        this.quantity -= quantity;
    }
}
