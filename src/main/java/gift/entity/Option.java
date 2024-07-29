package gift.entity;

import gift.exception.DuplicateOptionNameException;
import gift.exception.MinimumOptionException;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Option entity representing a option")
@Entity
@Table(name = "options")
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Option ID", example = "1")
    private Long id;
    @NotNull
    @Schema(description = "Option name", example = "[Best] 시어버터 핸드 & 시어 스틱 립 밤")
    private String name;
    @NotNull
    @Schema(description = "Option quantity", example = "969")
    private Integer quantity;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    protected Option() {

    }

    public Option(String name, Integer quantity, Product product) {
        this.name = name;
        this.quantity = quantity;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void updateOption(String name, Integer quantity, Product product) {
        this.name = name;
        this.quantity = quantity;
        this.product = product;
    }

    public void subtractQuantity(Integer quantity) {
        if (this.quantity - quantity < 0) {
            throw new MinimumOptionException("옵션에 해당하는 수량이 0개 미만이 될 수 없습니다.");
        }

        this.quantity -= quantity;
    }

    public boolean sameName(String name) {
        return this.name.equals(name);
    }

    @PrePersist
    @PreUpdate
    private void checkDuplicateOptionName() {
        boolean duplicate = product.sortAndBringOptions().stream()
            .anyMatch(option -> option.sameName(this.name) && !option.equals(this));
        if (duplicate) {
            throw new DuplicateOptionNameException("상품에 이미 동일한 옵션 이름이 존재합니다: " + this.name);
        }
    }

}
