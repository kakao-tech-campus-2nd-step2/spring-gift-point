package gift.entity;

import org.springframework.http.HttpStatus;

import gift.dto.CategoryResponse;
import gift.exception.InvalidUserException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "category")
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true, nullable = false, length = 255)
	private String name;
	
	@Column(nullable = false)
	private String color;
	
	@Column(nullable = false)
	private String imageUrl;
	
	private String description;
	
	public Category() {}
	
	public Category(String name, String color, String imageUrl, String description) {
		this.name = name;
		this.color = color;
		this.imageUrl = imageUrl;
		this.description = description;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
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
	
	public void validateIdMatch(Long categoryId) {
		if (!this.id.equals(categoryId)) {
			throw new InvalidUserException("The email doesn't or thr password is incorrect.", HttpStatus.FORBIDDEN);
		}
	}
	
	public CategoryResponse toDto() {
        return new CategoryResponse(id, name, color, imageUrl, description);
    }
}
