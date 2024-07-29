package gift.administrator.option;

import gift.administrator.product.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "옵션 DTO")
public class OptionDTO {

    private Long id;
    @Schema(description = "옵션 이름", example = "XL")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣ㄱ-ㅎ ()\\[\\]+\\-&/_]*$",
        message = "'( ), [ ], +, -, &, /, _'외의 특수문자는 사용할 수 없습니다.")
    @Size(max = 50, message = "공백을 포함하여 50자 이내의 옵션이름을 입력해주세요.")
    @NotBlank(message = "옵션 이름을 입력하지 않았습니다.")
    private String name;
    @Schema(description = "옵션 수량", example = "5")
    @Min(value = 1, message = "옵션 수량은 최소 1개 이상입니다.")
    @Digits(integer = 8, fraction = 0, message = "옵션 수량은 1억 개 미만입니다.")
    private int quantity;
    @Schema(description = "옵션이 포함된 상품 아이디", example = "1")
    private Long productId;

    public OptionDTO(){}

    public OptionDTO(Long id, String name, int quantity, Long productId) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.productId = productId;
    }

    public OptionDTO(String name, int quantity, Long productId) {
        this.name = name;
        this.quantity = quantity;
        this.productId = productId;
    }

    public Long getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public Long getProductId(){
        return productId;
    }

    public Option toOption(Product product){
        return new Option(id, name, quantity, product);
    }

    public static OptionDTO fromOption(Option option) {
        return new OptionDTO(option.getId(), option.getName(), option.getQuantity(),
            option.getProduct().getId());
    }
}
