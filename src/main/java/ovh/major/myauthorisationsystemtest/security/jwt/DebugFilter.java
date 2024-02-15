package ovh.major.myauthorisationsystemtest.security.jwt;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.*;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DebugFilter implements Filter {

    private final FilterChainProxy filterChainProxy;

    public DebugFilter(FilterChainProxy filterChainProxy) {
        this.filterChainProxy = filterChainProxy;
    }

    @PostConstruct
    public void init() {
        for (SecurityFilterChain chain : filterChainProxy.getFilterChains()) {
            System.out.println(chain.toString());
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(servletRequest, servletResponse);
    }
}