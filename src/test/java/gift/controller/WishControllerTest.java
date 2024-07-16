package gift.controller;

import gift.entity.Member;
import gift.entity.Product;
import gift.entity.Wish;
import gift.repository.MemberRepository;
import gift.service.WishService;
import gift.util.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WishController.class)
public class WishControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WishService wishService;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("위시리스트 전체 조회")
    public void getWishes() throws Exception {
        Member member = new Member();
        member.setId(1L);
        member.setEmail("test@example.com");
        member.setPassword("password");

        Product product = new Product();
        product.setId(1L);
        product.setName("Product1");
        product.setPrice(100);
        product.setImageUrl("http://example.com/image.jpg");

        Wish wish = new Wish();
        wish.setId(1L);
        wish.setMember(member);
        wish.setProduct(product);

        given(jwtUtil.extractEmail(anyString())).willReturn("test@example.com");
        given(memberRepository.findByEmail(anyString())).willReturn(Optional.of(member));
        given(wishService.findByMember(any(Member.class))).willReturn(Arrays.asList(wish));

        mockMvc.perform(get("/wishes")
                        .header("Authorization", "Bearer fake-jwt-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    @DisplayName("위시리스트 추가")
    public void addWish() throws Exception {
        Member member = new Member();
        member.setId(1L);
        member.setEmail("test@example.com");
        member.setPassword("password");

        Product product = new Product();
        product.setId(1L);
        product.setName("Product1");
        product.setPrice(100);
        product.setImageUrl("http://example.com/image.jpg");

        Wish wish = new Wish();
        wish.setId(1L);
        wish.setMember(member);
        wish.setProduct(product);

        given(jwtUtil.extractEmail(anyString())).willReturn("test@example.com");
        given(memberRepository.findByEmail(anyString())).willReturn(Optional.of(member));
        given(wishService.save(any(Wish.class))).willReturn(wish);

        mockMvc.perform(post("/wishes")
                        .header("Authorization", "Bearer fake-jwt-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("위시리스트 삭제")
    public void deleteWish() throws Exception {
        Member member = new Member();
        member.setId(1L);
        member.setEmail("test@example.com");
        member.setPassword("password");

        given(jwtUtil.extractEmail(anyString())).willReturn("test@example.com");
        given(memberRepository.findByEmail(anyString())).willReturn(Optional.of(member));

        mockMvc.perform(delete("/wishes/1")
                        .header("Authorization", "Bearer fake-jwt-token"))
                .andExpect(status().isOk())
                .andExpect(content().string("Wish deleted successfully"));
    }
}
