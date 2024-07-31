package gift.entity;

import gift.dto.ProductResponse;
import gift.exception.InvalidProductException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
  
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private int price;

	@Column(nullable = false, length = 255)
	private String imageUrl;
	
	@ManyToOne
	@JoinColumn(name = "category_id", nullable = false)
	private Category category;
	
	public Product() {}
	
	public Product(String name, int price, String imageUrl, Category category) {
		this.setName(name);
		this.setPrice(price);
		this.imageUrl = imageUrl;
		this.category = category;
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
		if (price < 0) {
			throw new InvalidProductException("Price cannot be less than ZERO.");
		}
		this.price = price;
	}
	
	public String getImageUrl() {
		return imageUrl;
	}
	
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	public Category getCategory() {
		return category;
	}
	
	public void setCategory(Category category) {
		this.category = category;
	}
	
	public ProductResponse toDto() {
		return new ProductResponse(this.id, this.name, this.price, this.imageUrl, this.category.getName());
	}
}
