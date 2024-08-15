package gift.entity;

import java.time.LocalDateTime;

import gift.dto.OrderResponse;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "option_id", nullable = false)
	private Option option;
	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	@Column(nullable = false)
	private int quantity;
	
	@Column(nullable = false)
	private LocalDateTime orderDateTime;
	
	@Column(nullable = false)
	private String message;
	
	@Column(nullable = false)
    private int finalPrice;
	
	public Order(Option option, User user, int quantity, String message) {
		this.option = option;
		this.user = user;
		this.quantity = quantity;
		this.message = message;
		this.orderDateTime = LocalDateTime.now();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public Option getOption() {
		return option;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setOrderDateTime(LocalDateTime orderDateTime) {
		this.orderDateTime = orderDateTime;
	}
	
	public int getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(int finalPrice) {
        this.finalPrice = finalPrice;
    }
	
	public OrderResponse toDto() {
		return new OrderResponse(this.id, this.option.getId(), this.quantity, this.orderDateTime, this.message);
	}
}
