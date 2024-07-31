package gift.dto;

import gift.entity.Product;
import gift.exception.InvalidProductException;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ProductUpdateRequest {
	
	@NotBlank(message = "이름은 필수로 입력해야 합니다.")
	@Size(max = 15, message = "이름은 최대 15자까지 입력 가능합니다.")
	@Pattern(regexp = "^[a-zA-Z0-9가-힣()\\[\\]+\\-&/_ ]*$", message = "허용되지 않는 특수 문자가 들어가 있습니다.")
	private String name;
	
	@Min(value = 0, message = "음수를 입력할 수 없습니다.")
	private int price;
	
	@Size(max = 255, message = "url의 길이는 최대 255자까지 입니다.")
	private String imageUrl;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		if (name.contains("카카오")) {
			throw new InvalidProductException("이름에 '카카오'를 포함할 수 없습니다.");
		}
		this.name = name;
	}
	
	public int getPrice() {
		return price;
	}
	
	public void setPrice(int price) {
		this.price = price;
	}
	
	public String getImageUrl() {
		return imageUrl;
	}
	
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	public ProductUpdateRequest(String name, int price, String imageUrl) {
		this.setName(name);
		this.price = price;
		this.imageUrl = imageUrl;
	}
	
	public void updateEntity(Product product) {
		product.setName(this.name);
		product.setPrice(this.price);
		product.setImageUrl(this.imageUrl);
	}
}
