package gift.dto;

import gift.entity.Option;
import gift.entity.Product;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class OptionRequest {

	@NotBlank(message = "옵션 이름은 필수로 입력해야 합니다.")
    @Size(max = 50, message = "옵션 이름은 최대 50자까지 입력 가능합니다.")
	@Pattern(regexp = "^[a-zA-Z0-9가-힣()\\[\\]+\\-&/_ ]*$", message = "허용되지 않는 특수 문자가 들어가 있습니다.")
	private String name;
	
	@Min(value = 1, message = "옵션 수량은 최소 1개 이상이어야 합니다.")
	@Max(value = 99999999, message = "옵션 수량은 1억 개 미만이어야 합니다.")
	private int quantity;
	
	public OptionRequest() {}
	
	public OptionRequest(String name, int quantity) {
		this.name = name;
		this.quantity = quantity;
	}
	
	public String getName() {
		return name;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public Option toEntity(Product product) {
		return new Option(this.name, this.quantity, product);
	}
}
