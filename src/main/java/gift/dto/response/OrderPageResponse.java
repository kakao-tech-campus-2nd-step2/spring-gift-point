package gift.dto.response;

import gift.domain.Order;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class OrderPageResponse {

    private List<OrderResponse> content;
    private int number;
    private long totalElements;
    private int size;
    private boolean last;

    public OrderPageResponse(List<OrderResponse> content, int number, long totalElements, int size, boolean last) {
        this.content = content;
        this.number = number;
        this.totalElements = totalElements;
        this.size = size;
        this.last = last;
    }

    public List<OrderResponse> getContent() {
        return content;
    }

    public int getNumber() {
        return number;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public int getSize() {
        return size;
    }

    public boolean isLast() {
        return last;
    }

    public static OrderPageResponse fromOrderPage(Page<Order> orderPage) {
        List<OrderResponse> content = orderPage.getContent().stream()
                .map(OrderResponse::fromOrder)
                .collect(Collectors.toList());

        return new OrderPageResponse(
                content,
                orderPage.getNumber(),
                orderPage.getTotalElements(),
                orderPage.getSize(),
                orderPage.isLast()
        );
    }
}
