package gift.dto;

public record OrderRequestDTO(Long optionId, Long quantity, String message) {
    @Override
    public String toString() {
        return "{" +
                "\"object_type\":\"text\"," +
                "\"text\":\"" + message + " quantity: " + quantity + "\"," +
                "\"link\":{}" +
                "}";
    }
}
