package br.com.rocketseat.todolist.filter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.rocketseat.todolist.user.IUserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {
    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("FILTER - FilterTaskAuth");

        var servletPath = request.getServletPath();
        if (servletPath.equals("/tasks/")) {
            var authorization = request.getHeader("Authorization");
            var authEncoded = authorization.substring("Basic".length()).trim();
            byte[] authDecodedBytes = Base64.getDecoder().decode(authEncoded);
            var authDecoded = new String(authDecodedBytes);
            String[] credentials = authDecoded.split(":");
            String username = credentials[0];
            String password = credentials[1];

            System.out.println("username: " + username);
            System.out.println("password: " + password);

            var user = this.userRepository.findByUsername(username);
            if (user == null) {
                response.sendError(401, "Usuário sem autorização");
            }

            if (user != null) {
                var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                if (passwordVerify.verified) {
                    System.out.println("Seguindo a vida, usuário autenticado");
                    request.setAttribute("idUser", user.getId());
                    filterChain.doFilter(request, response);
                }

                if (!passwordVerify.verified) {
                    response.sendError(401);
                }
            }
        } else {
            System.out.println("Rota pública, seguindo a vida");
            filterChain.doFilter(request, response);
        }
    }
}
