package br.jus.tjpb.lyno.litigancia.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    org.springframework.security.crypto.password.PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    UserDetailsService users(@Value("${app.auth.reporter-user}") String reporter,
                            @Value("${app.auth.reporter-password}") String reporterPassword,
                            @Value("${app.auth.analyst-user}") String analyst,
                            @Value("${app.auth.analyst-password}") String analystPassword,
                            org.springframework.security.crypto.password.PasswordEncoder encoder) {
        if (!reporter.matches("[A-Za-z0-9._-]{3,120}") || !analyst.matches("[A-Za-z0-9._-]{3,120}") || reporter.equals(analyst)) {
            throw new IllegalStateException("Defina usuários distintos e válidos para comunicante e analista");
        }
        if (reporterPassword.length() < 12 || analystPassword.length() < 12) {
            throw new IllegalStateException("Defina APP_REPORTER_PASSWORD e APP_ANALYST_PASSWORD com pelo menos 12 caracteres");
        }
        return new InMemoryUserDetailsManager(
                User.withUsername(reporter).password(encoder.encode(reporterPassword)).roles("COMUNICANTE").build(),
                User.withUsername(analyst).password(encoder.encode(analystPassword)).roles("ANALISTA").build());
    }

    @Bean
    SecurityFilterChain security(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/session", "/api/login", "/error").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/denuncias").hasAnyRole("COMUNICANTE", "ANALISTA")
                .requestMatchers(HttpMethod.GET, "/api/denuncias/*", "/api/denuncias/protocolo/*", "/api/denuncias/*/documentos/*").authenticated()
                .requestMatchers("/api/denuncias/**", "/api/dashboard/**").hasRole("ANALISTA")
                .anyRequest().denyAll())
            .formLogin(login -> login.loginProcessingUrl("/api/login")
                .successHandler((request, response, auth) -> response.setStatus(204))
                .failureHandler((request, response, exception) -> {
                    response.setStatus(401); response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"mensagem\":\"Usuário ou senha inválidos\"}");
                }))
            .logout(logout -> logout.logoutUrl("/api/logout").deleteCookies("JSESSIONID")
                .logoutSuccessHandler((request, response, auth) -> response.setStatus(204)))
            .exceptionHandling(errors -> errors
                .authenticationEntryPoint((request, response, exception) -> {
                    response.setStatus(401); response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"mensagem\":\"Entre na aplicação para continuar\"}");
                })
                .accessDeniedHandler((request, response, exception) -> {
                    response.setStatus(403); response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"mensagem\":\"Acesso não permitido ou sessão inválida. Atualize a página e tente novamente.\"}");
                }));
        // Mantém a proteção CSRF padrão. /api/session entrega o token mascarado ao frontend.
        return http.build();
    }
}
