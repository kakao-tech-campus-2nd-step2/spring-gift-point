package gift.dto;

import jakarta.validation.constraints.NotNull;

public class CategoryUpdateRequest {

	@NotNull(message = "선택할 수 있는 카테고리 번호를 입력해야 합니다.")
	private Long categoryId;
	
	public Long getCategoryId() {
		return categoryId;
	}
	
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	
	public CategoryUpdateRequest(Long categoryId) {
		this.categoryId = categoryId;
	}
}
