package gift.restdocs;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@TestConfiguration
public class RestDocsConfig implements WebMvcConfigurer {

    @Bean
    public RestDocumentationResultHandler write() {
        return MockMvcRestDocumentation.document(
            "{class-name}/{method-name}",
            Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
            Preprocessors.preprocessResponse(Preprocessors.prettyPrint())
        );
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MockAuthApiInterceptor())
            .excludePathPatterns("/api/join","/api/login");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new MockLoginMemberArgumentResolver());
    }
}
