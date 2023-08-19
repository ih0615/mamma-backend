package com.example.mammabackend.global.common.security.properties;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Getter
@Setter
@Configuration
@ConfigurationProperties("props.security")
public class SecurityProperties {

    private List<String> skipPath;

    public RequestMatcher[] getSkipRequestMatcher() {

        return skipPath.stream()
            .map(pathInfo -> {
                String[] pathInfoArr = pathInfo.split(" ");
                if (pathInfoArr.length == 2) {
                    return AntPathRequestMatcher.antMatcher(
                        HttpMethod.valueOf(pathInfoArr[0].toUpperCase()));
                }
                return AntPathRequestMatcher.antMatcher(pathInfo);
            }).toList().toArray(RequestMatcher[]::new);

    }

}
