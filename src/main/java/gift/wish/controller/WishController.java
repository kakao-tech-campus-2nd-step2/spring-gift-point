package gift.wish.controller;

import gift.auth.domain.AuthInfo;
import gift.auth.service.AuthService;
import gift.global.exception.DomainValidationException;
import gift.global.response.ErrorResponseDto;
import gift.global.response.ResultCode;
import gift.global.response.ResultResponseDto;
import gift.global.response.SimpleResultResponseDto;
import gift.global.security.Login;
import gift.global.utils.ResponseHelper;
import gift.wish.domain.Wish;
import gift.wish.dto.WishRequestDto;
import gift.wish.dto.WishResponseDto;
import gift.wish.dto.WishResponseListDto;
import gift.wish.service.WishService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishes")
public class WishController {
    private final WishService wishService;
    private final AuthService authService;

    public WishController(WishService wishService, AuthService authService) {
        this.wishService = wishService;
        this.authService = authService;
    }

    // 차후에 삭제 예정
    @GetMapping("/all")
    public ResponseEntity<ResultResponseDto<List<WishResponseDto>>> getAllWishes(@Login AuthInfo authInfo) {
        List<WishResponseDto> wishResponseDtos = wishService.getAllWishesByMember(authService.getMemberById(authInfo.memberId()));
        return ResponseHelper.createResponse(ResultCode.GET_ALL_WISHES_SUCCESS, wishResponseDtos);
    }

    @GetMapping("")
    public ResponseEntity<ResultResponseDto<WishResponseListDto>> getWishesByPage(@RequestParam(name = "page") int page, @Login AuthInfo authInfo) {
        WishResponseListDto wishResponseDtos = wishService.getWishesByMemberAndPage(authService.getMemberById(authInfo.memberId()), page);
        return ResponseHelper.createResponse(ResultCode.GET_ALL_WISHES_SUCCESS, wishResponseDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultResponseDto<WishResponseDto>> getWishById(@PathVariable(name = "id") Long id, @Login AuthInfo authInfo) {
        WishResponseDto wishResponseDto = wishService.getWishById(id);
        return ResponseHelper.createResponse(ResultCode.GET_ALL_WISHES_SUCCESS, wishResponseDto);
    }

    @PostMapping("")
    public ResponseEntity<SimpleResultResponseDto> createWish(@Login AuthInfo authInfo, @RequestBody WishRequestDto wishRequestDto) {
        wishService.createWish(wishRequestDto.toWishServiceDto(authInfo.memberId()));
        return ResponseHelper.createSimpleResponse(ResultCode.CREATE_WISH_SUCCESS);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SimpleResultResponseDto> updateWish(@PathVariable(name = "id") Long id, @Login AuthInfo authInfo,
                                                              @RequestBody WishRequestDto wishRequestDto) {
        wishService.updateWish(wishRequestDto.toWishServiceDto(id, authInfo.memberId()));
        return ResponseHelper.createSimpleResponse(ResultCode.UPDATE_WISH_SUCCESS);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SimpleResultResponseDto> deleteWish(@PathVariable(name = "id") Long id) {
        wishService.deleteWish(id);
        return ResponseHelper.createSimpleResponse(ResultCode.DELETE_WISH_SUCCESS);
    }

    // GlobalException Handler 에서 처리할 경우,
    // RequestBody에서 발생한 에러가 HttpMessageNotReadableException 로 Wrapping 이 되는 문제가 발생한다
    // 때문에, 해당 에러로 Wrapping 되기 전 Controller 에서 Domain Error 를 처리해주었다
    @ExceptionHandler(DomainValidationException.class)
    public ResponseEntity<ErrorResponseDto> handleOptionValidException(DomainValidationException e) {
        System.out.println(e);
        return ResponseHelper.createErrorResponse(e.getErrorCode());
    }
}
