package gift.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import gift.dto.wish.WishCreateRequest;
import gift.dto.wish.WishResponse;
import gift.service.WishService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class WishControllerTest {

    @Mock
    private WishService wishService;

    @InjectMocks
    private WishController wishController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("위시리스트 조회 (페이지네이션 적용)")
    public void testGetWishlist() {
        WishResponse wishResponse = new WishResponse(1L, 1L, 1L);
        Pageable pageable = PageRequest.of(0, 10);
        Page<WishResponse> page = new PageImpl<>(List.of(wishResponse), pageable, 1);

        when(wishService.getWishlistByMemberId(1L, pageable)).thenReturn(page);

        ResponseEntity<Page<WishResponse>> response = wishController.getWishlist(1L, pageable);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
        assertEquals(1, response.getBody().getContent().size());
    }

    @Test
    @DisplayName("위시리스트 항목 추가")
    public void testAddWish() {
        WishCreateRequest wishCreateRequest = new WishCreateRequest(1L);
        WishResponse wishResponse = new WishResponse(1L, 1L, 1L);

        when(wishService.addWish(any(WishCreateRequest.class), any(Long.class))).thenReturn(
            wishResponse);

        ResponseEntity<WishResponse> response = wishController.addWish(wishCreateRequest, 1L);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().id());
    }

    @Test
    @DisplayName("위시리스트 항목 삭제")
    public void testDeleteWish() {
        ResponseEntity<Void> response = wishController.deleteWish(1L, 1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
