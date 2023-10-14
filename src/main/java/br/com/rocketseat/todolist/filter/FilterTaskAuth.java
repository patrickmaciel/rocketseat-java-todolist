package br.com.rocketseat.todolist.filter;

import jakarta.servlet.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class FilterTaskAuth implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("[DEBUG] FilterTaskAuth");
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
