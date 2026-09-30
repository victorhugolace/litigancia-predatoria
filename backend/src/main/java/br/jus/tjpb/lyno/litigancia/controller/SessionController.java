package br.jus.tjpb.lyno.litigancia.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.core.env.*;
import java.util.List;

@RestController
public class SessionController {
    private final Environment environment;
    public SessionController(Environment environment) { this.environment = environment; }

    public record Session(boolean autenticado, String usuario, List<String> perfis, String csrfToken, boolean demo) {}

    @GetMapping("/api/session")
    public Session session(Authentication auth, CsrfToken csrf) {
        boolean authenticated = auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken);
        return new Session(authenticated, authenticated ? auth.getName() : null,
                authenticated ? auth.getAuthorities().stream().map(a -> a.getAuthority().replace("ROLE_", "")).toList() : List.of(),
                csrf.getToken(), environment.acceptsProfiles(Profiles.of("demo")));
    }
}
