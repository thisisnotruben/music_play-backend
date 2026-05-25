package rarlog.me.MusicPlay.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistSongKey implements Serializable {

    @Column(name = "song_id")
    private long songId;

    @Column(name = "playlist_id")
    private long playlistId;

}
