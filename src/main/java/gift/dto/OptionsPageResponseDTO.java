package gift.dto;

import gift.model.Option;

import java.util.List;

public record OptionsPageResponseDTO(List<Option> optionList,
                                     Integer currentPage,
                                     Integer totalPages) {
}
