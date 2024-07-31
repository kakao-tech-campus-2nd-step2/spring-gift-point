package gift.product.domain;

import gift.core.exception.product.NotFoundMoreStockException;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Option {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private Long quantity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product;

	protected Option() {
	}

	public Option(String name, Long quantity,Product product) {
		this.name = name;
		this.quantity = quantity;
		this.product = product;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public static Option create(String name, Long quantity,Product product) {
		return new Option(name, quantity,product);
	}

	public void addStock(int quantity) {
		this.quantity += quantity;
	}

	public void removeStock(Long quantity) {
		Long restStock = this.quantity - quantity;
		if (restStock < 0) {
			throw new NotFoundMoreStockException();
		}
		this.quantity = restStock;
	}
}
