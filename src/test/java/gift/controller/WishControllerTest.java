package gift.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.config.JwtProvider;
import gift.domain.member.Member;
import gift.dto.request.WishCreateRequest;
import gift.service.MemberService;
import gift.service.WishService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class WishControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private WishService wishService;

    @MockBean
    private MemberService memberService;

    @MockBean
    private JwtProvider jwtProvider;

    @DisplayName("[GET] 해당 회원의 위시리스트에 존재하는 상품을 모두 조회한다.")
    @Test
    void productList() throws Exception {
        //given
        Long memberId = 1L;
        Claims claims = Jwts.claims().subject(String.valueOf(memberId)).build();

        given(jwtProvider.getClaims(anyString())).willReturn(claims);
        given(jwtProvider.isVerified(anyString())).willReturn(true);
        given(memberService.getMember(anyLong())).willReturn(new Member());

        given(wishService.getProducts(any(Member.class), any(Pageable.class))).willReturn(List.of());

        //when
        ResultActions result = mvc.perform(get("/api/wishes")
                .header("Authorization", "Bearer abc"));

        //then
        result
                .andExpect(status().isOk());

        then(jwtProvider).should().getClaims(anyString());
        then(jwtProvider).should().isVerified(anyString());
        then(memberService).should().getMember(anyLong());

        then(wishService).should().getProducts(any(Member.class), any(Pageable.class));
    }

    @DisplayName("[POST] 해당 회원의 위시리스트에 상품 하나를 추가한다.")
    @Test
    void productAdd() throws Exception {
        //given
        Long memberId = 1L;
        Claims claims = Jwts.claims().subject(String.valueOf(memberId)).build();

        given(jwtProvider.getClaims(anyString())).willReturn(claims);
        given(jwtProvider.isVerified(anyString())).willReturn(true);
        given(memberService.getMember(anyLong())).willReturn(new Member());

        WishCreateRequest request = new WishCreateRequest(1L);

        willDoNothing().given(wishService).addProduct(any(Member.class), anyLong());

        //when
        ResultActions result = mvc.perform(post("/api/wishes")
                .header("Authorization", "Bearer abc")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)));

        //then
        result
                .andExpect(status().isCreated());

        then(jwtProvider).should().getClaims(anyString());
        then(jwtProvider).should().isVerified(anyString());
        then(memberService).should().getMember(anyLong());

        then(wishService).should().addProduct(any(Member.class), anyLong());
    }

    @DisplayName("[DELETE] 해당 회원의 위시리스트에 존재하는 상품 하나를 삭제한다.")
    @Test
    void productRemove() throws Exception {
        //given
        Long memberId = 1L;
        Claims claims = Jwts.claims().subject(String.valueOf(memberId)).build();

        given(jwtProvider.getClaims(anyString())).willReturn(claims);
        given(jwtProvider.isVerified(anyString())).willReturn(true);
        given(memberService.getMember(anyLong())).willReturn(new Member());

        WishCreateRequest request = new WishCreateRequest(1L);

        willDoNothing().given(wishService).removeProduct(any(Member.class), anyLong());

        //when
        ResultActions result = mvc.perform(delete("/api/wishes")
                .header("Authorization", "Bearer abc")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)));

        //then
        result
                .andExpect(status().isOk());

        then(jwtProvider).should().getClaims(anyString());
        then(jwtProvider).should().isVerified(anyString());
        then(memberService).should().getMember(anyLong());

        then(wishService).should().removeProduct(any(Member.class), anyLong());
    }

}
