package gift.mock;

import gift.domain.constants.Platform;
import gift.domain.vo.Email;
import gift.web.dto.MemberDetails;
import gift.web.resolver.LoginMemberArgumentResolver;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

@TestComponent
public class MockLoginMemberArgumentResolver extends LoginMemberArgumentResolver {

    public MockLoginMemberArgumentResolver() {
        super(null, null);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return new MemberDetails(1L, Email.from("member01@gmail.com"), Platform.GIFT);
    }
}
