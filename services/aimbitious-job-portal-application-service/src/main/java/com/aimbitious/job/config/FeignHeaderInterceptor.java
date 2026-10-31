package com.aimbitious.job.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

@Configuration
public class FeignHeaderInterceptor {

    private static final List<String> FORWARDED_HEADERS = List.of(
            "X-User-Id", "X-User-Email", "X-User-Roles"
    );

    @Bean
    public RequestInterceptor propagateUserHeaders() {
        return (RequestTemplate template) -> {
            ServletRequestAttributes attributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes == null) return;

            HttpServletRequest request = attributes.getRequest();
            for (String header : FORWARDED_HEADERS) {
                String value = request.getHeader(header);
                if (value != null) {
                    template.header(header, value);
                }
            }
        };
    }
}
