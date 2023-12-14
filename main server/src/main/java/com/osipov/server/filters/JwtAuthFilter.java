package com.osipov.server.filters;

import com.osipov.server.dto.TokenBody;
import com.osipov.server.error.InvalidTokenException;
import com.osipov.server.error.NotFoundException;
import com.osipov.server.model.Client;
import com.osipov.server.repository.ClientRepository;
import com.osipov.server.service.impl.JwtServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.osipov.server.error.ExceptionDescriptions.USER_NOT_FOUND;


@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final ClientRepository clientRepository;
    private final JwtServiceImpl jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (!(authHeader != null && authHeader.startsWith("Bearer "))) {
            filterChain.doFilter(request, response);
            return;
        }

        TokenBody tokenBody;

        try {
            tokenBody = jwtService.getTokenBody(authHeader.substring(7));
        } catch (InvalidTokenException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        if (!tokenBody.getTokenType().equals("access")) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        Client client = clientRepository
                .findById(tokenBody.getClientId())
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND.getTitle()));

        if (client == null || SecurityContextHolder.getContext().getAuthentication() != null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(client, null, client.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
    }
}