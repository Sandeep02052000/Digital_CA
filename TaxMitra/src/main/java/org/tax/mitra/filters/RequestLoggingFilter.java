package org.tax.mitra.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
@Order(1)
public class RequestLoggingFilter implements Filter {

    private static final String REQUEST_ID = "requestId";
    private static final String PATH = "path";
    private static final String METHOD = "method";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        try {
            MDC.put(REQUEST_ID, UUID.randomUUID().toString());
            MDC.put(PATH, req.getRequestURI());
            MDC.put(METHOD, req.getMethod());
            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}