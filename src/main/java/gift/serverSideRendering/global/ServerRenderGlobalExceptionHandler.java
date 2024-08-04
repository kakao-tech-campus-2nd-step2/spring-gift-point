package gift.serverSideRendering.global;

import gift.domain.exception.ErrorCode;
import gift.domain.exception.ServerException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(basePackages = "gift.serverSideRendering")
public class ServerRenderGlobalExceptionHandler {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ServerException.class)
    public String handleServerException(ServerException e, Model model) {
        model.addAttribute("headTitle", "오류 페이지");
        model.addAttribute("pageTitle", "서버 오류");
        model.addAttribute("errorMessage", "오류가 발생하였습니다. 원인: " + e.getErrorCode().getErrorMessage());

        if (e.getErrorCode() == ErrorCode.PRODUCT_ALREADY_EXISTS) {
            model.addAttribute("headTitle", "상품 중복");
            model.addAttribute("pageTitle", "상품 중복 오류");
            model.addAttribute("errorMessage", "이름, 가격, 이미지 URL이 같은 상품이 이미 존재합니다. 중복이 아닌 상품을 입력해주세요.");
        }

        return "errorDisplayPage";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleMethodArgumentNotValidException(MethodArgumentNotValidException e, Model model) {
        model.addAttribute("headTitle", "입력 오류");
        model.addAttribute("pageTitle", "입력 오류");
        model.addAttribute("errorMessage", e.getBindingResult().getFieldError().getDefaultMessage());
        return "errorDisplayPage";
    }
}
