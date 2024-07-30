package gift.wish.model;

public record WishRequest(Long productId, Integer count) {

    public boolean isCountZero() {
        return count == 0;
    }
}
