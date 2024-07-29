package gift.product.controller;

import static gift.product.exception.GlobalExceptionHandler.UNKNOWN_VALIDATION_ERROR;

import gift.product.dto.MemberDTO;
import gift.product.dto.TokenDTO;
import gift.product.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Member", description = "API 컨트롤러")
@RestController
@RequestMapping("/api/member")
public class ApiMemberController {

    private final MemberService memberService;

    @Autowired
    public ApiMemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @Operation(
        summary = "회원 가입",
        description = "회원 정보를 애플리케이션에 등록합니다."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "회원 가입 성공",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = TokenDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "이메일 형식이 올바르지 않거나 비밀번호가 4자 이상 입력되지 않음"
        ),
        @ApiResponse(
            responseCode = "409",
            description = "중복된 이메일에 대한 회원 가입 시도"
        )
    })
    @PostMapping
    public ResponseEntity<TokenDTO> signUp(
            @Valid @RequestBody MemberDTO memberDTO,
            BindingResult bindingResult) {
        System.out.println("[ApiMemberController] signUp()");
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null)
                throw new ValidationException(fieldError.getDefaultMessage());
            throw new ValidationException(UNKNOWN_VALIDATION_ERROR);
        }
        return ResponseEntity.status(HttpStatus.OK)
            .body(memberService.signUp(memberDTO));
    }

    @Operation(
        summary = "로그인",
        description = "애플리케이션의 기능을 이용하기 위한 권한을 취득합니다."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "로그인 성공",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = TokenDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "등록된 회원 정보가 없음"
        )
    })
    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(
            @Valid @RequestBody MemberDTO memberDTO,
            BindingResult bindingResult) {
        System.out.println("[ApiMemberController] login()");
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null)
                throw new ValidationException(fieldError.getDefaultMessage());
            throw new ValidationException(UNKNOWN_VALIDATION_ERROR);
        }
        return ResponseEntity.status(HttpStatus.OK)
            .body(memberService.login(memberDTO));
    }
}
