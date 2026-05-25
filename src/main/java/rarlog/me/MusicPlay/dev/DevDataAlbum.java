package rarlog.me.MusicPlay.dev;

import java.util.List;

import lombok.Data;

@Data
public class DevDataAlbum {

    private String name;
    private String coverPath;
    private List<DevDataSong> songs;

}
