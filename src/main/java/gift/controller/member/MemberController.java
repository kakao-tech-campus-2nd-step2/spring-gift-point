package gift.controller.member;

import gift.config.LoginAdmin;
import gift.config.LoginUser;
import gift.controller.auth.AuthController;
import gift.controller.auth.LoginResponse;
import gift.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Member", description = "Member API")
@RequestMapping("api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    @Operation(summary = "get All Members", description = "모든 멤버 불러오기(기본 개수 : 5개)")
    public ResponseEntity<Page<MemberResponse>> getAllMembers(@LoginAdmin LoginResponse member,
        @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(memberService.findAll(pageable));
    }

    @GetMapping("/{memberId}")
    @Operation(summary = "get Member", description = "memberId를 통해 멤버 정보 불러오기")
    public ResponseEntity<MemberResponse> getMember(@LoginUser LoginResponse member,
        @PathVariable UUID memberId) {
        AuthController.validateUserOrAdmin(member, memberId);
        return ResponseEntity.status(HttpStatus.OK).body(memberService.findById(memberId));
    }

    @PostMapping("/register")
    @Operation(summary = "create Member", description = "멤버 생성(회원가입)")
    public ResponseEntity<MemberResponse> createMember(@Valid @RequestBody SignUpRequest member) {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.save(member));
    }

    @PutMapping("/{memberId}")
    @Operation(summary = "modify Member", description = "멤버 정보 수정")
    public ResponseEntity<MemberResponse> updateMember(@LoginUser LoginResponse loginMember,
        @PathVariable UUID memberId, @RequestBody MemberRequest member) {
        AuthController.validateUserOrAdmin(loginMember, memberId);
        return ResponseEntity.status(HttpStatus.OK).body(memberService.update(memberId, member));
    }

    @DeleteMapping("/{memberId}")
    @Operation(summary = "delete Member", description = "멤버 삭제")
    public ResponseEntity<Void> deleteMember(@LoginUser LoginResponse loginMember,
        @PathVariable UUID memberId) {
        AuthController.validateUserOrAdmin(loginMember, memberId);
        memberService.delete(memberId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}