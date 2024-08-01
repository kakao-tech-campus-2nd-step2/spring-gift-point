package gift.dto;

import java.util.Map;
import java.util.HashMap;

public record TemplateObjectDTO(
    String object_type,
    String text,
    Map<String, String> link
) {
    public TemplateObjectDTO(Long id, Long optionId, Long quantity, String orderDateTime, String message) {
        this(
            "text",
            String.format("주문 정보:\n주문 ID: %d\n옵션 ID: %d\n수량: %d\n주문 시간: %s\n메시지: %s",
                id, optionId, quantity, orderDateTime, message),
            createLink()
        );
    }

    private static Map<String, String> createLink() {
        Map<String, String> link = new HashMap<>();
        link.put("web_url", "http://localhost:8080/admin/products");
        link.put("mobile_web_url", "http://localhost:8080/admin/products");
        return link;
    }
}
