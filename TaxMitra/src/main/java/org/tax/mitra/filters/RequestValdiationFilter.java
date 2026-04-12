package org.tax.mitra.filters;

import io.netty.util.internal.StringUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(2)
public class RequestValdiationFilter implements Filter {
    private static final String secretKey = "subalTech";
    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String apiKey = request.getHeader("X-API-KEY").trim();
        if(StringUtil.isNullOrEmpty(apiKey)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Please pass the secret key in the request");
            return;
        } else if (!apiKey.equalsIgnoreCase(secretKey)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid API Key");
            return;
        }
        filterChain.doFilter(request, response);
    }
}
