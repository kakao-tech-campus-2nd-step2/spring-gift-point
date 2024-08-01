package gift.dto.order;

import jakarta.validation.constraints.NotNull;

public record OrderRequestDTO(@NotNull Long optionId,
                              @NotNull Long quantity,
                              @NotNull String message) {
    @Override
    public String toString() {
        return "{" +
                "\"object_type\":\"text\"," +
                "\"text\":\"" + message + " quantity: " + quantity + "\"," +
                "\"link\":{}" +
                "}";
    }
}
