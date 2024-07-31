package gift.service.dto;

import gift.controller.dto.request.OptionRequest;

import java.util.List;

public record CreateProductDto(
        String name,
        int price,
        String imageUrl,
        Long categoryId,
        List<OptionRequest.CreateOption> options
) {
}

