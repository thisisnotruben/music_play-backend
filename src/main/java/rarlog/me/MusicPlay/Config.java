package rarlog.me.MusicPlay;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import rarlog.me.MusicPlay.controller.AccountController;
import rarlog.me.MusicPlay.repository.AlbumRepository;
import rarlog.me.MusicPlay.repository.ArtistRepository;
import rarlog.me.MusicPlay.repository.SongRepository;
import rarlog.me.MusicPlay.security.AppUserPrincipalDetailsService;
import rarlog.me.MusicPlay.security.JwtFilter;
import rarlog.me.Service.SearchService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class Config {

    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;
    private final SongRepository songRepository;
    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(AccountController.REQUEST_MAPPING.concat("/create"),
                                AccountController.REQUEST_MAPPING.concat("/login"),
                                "/swagger-ui/**", "/v3/api-docs/**", "/api/v1/**")
                        .permitAll()
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(
            AppUserPrincipalDetailsService appUserPrincipalDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(appUserPrincipalDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) {
        return config.getAuthenticationManager();
    }

    @Bean
    public SearchService searchService(
            @Value("${search.url}") String searchUrl,
            @Value("${search.core}") String searchCore) {
        return new SearchService(searchUrl, searchCore, artistRepository, albumRepository, songRepository);
    }

}
