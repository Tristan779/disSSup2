package be.kuleuven.foodrestservice.config;

import be.kuleuven.foodrestservice.security.ApiKeyFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

    @Bean
    public FilterRegistrationBean<ApiKeyFilter> apiKeyFilter() {
        FilterRegistrationBean<ApiKeyFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new ApiKeyFilter());
        registrationBean.addUrlPatterns("/rest/*");

        return registrationBean;
    }
}