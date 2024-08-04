package gift.aop;

import gift.model.product.Product;
import gift.model.user.User;
import gift.service.ProductService;
import gift.service.UserService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Aspect
@Component
public class ProductMatchesUserAop {

    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;

    @Pointcut("execution(* gift.service.ProductService.update(..))")
    public void updateMethod() {
    }

    @Pointcut("execution(* gift.service.ProductService.delete(..))")
    public void deleteMethod() {
    }

    @Pointcut("execution(* gift.service.ProductService.addProductOption(..))")
    public void addProductOptionMethod() {
    }

    @Pointcut("execution(* gift.service.ProductService.editProductOption(..))")
    public void editProductOptionMethod() {
    }

    @Pointcut("execution(* gift.service.ProductService.deleteProductOption(..))")
    public void deleteProductOptionMethod() {
    }

    @Before("updateMethod() || deleteMethod() " +
            "|| addProductOptionMethod() || editProductOptionMethod() || deleteMethod()")
    public void productMatchesUser(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Long id = (Long) args[0];
        String email = (String) args[args.length - 1];

        User user = userService.findOne(email);
        Product product = productService.findById(id);
        if (user.getId() != product.getUser().getId()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
    }
}
