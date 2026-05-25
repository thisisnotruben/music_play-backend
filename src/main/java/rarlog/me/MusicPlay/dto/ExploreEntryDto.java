package rarlog.me.MusicPlay.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExploreEntryDto {

    private String label;
    private List<PlaylistDto> feed;

}
