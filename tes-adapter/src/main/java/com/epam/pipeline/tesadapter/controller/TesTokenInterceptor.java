package com.epam.pipeline.tesadapter.controller;

import com.epam.pipeline.tesadapter.entity.TesTokenHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.web.util.matcher.IpAddressMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@SuppressWarnings("unused")
@Component
public class TesTokenInterceptor implements HandlerInterceptor {
    private static final String HTTP_AUTH_COOKIE = "HttpAuthorization";

    private TesTokenHolder tesTokenHolder;

    @Value("${cloud.pipeline.token}")
    private String defaultPipelineToken;

    private final IpAddressMatcher ipAddressMatcherV4;
    private final IpAddressMatcher ipAddressMatcherV6;

    @Autowired
    public TesTokenInterceptor(TesTokenHolder tesTokenHolder,
                               @Value("${security.allowed.client.ipv4.range}") String ipRangeV4,
                               @Value("${security.allowed.client.ipv6.range}") String ipRangeV6) {
        this.tesTokenHolder = tesTokenHolder;
        ipAddressMatcherV4 = StringUtils.isNotEmpty(ipRangeV4) ? new IpAddressMatcher(ipRangeV4) : null;
        ipAddressMatcherV6 = StringUtils.isNotEmpty(ipRangeV6) ? new IpAddressMatcher(ipRangeV6) : null;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (checkRequestForToken(request).isPresent()) {
            tesTokenHolder.setToken(checkRequestForToken(request).get());
            return true;
        } else if (checkClientHostAddress(request) && Strings.isNotEmpty(defaultPipelineToken)) {
            tesTokenHolder.setToken(defaultPipelineToken);
            return true;
        }
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        return false;
    }

    private Optional<String> checkRequestForToken(HttpServletRequest request) {
        if (StringUtils.isNotEmpty(request.getHeader(HttpHeaders.AUTHORIZATION))) {
            return Optional.of(request.getHeader(HttpHeaders.AUTHORIZATION));
        } else if (ArrayUtils.isNotEmpty(request.getCookies())) {
            return Arrays.stream(request.getCookies()).filter(cookie ->
                    cookie.getName().equalsIgnoreCase(HTTP_AUTH_COOKIE))
                    .map(Cookie::getValue).findFirst();
        }
        return Optional.empty();
    }

    private boolean checkClientHostAddress(HttpServletRequest request) {
        return (ipAddressMatcherV4 != null && ipAddressMatcherV4.matches(request)) ||
                (ipAddressMatcherV6 != null && ipAddressMatcherV6.matches(request));
    }
}
