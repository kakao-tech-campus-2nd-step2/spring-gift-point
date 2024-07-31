package gift.serverSideRendering.global;

import gift.domain.exception.conflict.ProductAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(basePackages = "gift.serverSideRendering")
public class ServerRenderGlobalExceptionHandler {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ProductAlreadyExistsException.class)
    public String handleProductAlreadyExistsException(ProductAlreadyExistsException e, Model model) {
        model.addAttribute("headTitle", "상품 중복");
        model.addAttribute("pageTitle", "상품 중복 오류");
        model.addAttribute("errorMessage", "이름, 가격, 이미지 URL이 같은 상품이 이미 존재합니다. 중복이 아닌 상품을 입력해주세요.");
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
