package rarlog.me.MusicPlay.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import rarlog.me.dto.AlbumDto;
import rarlog.me.dto.AllMusicDto;
import rarlog.me.dto.ExploreEntryDto;
import rarlog.me.dto.PlaylistDto;
import rarlog.me.entity.AppUser;
import rarlog.me.entity.Playlist;
import rarlog.me.entity.PlaylistSong;
import rarlog.me.entity.Song;
import rarlog.me.MusicPlay.exception.PlaylistNotFoundException;
import rarlog.me.MusicPlay.exception.SongNotFoundException;
import rarlog.me.MusicPlay.exception.UserNotFoundException;
import rarlog.me.MusicPlay.repository.AlbumRepository;
import rarlog.me.MusicPlay.repository.AppUserRepository;
import rarlog.me.MusicPlay.repository.ArtistRepository;
import rarlog.me.MusicPlay.repository.PlaylistRepository;
import rarlog.me.MusicPlay.repository.PlaylistSongRepository;
import rarlog.me.MusicPlay.repository.SongRepository;

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

    public void createPlaylist(String username, String playlistName, Optional<MultipartFile> file) {
        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        Playlist playlist = playlistRepository.save(Playlist.builder()
                .coverPath("")
                .name(playlistName)
                .appUser(user)
                .build());

        if (file.isPresent()) {
            String uploadedPlaylistPath = storageService.uploadPlaylistCover(
                    user.getId(), playlist.getId(), file.get());
            playlist.setCoverPath(uploadedPlaylistPath);
            playlistRepository.save(playlist);
        }
    }

    public void deletePlaylist(String username, long playlistId) {
        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new PlaylistNotFoundException());

        for (PlaylistSong playlistSong : playlist.getPlaylistSongs()) {
            playlistSongRepository.delete(playlistSong);
        }
        
        user.getPlaylists().remove(playlist);
        if (!playlist.getCoverPath().isEmpty()) {
            storageService.deletePlaylistCover(playlist.getCoverPath());
        }
        playlistRepository.delete(playlist);
    }

    public void addSongToPlaylist(long songId, long playlistId) {
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new SongNotFoundException());
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new PlaylistNotFoundException());

        playlistSongRepository.save(new PlaylistSong(song, playlist));
    }

    public void deleteSongfromPlaylist(long songId, long playlistId) {
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new SongNotFoundException());
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new PlaylistNotFoundException());

        playlistSongRepository.delete(new PlaylistSong(song, playlist));
    }

    public List<PlaylistDto> getPlaylists(String username) {
        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        return user.getPlaylists().stream()
                .map(playlist -> new PlaylistDto(playlist))
                .collect(Collectors.toList());
    }

    public List<AlbumDto> getAlbums() {
        return albumRepository.findAll().stream()
                .map(album -> new AlbumDto(album))
                .collect(Collectors.toList());
    }

    public List<ExploreEntryDto> getExploreFeed() {
        throw new NotImplementedException("TODO");
    }

    public List<AllMusicDto> getAllMusic() {
        return artistRepository.findAll().stream()
                .map(artist -> new AllMusicDto(artist.getName(), artist.getAlbums().stream()
                        .map(album -> new AlbumDto(album))
                        .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

}
