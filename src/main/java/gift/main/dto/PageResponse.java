package gift.main.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.data.domain.Page;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PageResponse(CustomPage data) {

    public PageResponse(Page<?> page) {
        this(new CustomPage(page));
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    record CustomPage(int totalPage,
                      List<?> content) {

        CustomPage(Page<?> page) {
            this(page.getTotalPages(), page.getContent());
        }
    }

}
