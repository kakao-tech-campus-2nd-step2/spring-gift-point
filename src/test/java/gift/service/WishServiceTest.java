package gift.service;

import gift.domain.Member;
import gift.domain.Product;
import gift.domain.Wish;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class WishServiceTest {

    @Mock
    private WishRepository wishRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private WishService wishService;

    private Member member;
    private Product product;
    private Wish wish;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        member = new Member("test@example.com", "password");
        product = new Product("Product Name", 100, "image-url", null);
        wish = new Wish(member, product);
    }

    @Test
    public void testAddWish() {
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(wishRepository.existsByMemberAndProduct(any(Member.class), any(Product.class))).thenReturn(false);

        wishService.addWish(member.getEmail(), 1L);

        verify(wishRepository, times(1)).save(any(Wish.class));
    }

    @Test
    public void testRemoveWish() {
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        wishService.removeWish(member.getEmail(), 1L);

        verify(wishRepository, times(1)).deleteByMemberAndProduct(any(Member.class), any(Product.class));
    }
}
