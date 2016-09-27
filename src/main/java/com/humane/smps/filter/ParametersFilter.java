package com.humane.smps.filter;

import com.humane.smps.service.CustomUserDetails;
import com.humane.util.filter.EnhancedHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ParametersFilter implements Filter {
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Map<String, String[]> additionalParams = new HashMap<>();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof CustomUserDetails) {
                CustomUserDetails userDetails = (CustomUserDetails) principal;
                additionalParams.put("userAdmissions", new String[]{userDetails.getUserAdmissions()});
            }
        }

        EnhancedHttpRequest enhancedHttpRequest = new EnhancedHttpRequest((HttpServletRequest) request, additionalParams);
        chain.doFilter(enhancedHttpRequest, response);
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }
}
