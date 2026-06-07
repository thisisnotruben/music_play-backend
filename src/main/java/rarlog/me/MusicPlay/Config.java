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
import rarlog.me.MusicPlay.security.AppUserPrincipalDetailsService;
import rarlog.me.MusicPlay.security.JwtFilter;
import rarlog.me.MusicPlay.service.StorageService;
import rarlog.me.Service.SearchService;
import rarlog.me.repository.AlbumRepository;
import rarlog.me.repository.ArtistRepository;
import rarlog.me.repository.SongRepository;

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
            @Value("${search.host}") String host,
            @Value("${search.port}") String port,
            @Value("${search.collection}") String collection) {

        return new SearchService(String.format("%s:%s", host, port),
                collection, artistRepository, albumRepository, songRepository);
    }

    @Bean
    public StorageService storageService(
            @Value("${storage.host}") String host,
            @Value("${storage.hostPort}") String post,
            @Value("${storage.regionName}") String regionName,
            @Value("${storage.accessKey}") String accessKey,
            @Value("${storage.secretKey}") String secretKey,
            @Value("${storage.playlistBucket}") String playlistBucket) {

        return new StorageService(String.format("%s:%s", host, post),
                accessKey, secretKey, regionName, playlistBucket);
    }

}
