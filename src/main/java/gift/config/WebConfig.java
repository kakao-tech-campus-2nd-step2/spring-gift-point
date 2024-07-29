package gift.config;

import gift.member.HandlerMemberArgumentResolver;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final HandlerMemberArgumentResolver handlerMemberArgumentResolver;

    public WebConfig(HandlerMemberArgumentResolver handlerMemberArgumentResolver) {
        this.handlerMemberArgumentResolver = handlerMemberArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(handlerMemberArgumentResolver);
    }
}
