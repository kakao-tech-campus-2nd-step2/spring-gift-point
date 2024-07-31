package gift.dto;

public class OptionResponse {

	private Long id;
	private String name;
	private int quantity;
	
	public OptionResponse(Long id, String name, int quantity) {
		this.id = id;
		this.name = name;
		this.quantity = quantity;
	}

	public String getName() {
		return name;
	}
}
