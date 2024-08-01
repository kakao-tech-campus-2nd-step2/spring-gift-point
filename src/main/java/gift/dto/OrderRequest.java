package gift.dto;

public class OrderRequest {
  private final long optionId;
  private final int quantity;
  private final String message;

  public OrderRequest(long optionId, int quantity, String message) {
    this.optionId = optionId;
    this.quantity = quantity;
    this.message = message;
  }

  public long getOptionId() {
    return optionId;
  }

  public int getQuantity() {
    return quantity;
  }

  public String getMessage() {
    return message;
  }
}