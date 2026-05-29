package rarlog.me.MusicPlay.dto;

import org.apache.solr.client.solrj.beans.Field;

import lombok.Data;
import rarlog.me.MusicPlay.entity.Album;
import rarlog.me.MusicPlay.entity.Artist;
import rarlog.me.MusicPlay.entity.Song;

@Data
public class SearchDto {

    public final static String TYPE_ARTIST = "ARTIST";
    public final static String TYPE_ALBUM = "ALBUM";
    public final static String TYPE_SONG = "SONG";

    @Field
    private final String name;

    @Field
    private final String type;

    @Field
    private final long id;

    public SearchDto(Artist artist) {
        this.name = artist.getName();
        this.type = TYPE_ARTIST;
        this.id = artist.getId();
    }

    public SearchDto(Album album) {
        this.name = album.getName();
        this.type = TYPE_ALBUM;
        this.id = album.getId();
    }

    public SearchDto(Song song) {
        this.name = song.getName();
        this.type = TYPE_SONG;
        this.id = song.getId();
    }

}
