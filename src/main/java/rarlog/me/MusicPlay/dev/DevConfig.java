package rarlog.me.MusicPlay.dev;

import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import rarlog.me.MusicPlay.dto.AccountCreateDto;
import rarlog.me.MusicPlay.entity.Album;
import rarlog.me.MusicPlay.entity.Artist;
import rarlog.me.MusicPlay.entity.Song;
import rarlog.me.MusicPlay.repository.AlbumRepository;
import rarlog.me.MusicPlay.repository.ArtistRepository;
import rarlog.me.MusicPlay.repository.SongRepository;
import rarlog.me.MusicPlay.service.AppUserService;

@Configuration
@RequiredArgsConstructor
public class DevConfig {

    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final AppUserService appUserService;
    private final SongRepository songRepository;

    @Bean
    public CommandLineRunner initDevEnv(@Value("${misc.dataPath}") String dataPath) {
        return (args) -> {

            appUserService.createAccount(
                    new AccountCreateDto("admin", "admin123", "admin@example.com", "John", "Doe"));

            InputStreamReader reader = new InputStreamReader(
                    new ClassPathResource(dataPath).getInputStream());
            ObjectMapper objectMapper = new ObjectMapper();
            DevData devData = objectMapper.readValue(reader, DevData.class);

            devData.getData().keySet().stream().forEach(artistName -> {

                Artist artist = artistRepository.save(Artist.builder()
                        .name(artistName).build());

                devData.getData().get(artistName).getAlbums().stream().forEach(devDataAlbum -> {
                    Album album = albumRepository.save(Album.builder()
                            .name(devDataAlbum.getName())
                            .coverPath(devDataAlbum.getCoverPath())
                            .artist(artist)
                            .build());

                    songRepository.saveAll(devDataAlbum.getSongs().stream()
                            .map(songData -> Song.builder()
                                    .name(songData.getName())
                                    .genre(songData.getGenre())
                                    .length(songData.getLength())
                                    .audioPath(songData.getAudioPath())
                                    .album(album)
                                    .build())
                            .collect(Collectors.toList()));
                });
            });
        };
    }

}
