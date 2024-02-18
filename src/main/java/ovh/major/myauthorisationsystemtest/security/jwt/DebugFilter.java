package ovh.major.myauthorisationsystemtest.security.jwt;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Log4j2
public class DebugFilter implements Filter {

    private final FilterChainProxy filterChainProxy;

    public DebugFilter(FilterChainProxy filterChainProxy) {
        this.filterChainProxy = filterChainProxy;
    }

    @PostConstruct
    public void init() {
        for (SecurityFilterChain chain : filterChainProxy.getFilterChains()) {
            log.info("\n\n" + chain.getClass());
            chain.getFilters()
                    .stream()
                    .forEach(filter -> log.info(filter));
        }
        log.info("\n");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(servletRequest, servletResponse);
    }
}