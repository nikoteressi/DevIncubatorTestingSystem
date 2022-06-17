package com.example.dits.filters;

import org.eclipse.jetty.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthentificatedUserRedirectFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String baseUrl = "http://localhost:8080";

        if (isAuthenticated() && "/".equals(request.getRequestURI())) {
            String encodedRedirectURL = "";

            if (getAuthority().contains("USER")) {
                encodedRedirectURL = response.encodeRedirectURL(
                        baseUrl + "/user/chooseTest");
            }
            if (getAuthority().contains("ADMIN")) {
                encodedRedirectURL = response.encodeRedirectURL(
                        baseUrl + "/admin/users-list");
            }

            response.encodeRedirectURL(encodedRedirectURL);
            response.setHeader("Location", encodedRedirectURL);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.
                isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return authentication.isAuthenticated();
    }

    private static String getAuthority() {
        Object principal = getPrincipal();
        return principal instanceof UserDetails ? String.valueOf(((UserDetails) principal).getAuthorities().stream().findFirst().orElse(null)) : principal.toString();
    }

    private static Object getPrincipal() {
        return SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
    }
}
