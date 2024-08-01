package gift.web.dto.response.productoption;

import java.util.List;

public class ReadAllProductOptionsResponse {

    List<ReadProductOptionResponse> options;

    public ReadAllProductOptionsResponse(List<ReadProductOptionResponse> options) {
        this.options = options;
    }

    public static ReadAllProductOptionsResponse from(List<ReadProductOptionResponse> options) {
        return new ReadAllProductOptionsResponse(options);
    }

    public List<ReadProductOptionResponse> getOptions() {
        return options;
    }
}
