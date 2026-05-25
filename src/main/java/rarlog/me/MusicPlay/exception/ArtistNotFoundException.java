package rarlog.me.MusicPlay.exception;

public class ArtistNotFoundException extends RuntimeException {

    public ArtistNotFoundException() {
        super("Artist Not found.");
    }

}
