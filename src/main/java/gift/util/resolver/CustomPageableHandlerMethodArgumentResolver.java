package gift.util.resolver;

import java.util.stream.Collectors;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

public class CustomPageableHandlerMethodArgumentResolver extends PageableHandlerMethodArgumentResolver {

    @Override
    public Pageable resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                    NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        Pageable pageable = super.resolveArgument(parameter, mavContainer, webRequest, binderFactory);

        // createdDate가 포함되어 있으면 registrationDate로 변경
        Sort updatedSort = pageable.getSort().stream()
                .map(order -> "createdDate".equals(order.getProperty()) ?
                        new Sort.Order(order.getDirection(), "registrationDate") : order)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Sort::by));

        // 새로운 Pageable 객체 생성
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), updatedSort);
    }
}