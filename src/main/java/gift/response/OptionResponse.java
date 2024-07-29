package gift.response;

import gift.model.Options;

public record OptionResponse(Long id, String name, Integer quantity) {

    private OptionResponse(Options options) {
        this(options.getId(), options.getName(), options.getQuantity());
    }

    public static OptionResponse createOptionResponse(Options options) {
        return new OptionResponse(options);
    }
}
