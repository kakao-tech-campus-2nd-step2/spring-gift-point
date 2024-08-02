package gift.dto.order;

import org.springframework.data.domain.Page;

import java.util.List;

public class OrderPageDTO {
    private List<OrderResponseDTO> content;
    private int number;
    private int totalElements;
    private int size;
    private boolean last;

    public OrderPageDTO(Page<OrderResponseDTO> orders) {
        this.content = orders.getContent();
        this.number = orders.getNumber();
        this.totalElements = (int) orders.getTotalElements();
        this.size = orders.getSize();
        this.last = orders.isLast();
    }

}
