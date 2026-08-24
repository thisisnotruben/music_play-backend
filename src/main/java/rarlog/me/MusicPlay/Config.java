package rarlog.me.MusicPlay;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import rarlog.me.MusicPlay.service.StorageService;
import rarlog.me.Service.SearchService;
import rarlog.me.repository.AlbumRepository;
import rarlog.me.repository.ArtistRepository;
import rarlog.me.repository.SongRepository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class Config {

    public final static String AUTH_USER_EVENT_QUEUE_NAME = "${message.user.queueName}";

    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;
    private final SongRepository songRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/actuator/health").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(server ->
                        server.jwt(Customizer.withDefaults()))
                .build();
    }

    @Bean
    public OpenAPI openAPI() {

        List<SecurityRequirement> securityRequirementList = Arrays.asList(new SecurityRequirement().
                addList("BearerAuth"), new SecurityRequirement().
                addList("BasicAuth"));

        Map<String, SecurityScheme> securitySchemeMap = new HashMap<>();
        securitySchemeMap.put("BearerAuth", new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer"));
        securitySchemeMap.put("BasicAuth", new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("basic"));

        return new OpenAPI()
                .info(new Info().title("Music Play API")
                        .description("Used for the querying of account/music services.")
                        .version("1.0"))
                .security(securityRequirementList)
                .components(new Components().securitySchemes(securitySchemeMap));
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
