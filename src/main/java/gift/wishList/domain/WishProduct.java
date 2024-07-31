package gift.wishList.domain;


import gift.product.domain.Product;
import gift.user.domain.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class WishProduct {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product;

	protected WishProduct() {
	}

	public WishProduct( User user, Product product) {
		this.user = user;
		this.product = product;
	}
	public static WishProduct of(User user, Product product) {
		return new WishProduct(user, product);
	}

	public Long getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	public Product getProduct() {
		return product;
	}

}



