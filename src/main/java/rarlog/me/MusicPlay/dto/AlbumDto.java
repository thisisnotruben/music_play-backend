package rarlog.me.MusicPlay.dto;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;
import lombok.NoArgsConstructor;
import rarlog.me.MusicPlay.entity.Album;

@Data
@NoArgsConstructor
public class AlbumDto {

    private String name;
    private String coverPath;
    private String artistName;
    private List<SongDto> songs;

    public AlbumDto(Album album) {
        this.name = album.getName();
        this.coverPath = album.getCoverPath();
        this.artistName = album.getArtist().getName();
        this.songs = album.getSongs().stream().map(s -> new SongDto(s)).collect(Collectors.toList());
    }

}
