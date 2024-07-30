package gift.order.dto;

import java.util.Map;

public record TemplateObject(
        String object_type,
        String text,
        Map<String, String> link,
        String button_title
) {}
