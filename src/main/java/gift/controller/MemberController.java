package gift.controller;

import gift.constants.SuccessMessage;
import gift.dto.MemberDto;
import gift.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    /**
     * 회원가입 <br> 이미 존재하는 email이면 IllegalArgumentException
     *
     * @return ResponseEntity<String>
     */
    @PostMapping("/register")
    public ResponseEntity<String> registerMember(@RequestBody @Valid MemberDto member) {
        memberService.registerMember(member);
        return ResponseEntity.ok(SuccessMessage.REGISTER_MEMBER_SUCCESS_MSG);
    }

    /**
     * 로그인 기능.
     *
     * @return 성공 시, 200 OK 응답과 jwt 토큰을 함께 반환
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid MemberDto member) {
        String token = memberService.login(member);

        return ResponseEntity.ok().header("token", token)
            .body(SuccessMessage.LOGIN_MEMBER_SUCCESS_MSG);
    }

    @PostMapping("/wishlist/{productId}")
    public ResponseEntity<String> addWishlist(@PathVariable("productId") Long productId,
        HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        memberService.addWishlist(email, productId);

        return ResponseEntity.ok(SuccessMessage.ADD_WISHLIST_SUCCESS_MSG);
    }

    @DeleteMapping("/wishlist/{productId}")
    public ResponseEntity<String> deleteWishlist(@PathVariable("productId") Long productId,
        HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        memberService.deleteWishlist(email, productId);

        return ResponseEntity.ok(SuccessMessage.DELETE_WISHLIST_SUCCESS_MSG);
    }
}
