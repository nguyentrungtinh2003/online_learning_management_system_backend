package com.TrungTinhBackend.codearena_backend.Config;

import com.TrungTinhBackend.codearena_backend.Service.Jwt.JwtAuthFilter;
import com.TrungTinhBackend.codearena_backend.Service.Jwt.JwtUtils;
import com.TrungTinhBackend.codearena_backend.Service.Jwt.UserDetailsService;
import com.TrungTinhBackend.codearena_backend.Service.LoginLog.LoginLogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // Bật tính năng dùng @PreAuthorize
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(UserDetailsService userDetailsService, JwtAuthFilter jwtAuthFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Không dùng session
                .authorizeHttpRequests(request -> request

                        // Thêm các đường dẫn Swagger UI và tài liệu API để không bị chặn
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/oauth2/**",             // 👈 Cho phép truy cập OAuth2 endpoint
                                "/login/oauth2/**", "/robots.txt", "/api/ws/**",  "/api/user-google" ).permitAll()
                        // Các API cần quyền truy cập
                        .requestMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/api/teacher/**").hasAnyAuthority("ROLE_TEACHER", "ROLE_ADMIN")
                        .requestMatchers("/api/student/**").hasAnyAuthority("ROLE_STUDENT","ROLE_TEACHER","ROLE_ADMIN")
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/api/**").permitAll()

                        // Các yêu cầu khác không yêu cầu xác thực
                        .anyRequest().permitAll()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"You are not authorized to access this resource\"}");
                        })
                )
                // 🔹 Sử dụng Bean thay vì tạo mới instance
                .oauth2Login(oauth -> oauth.defaultSuccessUrl("https://codearena-frontend-dev.vercel.app/", true))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        return new MappingJackson2HttpMessageConverter();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
