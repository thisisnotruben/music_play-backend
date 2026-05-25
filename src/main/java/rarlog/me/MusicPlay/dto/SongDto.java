package rarlog.me.MusicPlay.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import rarlog.me.MusicPlay.entity.Song;

@Data
@NoArgsConstructor
public class SongDto {

    private String name;
    private String genre;
    private float length;
    private String audioPath;
    private String albumName;

    public SongDto(Song song) {
        this.name = song.getName();
        this.genre = song.getGenre();
        this.length = song.getLength();
        this.audioPath = song.getAudioPath();
        this.albumName = song.getAlbum().getName();
    }

}
