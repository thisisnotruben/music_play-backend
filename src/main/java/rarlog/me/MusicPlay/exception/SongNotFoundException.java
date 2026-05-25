package rarlog.me.MusicPlay.exception;

public class SongNotFoundException extends RuntimeException {

    public SongNotFoundException() {
        super("Song not found.");
    }
    
}
