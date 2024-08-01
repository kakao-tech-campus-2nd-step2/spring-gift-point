package gift.service;

import gift.domain.Category;
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

import java.util.Optional;

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

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    public void testAddWish() {
//        Long memberId = 1L;
//        String productName = "Test Product";
//
//        Member member = new Member("test@example.com", "password123");
//        Product product = new Product("Test Product", 1000, "test.jpg", new Category("Test Category"));
//
//        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
//        when(productRepository.findByName(productName)).thenReturn(Optional.of(product));
//        when(wishRepository.existsByMemberAndProduct(member, product)).thenReturn(false);
//        when(wishRepository.save(any(Wish.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        wishService.addWish(memberId, productName);
//
//        verify(wishRepository, times(1)).save(any(Wish.class));
//    }

//    @Test
//    public void testRemoveWish() {
//        Long memberId = 1L;
//        String productName = "Test Product";
//
//        Member member = new Member("test@example.com", "password123");
//        Product product = new Product("Test Product", 1000, "test.jpg", new Category("Test Category"));
//
//        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
//        when(productRepository.findByName(productName)).thenReturn(Optional.of(product));
//
//        doNothing().when(wishRepository).deleteByMemberAndProduct(member, product);
//
//        wishService.removeWish(memberId, productName);
//
//        verify(wishRepository, times(1)).deleteByMemberAndProduct(member, product);
//    }
}
