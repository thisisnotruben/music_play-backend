package rarlog.me.MusicPlay.dto;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;
import rarlog.me.MusicPlay.entity.Playlist;

@Data
public class PlaylistDto {

    private String name;
    private List<SongDto> songs;

    public PlaylistDto(Playlist playlist) {
        this.name = playlist.getName();
        this.songs = playlist.getPlaylistSongs().stream().map(p -> new SongDto(p.getSong()))
                .collect(Collectors.toList());
    }

}
