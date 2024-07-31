package gift.service;

import gift.dto.WishRequest;
import gift.entity.Member;
import gift.entity.Option;
import gift.entity.Product;
import gift.entity.Wish;
import gift.repository.MemberRepository;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class WishServiceTest {

    @Mock
    private WishRepository wishRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OptionRepository optionRepository;

    @InjectMocks
    private WishService wishService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("위시리스트 페이징 조회 테스트")
    void getWishesPaged() {
        Pageable pageable = PageRequest.of(0, 5);
        Wish wish = mock(Wish.class);
        Page<Wish> wishPage = new PageImpl<>(Collections.singletonList(wish));
        when(wishRepository.findAll(pageable)).thenReturn(wishPage);

        Page<Wish> result = wishService.getWishes(pageable);

        assertEquals(1, result.getTotalElements());
        verify(wishRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("이메일로 위시리스트 조회 테스트")
    void getWishesByEmail() {
        Member member = mock(Member.class);
        Wish wish = mock(Wish.class);
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));
        when(wishRepository.findByMember(member)).thenReturn(Collections.singletonList(wish));

        List<Wish> result = wishService.getWishes("test@example.com");

        assertEquals(1, result.size());
        verify(memberRepository, times(1)).findByEmail(anyString());
        verify(wishRepository, times(1)).findByMember(member);
    }

    @Test
    @DisplayName("위시 추가 테스트")
    void addWish() {
        Member member = mock(Member.class);
        Product product = mock(Product.class);
        Option option = mock(Option.class);
        WishRequest wishRequest = new WishRequest(1L, 1L);

        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(optionRepository.findById(anyLong())).thenReturn(Optional.of(option));
        when(wishRepository.existsByMemberAndProductAndOption(member, product, option)).thenReturn(false);
        when(wishRepository.save(any(Wish.class))).thenAnswer(invocation -> {
            Wish wish = invocation.getArgument(0);
            Wish spyWish = spy(wish);
            doReturn(1L).when(spyWish).getId(); // ID를 직접 설정하지 않고 스파이 객체로 ID를 설정
            return spyWish;
        });

        Wish result = wishService.addWish("test@example.com", wishRequest);

        assertEquals(1L, result.getId());
        verify(memberRepository, times(1)).findByEmail(anyString());
        verify(productRepository, times(1)).findById(anyLong());
        verify(optionRepository, times(1)).findById(anyLong());
        verify(wishRepository, times(1)).existsByMemberAndProductAndOption(member, product, option);
        verify(wishRepository, times(1)).save(any(Wish.class));
    }

    @Test
    @DisplayName("위시 제거 테스트")
    void removeWish() {
        Member member = mock(Member.class);
        Product product = mock(Product.class);

        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        wishService.removeWish("test@example.com", 1L);

        verify(memberRepository, times(1)).findByEmail(anyString());
        verify(productRepository, times(1)).findById(anyLong());
        verify(wishRepository, times(1)).deleteByMemberAndProduct(member, product);
    }
}