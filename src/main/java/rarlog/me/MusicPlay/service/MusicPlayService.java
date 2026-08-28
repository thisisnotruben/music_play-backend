package rarlog.me.MusicPlay.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import rarlog.me.MusicPlay.exception.AlbumNotFoundException;
import rarlog.me.MusicPlay.exception.PlaylistNotFoundException;
import rarlog.me.MusicPlay.exception.SongNotFoundException;
import rarlog.me.MusicPlay.exception.UserNotFoundException;
import rarlog.me.dto.AlbumDto;
import rarlog.me.dto.ArtistDto;
import rarlog.me.dto.ExploreEntryDto;
import rarlog.me.dto.PlaylistDto;
import rarlog.me.entity.AppUser;
import rarlog.me.entity.Playlist;
import rarlog.me.entity.PlaylistSong;
import rarlog.me.entity.PlaylistSongKey;
import rarlog.me.entity.Song;
import rarlog.me.repository.AlbumRepository;
import rarlog.me.repository.AppUserRepository;
import rarlog.me.repository.ArtistRepository;
import rarlog.me.repository.PlaylistRepository;
import rarlog.me.repository.PlaylistSongRepository;
import rarlog.me.repository.SongRepository;

@Service
@RequiredArgsConstructor
public class MusicPlayService {

    private final AppUserRepository appUserRepository;
    private final PlaylistRepository playlistRepository;
    private final PlaylistSongRepository playlistSongRepository;
    private final SongRepository songRepository;
    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final StorageService storageService;

    public PlaylistDto createPlaylist(String userId, String playlistName, Optional<MultipartFile> file) {
        AppUser user = appUserRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Playlist playlist = playlistRepository.save(Playlist.builder()
                .coverPath("")
                .name(playlistName)
                .playlistSongs(new ArrayList<PlaylistSong>())
                .appUser(user)
                .build());

        if (file.isPresent()) {
            String uploadedPlaylistPath = storageService.uploadPlaylistCover(
                    user.getId(), playlist.getId(), file.get());
            playlist.setCoverPath(uploadedPlaylistPath);
            playlistRepository.save(playlist);
        }
        return new PlaylistDto(playlist);
    }

    public PlaylistDto editPlaylist(String userId, long playlistId, Optional<String> newName,
                                    Optional<MultipartFile> file) {
        AppUser user = appUserRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(PlaylistNotFoundException::new);

        newName.ifPresent(playlist::setName);

        if (file.isPresent()) {
            if (!playlist.getCoverPath().isEmpty()) {
                storageService.deletePlaylistCover(playlist.getCoverPath());
            }

            String uploadedPlaylistPath = storageService.uploadPlaylistCover(
                    user.getId(), playlist.getId(), file.get());
            playlist.setCoverPath(uploadedPlaylistPath);
        }

        playlistRepository.save(playlist);
        return new PlaylistDto(playlist);
    }

    public void deletePlaylist(String userId, long playlistId) {
        AppUser user = appUserRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(PlaylistNotFoundException::new);

        playlistSongRepository.deleteAll(playlist.getPlaylistSongs());

        user.getPlaylists().remove(playlist);
        if (!playlist.getCoverPath().isEmpty()) {
            storageService.deletePlaylistCover(playlist.getCoverPath());
        }
        playlistRepository.delete(playlist);
    }

    public void addSongToPlaylist(long songId, long playlistId) {
        Song song = songRepository.findById(songId)
                .orElseThrow(SongNotFoundException::new);
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(PlaylistNotFoundException::new);

        playlistSongRepository.save(new PlaylistSong(song, playlist));
    }

    public void deleteSongFromPlaylist(long songId, long playlistId) {
        playlistSongRepository.deleteById(new PlaylistSongKey(songId, playlistId));
    }

    public List<PlaylistDto> getPlaylists(String userId) {
        AppUser user = appUserRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        return user.getPlaylists().stream()
                .map(PlaylistDto::new)
                .collect(Collectors.toList());
    }

    public AlbumDto getAlbum(long albumId) {
        return new AlbumDto(albumRepository.findById(albumId)
                .orElseThrow(AlbumNotFoundException::new));
    }

    public List<AlbumDto> getAlbums() {
        return albumRepository.findAll().stream()
                .map(AlbumDto::new)
                .collect(Collectors.toList());
    }

    public List<ExploreEntryDto> getExploreFeed() {
        throw new NotImplementedException("TODO");
    }

    public List<ArtistDto> getArtists() {
        return artistRepository.findAll().stream()
                .map(artist -> new ArtistDto(artist.getName(), artist.getAlbums().stream()
                        .map(AlbumDto::new)
                        .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

}
