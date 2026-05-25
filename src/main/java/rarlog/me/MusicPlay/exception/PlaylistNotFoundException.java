package rarlog.me.MusicPlay.exception;

public class PlaylistNotFoundException extends RuntimeException {

    public PlaylistNotFoundException() {
        super("Playlist not found.");
    }
    
}
