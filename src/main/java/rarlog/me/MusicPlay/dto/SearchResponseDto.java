package rarlog.me.MusicPlay.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchResponseDto {

    private final List<ArtistDto> artists;
    private final List<AlbumDto> albums;
    private final List<SongDto> songs;

}
