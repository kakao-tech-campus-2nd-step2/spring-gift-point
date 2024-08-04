package gift.product.controller.auth;

import gift.product.dto.auth.AccessTokenDto;
import gift.product.dto.auth.LoginMemberIdDto;
import gift.product.dto.auth.MemberDto;
import gift.product.dto.auth.PointRequest;
import gift.product.model.Member;
import gift.product.service.AuthService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Hidden
@Controller
@RequestMapping("/admin")
public class AdminAuthController {

    public static final String ACCESS_TOKEN = "accessToken";
    private static final String REDIRECT_ADMIN_LOGIN_PROCESSING = "redirect:/admin/login/process";
    private static final String REDIRECT_ADMIN_MEMBERS = "redirect:/admin/members";
    private final AuthService authService;

    public AdminAuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String loginForm() {
        return "admin/loginForm";
    }

    @PostMapping("/login")
    public String login(MemberDto memberDto, RedirectAttributes redirectAttributes) {
        AccessTokenDto accessTokenDto = authService.login(memberDto);
        redirectAttributes.addAttribute(ACCESS_TOKEN, accessTokenDto.accessToken());

        return REDIRECT_ADMIN_LOGIN_PROCESSING;
    }

    @GetMapping("/login/process")
    public String loginProcess(@RequestParam(ACCESS_TOKEN) String accessToken,
        Model model) {
        model.addAttribute(ACCESS_TOKEN, accessToken);

        return "admin/loginProcess";
    }

    @PostMapping("login/process")
    @ResponseBody
    public ResponseEntity<Void> loginSuccess(@RequestParam(ACCESS_TOKEN) String accessToken,
        HttpServletResponse response) {
        addAccessTokenCookieInResponse(accessToken, response);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/admin/products"));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping("/members")
    public String members(Model model, HttpServletRequest request) throws AccessDeniedException {
        LoginMemberIdDto loginMemberIdDto = getLoginMember(request);
        List<Member> members = authService.getMemberAll(loginMemberIdDto);
        model.addAttribute("members", members);

        return "admin/members";
    }

    @GetMapping("/members/add/{id}")
    public String addMemberPointForm(@PathVariable(name = "id") Long memberId, Model model) {
        model.addAttribute("memberId", memberId);
        return "admin/addMemberPointForm";
    }

    @PutMapping("/members/add/{id}")
    public String addMemberPointForm(@PathVariable(name = "id") Long memberId,
        @Valid PointRequest pointRequest)
        throws AccessDeniedException {
        authService.addMemberPoint(pointRequest, new LoginMemberIdDto(memberId));
        return REDIRECT_ADMIN_MEMBERS;
    }

    @GetMapping("/members/subtract/{id}")
    public String subtractMemberPointForm(@PathVariable(name = "id") Long memberId, Model model) {
        model.addAttribute("memberId", memberId);
        return "admin/subtractMemberPointForm";
    }

    @PutMapping("/members/subtract/{id}")
    public String subtractMemberPointForm(@PathVariable(name = "id") Long memberId,
        @Valid PointRequest pointRequest) {
        authService.subtractMemberPoint(pointRequest, new LoginMemberIdDto(memberId));
        return REDIRECT_ADMIN_MEMBERS;
    }

    private void addAccessTokenCookieInResponse(String accessToken,
        HttpServletResponse response) {
        Cookie cookie = new Cookie(ACCESS_TOKEN, accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/admin");
        response.addCookie(cookie);
    }

    private LoginMemberIdDto getLoginMember(HttpServletRequest request) {
        return new LoginMemberIdDto((Long) request.getAttribute("id"));
    }
}
