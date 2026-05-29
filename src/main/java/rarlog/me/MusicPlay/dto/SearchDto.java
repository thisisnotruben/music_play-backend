package rarlog.me.MusicPlay.dto;

import org.apache.solr.client.solrj.beans.Field;

import lombok.Data;
import lombok.NoArgsConstructor;
import rarlog.me.MusicPlay.entity.Album;
import rarlog.me.MusicPlay.entity.Artist;
import rarlog.me.MusicPlay.entity.Song;

@Data
@NoArgsConstructor
public class SearchDto {

    public final static String TYPE_ARTIST = "ARTIST";
    public final static String TYPE_ALBUM = "ALBUM";
    public final static String TYPE_SONG = "SONG";

    @Field
    private String name;

    @Field
    private String type;

    @Field
    private long dbId;

    public SearchDto(Artist artist) {
        this.name = artist.getName();
        this.type = TYPE_ARTIST;
        this.dbId = artist.getId();
    }

    public SearchDto(Album album) {
        this.name = album.getName();
        this.type = TYPE_ALBUM;
        this.dbId = album.getId();
    }

    public SearchDto(Song song) {
        this.name = song.getName();
        this.type = TYPE_SONG;
        this.dbId = song.getId();
    }

}
