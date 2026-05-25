package rarlog.me.MusicPlay.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "playlist_song")
public class PlaylistSong {

    @EmbeddedId
    private PlaylistSongKey id;

    @OneToOne
    @MapsId("songId")
    @JoinColumn(name = "song_id")
    private Song song;

    @ManyToOne
    @MapsId("playlistId")
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;

    public PlaylistSong(Song song, Playlist playlist) {
        this.id = new PlaylistSongKey(song.getId(), playlist.getId());
        this.song = song;
        this.playlist = playlist;
    }

}