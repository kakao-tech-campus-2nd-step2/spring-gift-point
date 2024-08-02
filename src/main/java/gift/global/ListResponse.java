package gift.global;

import java.util.List;

public record ListResponse<T>(
    List<T> contents
) {

    public static <T> ListResponse<T> of(List<T> contents) {
        return new ListResponse<>(contents);
    }
}
