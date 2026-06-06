package rarlog.me.MusicPlay.exception;

public class AlbumNotFoundException extends RuntimeException {

    public AlbumNotFoundException() {
        super("Album not found.");
    }

}
