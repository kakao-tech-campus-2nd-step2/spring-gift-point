package gift.product.controller;

import static gift.product.exception.GlobalExceptionHandler.UNKNOWN_VALIDATION_ERROR;

import gift.product.docs.MemberControllerDocs;
import gift.product.dto.MemberDTO;
import gift.product.dto.TokenDTO;
import gift.product.service.MemberService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/member")
public class ApiMemberController implements MemberControllerDocs {

    private final MemberService memberService;

    @Autowired
    public ApiMemberController(MemberService memberService) {
        this.memberService = memberService;
    }

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
