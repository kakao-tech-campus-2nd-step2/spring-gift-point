package gift.test.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;

import gift.dto.WishlistRequest;
import gift.dto.WishlistResponse;
import gift.entity.Category;
import gift.entity.Product;
import gift.entity.User;
import gift.entity.Wishlist;
import gift.repository.ProductRepository;
import gift.repository.WishlistRepository;
import gift.service.UserService;
import gift.service.WishlistService;

public class WishlistServiceTest {

    @Mock
    private WishlistRepository wishlistRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserService userService;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private WishlistService wishlistService;

    private User user;
    private Product product;
    private Wishlist wishlist;
    private Category category;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User("test@test.com", "pw");
        user.setId(1L);
        
        category = new Category("교환권", "#6c95d1", "https://example.com/image.jpg", "");
        product = new Product("아이스 아메리카노 T", 4500, "https://example.com/image.jpg", category);
        product.setId(1L);

        wishlist = new Wishlist(user, product);
        wishlist.setId(1L);
    }

    @Test
    public void testGetWishlist() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Wishlist> page = new PageImpl<>(Collections.singletonList(wishlist), pageable, 1);

        when(userService.getUserFromToken(anyString())).thenReturn(user);
        when(wishlistRepository.findByUserId(anyLong(), any(Pageable.class))).thenReturn(page);

        Page<WishlistResponse> response = wishlistService.getWishlist("token", pageable);

        verify(userService).getUserFromToken(anyString());
        verify(wishlistRepository).findByUserId(anyLong(), any(Pageable.class));
        assertThat(response).isNotNull();
        assertThat(response.getContent()).hasSize(1);
        assertThat(response.getContent().get(0).getProductName()).isEqualTo(product.getName());
    }

    @Test
    public void testAddWishlist() {
        when(userService.getUserFromToken(anyString())).thenReturn(user);
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(wishlistRepository.save(any(Wishlist.class))).thenReturn(wishlist);

        WishlistRequest request = new WishlistRequest(product.getId(), 1);
        wishlistService.addWishlist("token", request, bindingResult);
        
        verify(userService).getUserFromToken(anyString());
        verify(productRepository).findById(anyLong());
        verify(wishlistRepository).save(any(Wishlist.class));
    }

    @Test
    public void testRemoveWishlist() {
        when(userService.getUserFromToken(anyString())).thenReturn(user);
        when(wishlistRepository.findById(anyLong())).thenReturn(Optional.of(wishlist));
        doNothing().when(wishlistRepository).delete(any(Wishlist.class));

        WishlistRequest request = new WishlistRequest(product.getId(), 1);
        wishlistService.removeWishlist("token", request, bindingResult);
        
        verify(userService).getUserFromToken(anyString());
        verify(wishlistRepository).findById(anyLong());
        verify(wishlistRepository).delete(any(Wishlist.class));
    }

    @Test
    public void testUpdateWishlistQuantity() {
        when(userService.getUserFromToken(anyString())).thenReturn(user);
        when(wishlistRepository.findById(anyLong())).thenReturn(Optional.of(wishlist));
        when(wishlistRepository.save(any(Wishlist.class))).thenReturn(wishlist);

        WishlistRequest request = new WishlistRequest(product.getId(), 2);
        wishlistService.updateWishlistQuantity("token", request, bindingResult);
        
        verify(userService).getUserFromToken(anyString());
        verify(wishlistRepository).findById(anyLong());
        verify(wishlistRepository).save(any(Wishlist.class));
    }
}
