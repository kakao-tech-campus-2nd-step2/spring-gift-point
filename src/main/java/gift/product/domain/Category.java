package gift.product.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String description;
	private String color;
	private String imageUrl;

	protected Category() { }

	public Category(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getColor() {
		return color;
	}

	public String getImageUrl() {
		return imageUrl;
	}
}
