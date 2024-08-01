package gift.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class OrderResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long optionId;
    private int quantity;

    @DateTimeFormat
    private Date date;
    private String message;

    public OrderResponse(
        Long optionId,
        int quantity,
        String message
    ) {
        this.message = message;
        this.quantity = quantity;
        this.optionId = optionId;
        this.date = new Date();
    }
}
