package gift.Controller;

import gift.DTO.RequestMemberDTO;
import gift.DTO.ResponseMemberDTO;
import gift.Service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Tag(name = "로그인 및 회원가입", description = "로그인 및 회원가입 API")
@RestController
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;


    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @Operation(summary = "회원가입", description = "회원가입을 진행합니다")
    @ApiResponse(responseCode = "201", description = "회원가입 완료",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
    })
    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. 입력값을 확인하거나 중복된 이메일입니다",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            })
    @ApiResponse(responseCode = "500", description = "서버 내부 에러 발생",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            })
    @PostMapping("/register")
    public ResponseEntity<ResponseMemberDTO> registerUser(@Valid @RequestBody RequestMemberDTO requestMemberDTO){
        String token = memberService.signUpUser(requestMemberDTO);
        return new ResponseEntity<>(new ResponseMemberDTO(token), HttpStatus.CREATED);
    }

    @Operation(summary = "로그인", description = "존재하는 사용자인지 확인 후 토큰을 발급합니다" )
    @ApiResponse(responseCode = "200", description = "로그인 완료",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
    })
    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. 입력값을 확인해주세요",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            })
    @ApiResponse(responseCode = "403", description = "아이디 또는 비밀번호가 틀렸습니다",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            })
    @ApiResponse(responseCode = "500", description = "서버 내부 에러 발생",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            })
    @PostMapping("/login")
    public ResponseEntity<ResponseMemberDTO> loginUser(@Valid @RequestBody RequestMemberDTO member) {
        String token = memberService.loginUser(member);
        return ResponseEntity.ok(new ResponseMemberDTO(token));
    }
}