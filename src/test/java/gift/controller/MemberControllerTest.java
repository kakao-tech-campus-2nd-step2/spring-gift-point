package gift.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.constants.ErrorMessage;
import gift.dto.MemberDto;
import gift.dto.OptionSaveRequest;
import gift.dto.ProductRequest;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Sql("/sql/truncateIdentity.sql")
class MemberControllerTest {

    private @Autowired MockMvc mockMvc;

    private final String member = """
        {"email": "sgoh@naver.com", "password": "sgohpass"}
        """;
    private final String category = """ 
        {"name": "음식", "color": "Red", "imageUrl": "http", "description": "description"}
        """;

    void addCategory(String category) throws Exception {
        mockMvc.perform(post("/api/categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(category));
    }

    void registerMember(String member) throws Exception {
        mockMvc.perform(post("/api/members/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(member));
    }

    void addProduct() throws Exception {
        ProductRequest request = new ProductRequest(null, "커피", 4500L, "https", 1L, "음식",
            List.of(new OptionSaveRequest("선물", 30, null)));

        String product = new ObjectMapper().writeValueAsString(request);
        mockMvc.perform(post("/api/products/product")
            .contentType(MediaType.APPLICATION_JSON)
            .content(product));
    }

    String loginAndGetToken(String member) throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/api/members/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(member)).andReturn();
        return mvcResult.getResponse().getHeader("token");
    }

    @Test
    @DisplayName("회원가입 테스트")
    void registerMember() throws Exception {
        mockMvc.perform(post("/api/members/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(member))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("로그인 테스트")
    void login() throws Exception {
        registerMember(member);

        mockMvc.perform(post("/api/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(member))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("위시 리스트 목록 테스트")
    void wishlist() throws Exception {
        registerMember(member);
        String token = loginAndGetToken(member);

        mockMvc.perform(get("/api/members/wishlist")
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("위시 리스트 추가 테스트")
    void addWishlist() throws Exception {
        addCategory(category);
        registerMember(member);
        addProduct();
        String token = loginAndGetToken(member);

        mockMvc.perform(post("/api/members/wishlist/1")
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("위시 리스트 삭제 테스트")
    void deleteWishlist() throws Exception {
        addCategory(category);
        registerMember(member);
        addProduct();
        String token = loginAndGetToken(member);

        mockMvc.perform(post("/api/members/wishlist/1")
            .header("Authorization", "Bearer " + token));
        mockMvc.perform(delete("/api/members/wishlist/1")
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("이메일 형식 패턴 검증 테스트")
    void invalidEmailFormat() throws Exception {
        MemberDto memberDto = new MemberDto(null, "sgoh", "sgoh");
        String inputJson = new ObjectMapper().writeValueAsString(memberDto);

        mockMvc.perform(post("/api/members/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(ErrorMessage.MEMBER_NOT_EMAIL_FORMAT_MSG));
    }

    @Test
    @DisplayName("이메일 Not Blank 검증 테스트")
    void blankEmail() throws Exception {
        MemberDto memberDto = new MemberDto(null, "", "sgoh");
        String inputJson = new ObjectMapper().writeValueAsString(memberDto);

        mockMvc.perform(post("/api/members/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(ErrorMessage.MEMBER_EMAIL_NOT_BLANK_MSG));
    }

    @Test
    @DisplayName("비밀번호 패턴 검증 테스트")
    void invalidPasswordPattern() throws Exception {
        MemberDto memberDto = new MemberDto(null, "sgoh@naver.com", "비밀번호");
        String inputJson = new ObjectMapper().writeValueAsString(memberDto);

        mockMvc.perform(post("/api/members/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(ErrorMessage.MEMBER_PASSWORD_INVALID_PATTERN_MSG));
    }

    @Test
    @DisplayName("비밀번호 길이 검증 테스트")
    void passwordLength() throws Exception {
        MemberDto memberDto = new MemberDto(null, "sgoh@naver.com", "0123456789012345");
        String inputJson = new ObjectMapper().writeValueAsString(memberDto);

        mockMvc.perform(post("/api/members/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(ErrorMessage.MEMBER_PASSWORD_INVALID_LENGTH_MSG));
    }

    @Test
    @DisplayName("비밀번호 null 검증 테스트")
    void blankPassword() throws Exception {
        MemberDto memberDto = new MemberDto(null, "sgoh@naver.com", null);
        String inputJson = new ObjectMapper().writeValueAsString(memberDto);

        mockMvc.perform(post("/api/members/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(ErrorMessage.MEMBER_PASSWORD_NOT_BLANK_MSG));
    }
}