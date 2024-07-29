package gift.controller;


import static gift.util.ResponseEntityUtil.responseError;

import gift.dto.betweenClient.JwtDTO;
import gift.dto.betweenClient.ResponseDTO;
import gift.dto.betweenClient.member.MemberDTO;
import gift.exception.BadRequestExceptions.UserNotFoundException;
import gift.service.MemberService;
import gift.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@PropertySource("classpath:application-secret.properties")
@PropertySource("classpath:application-kakao-login.properties")
@Validated
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    @Value("${kakao-rest-api-key}")
    private String clientId;

    @Value("${kakao-redirect-uri}")
    private String redirectUri;

    @Autowired
    public MemberController(MemberService memberService, JwtUtil jwtUtil) {
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
    }


    @GetMapping("/register")
    @Operation(description = "서버가 클라이언트에게 회원 가입 페이지를 제공합니다.", tags = "Member")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 회원가입 페이지를 제공합니다.",
                    content = @Content(mediaType = "text/html", schema = @Schema(type = "String"))),
            @ApiResponse(responseCode = "500", description = "서버에 의한 오류입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class)))})
    public String register(){
        return "register";
    }



    @PostMapping("/register")
    @Operation(description = "서버가 클라이언트가 제출한 사용자 정보를 가지고 회원가입을 진행합니다.", tags = "Member")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원가입에 성공하였습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = JwtDTO.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. 보통 요청의 양식이 잘못된 경우입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "이미 존재하는 사용자입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "서버에 의한 오류입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class)))})
    public ResponseEntity<?> register(@RequestBody @Valid MemberDTO memberDTO) {
        memberDTO.setAccountType("basic");
        memberService.register(memberDTO);
        String token = jwtUtil.generateToken(memberDTO);
        return new ResponseEntity<>(new JwtDTO(token), HttpStatus.CREATED);
    }



    @GetMapping("/login")
    @Operation(description = "서버가 클라이언트에게 로그인 페이지를 제공합니다.", tags = "Member")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 로그인 페이지를 제공합니다.",
                    content = @Content(mediaType = "text/html", schema = @Schema(type = "String"))),
            @ApiResponse(responseCode = "500", description = "서버에 의한 오류입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class)))})
    public String login(Model model){
        String kakaoAuthUrl = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="+clientId+"&redirect_uri="+redirectUri;
        model.addAttribute("kakaoAuthUrl", kakaoAuthUrl);
        return "login";
    }



    @PostMapping("/login")
    @Operation(description = "서버가 클라이언트가 제출한 사용자 정보를 가지고 로그인을 진행합니다.", tags = "Member")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인에 성공하였습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = JwtDTO.class))),
            @ApiResponse(responseCode = "400", description = "요청의 양식이 잘못되었습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "아이디나 비밀번호 또는 계정 타입(소셜/기본)이 서버가 가지고 있는 것과 다릅니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "서버에 의한 오류입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class)))})
    public ResponseEntity<?> login(@RequestBody @Valid MemberDTO memberDTO) {
        String token;
        try {
            memberDTO.setAccountType("basic");
            memberService.login(memberDTO);
            token = jwtUtil.generateToken(memberDTO);
        } catch (UserNotFoundException e) {
            return responseError(e, HttpStatus.FORBIDDEN);
        } catch (RuntimeException e){
            return responseError(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new JwtDTO(token), HttpStatus.OK);
    }
}