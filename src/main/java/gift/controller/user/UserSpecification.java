package gift.controller.user;

import gift.dto.user.LoginResponse;
import gift.dto.user.UserRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Tag(name = "유저 등록 및 로그인", description = "유저 등록 및 로그인을 위한 API")
public interface UserSpecification {

    @PostMapping("/login")
    @Operation(summary = "유저 로그인", description = "유저 인증을 진행하고 JWT 토큰을 발급합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "로그인 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Map.class),
                                    examples = @ExampleObject(value = "{\"access_token\":\"exampleToken\"}")
                            )
                    )
            }
    )
    ResponseEntity<LoginResponse.Info> login(@RequestBody UserRequest.Check userRequest);

    @Operation(
            summary = "유저 회원가입",
            description = "새로운 유저를 등록합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "회원가입 성공")
            }
    )
    ResponseEntity<String> register(@RequestBody UserRequest.Create userRequest);


}
