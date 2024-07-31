package gift.dto;

import jakarta.validation.constraints.NotBlank;

public class CategoryUpdateRequest {

	@NotBlank(message = "선택할 수 있는 카테고리 이름을 입력해야 합니다.")
	private String categoryName;
	
	public String getCategoryName() {
		return categoryName;
	}
	
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
	public CategoryUpdateRequest(String categoryName) {
		this.categoryName = categoryName;
	}
}
