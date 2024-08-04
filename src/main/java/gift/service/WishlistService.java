package gift.service;

import gift.dto.Request.WishRequestDto;
import gift.dto.Response.WishResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WishlistService {
    void addToWish(String username, WishRequestDto wishRequestDto);
    void removeFromWish(Long wishId);
    Page<WishResponseDto> getWishesByUser(String username, Pageable pageable);
}
