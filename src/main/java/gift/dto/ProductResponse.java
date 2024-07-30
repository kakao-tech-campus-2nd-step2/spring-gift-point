package gift.dto;

public class ProductResponse {

	private Long id;
	private String name;
	private int price;
	private String imageUrl;
	private String categoryName;
	
	public ProductResponse(Long id, String name, int price, String imageUrl, String categoryName) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.imageUrl = imageUrl;
		this.categoryName = categoryName;
	}
	
	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getPrice() {
		return price;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public String getCategoryName() {
		return categoryName;
	}
}
