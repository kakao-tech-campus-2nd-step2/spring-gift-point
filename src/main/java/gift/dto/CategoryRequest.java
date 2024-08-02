package gift.dto;

import gift.entity.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoryRequest {

	@NotBlank(message = "이름은 필수 입력입니다.")
    @Size(max = 255, message = "이름은 최대 255자까지 입력 가능합니다.")
    private String name;

    @NotBlank(message = "색상은 필수 입력입니다.")
    private String color;

    @NotBlank(message = "이미지 URL은 필수 입력입니다.")
    private String imageUrl;

    private String description;
    
    public CategoryRequest() {}
    
    public CategoryRequest(String name, String color, String imageUrl, String description) {
		this.name = name;
		this.color = color;
		this.imageUrl = imageUrl;
		this.description = description;
	}

	public String getName() {
    	return name;
    }
    
    public String getColor() {
    	return color;
    }
    
    public String getImageUrl() {
    	return imageUrl;
    }
    
    public String getDescription() {
    	return description;
    }
    
    public void setName(String name) {
    	this.name = name;
    }
    
    public void setColor(String color) {
    	this.color = color;
    }
    
    public void setImageUrl(String imageUrl) {
    	this.imageUrl = imageUrl;
    }
    
    public void setDescription(String description) {
    	this.description = description;
    }
    
    public Category toEntity() {
        return new Category(name, color, imageUrl, description);
    }
}
