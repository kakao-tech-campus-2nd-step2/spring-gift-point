package gift.dto;

import java.util.List;

public class OrderRequest {
    private List<OrderItemRequest> items;
    private String recipientMessage;

    public OrderRequest() {
    }

    public OrderRequest(List<OrderItemRequest> items, String recipientMessage) {
        this.items = items;
        this.recipientMessage = recipientMessage;
    }

    public List<OrderItemRequest> getItems() {
        return items;
    }

    public void setItems(List<OrderItemRequest> items) {
        this.items = items;
    }

    public String getRecipientMessage() {
        return recipientMessage;
    }

    public void setRecipientMessage(String recipientMessage) {
        this.recipientMessage = recipientMessage;
    }

}
